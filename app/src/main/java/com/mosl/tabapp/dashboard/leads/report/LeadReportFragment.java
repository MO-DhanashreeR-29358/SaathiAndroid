package com.mosl.tabapp.dashboard.leads.report;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.CommonRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.dashboard.ClientsLeadsFragmentOld;
import com.mosl.tabapp.dashboard.leads.LeadsFragment;
import com.mosl.tabapp.databinding.FragTableLeadShowBinding;
import com.mosl.tabapp.databinding.ItemTableBinding;
import com.mosl.tabapp.databinding.ItemTableFamilyBinding;
import com.mosl.tabapp.databinding.ItemTableFirstLeadBinding;
import com.mosl.tabapp.databinding.ItemTableHeaderFirstLeadBinding;
import com.mosl.tabapp.databinding.ItemTableHeaderLeadBinding;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.navigation.ScreenNavigatorImpl;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import io.reactivex.disposables.Disposable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TYPESOFLEAD;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.FLT_BANKBRANCHCODE;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.FLT_BANKNAME;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.FLT_BANKSUBBROKERCODE;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.PERIOD_FILTER;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.PRODUCT_FILTER;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.SOURCE_FILTER;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class LeadReportFragment extends CommonFragment implements View.OnClickListener {
  private FragTableLeadShowBinding binding;
  private DashboardModel dashboardModel;
  private HashMap<String, String> map;
  private String widgetSystemName;

  @Override
  public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View leadShowView = super.onCreateView(inflater, container, savedInstanceState);
    binding = FragTableLeadShowBinding.bind(leadShowView);
    setUpView();
    hideAppTitle();
    return leadShowView;
  }

  @Override
  protected int getLayout() {
    return R.layout.frag_table_lead_show;
  }

  private void setUpView() {
    binding.tvShowLess.setPaintFlags(
        binding.tvShowLess.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
    try {
      map = (HashMap<String, String>) this.getArguments().getSerializable(MAP);
      widgetSystemName = this.getArguments().getString(SYSTEM_NAME);
    } catch (Exception e) {
      e.printStackTrace();
    }

    newWebServiceCall(widgetSystemName, map);
    binding.tvShowLess.setOnClickListener(this);
  }

  private void renderLeadTable(Map<String, Object> responseJsonObj) {
    //hideProgress();
    LeadTableAdapterJava
        leadTableAdapter =
        new LeadTableAdapterJava(requireActivity(), map, navigator, widgetSystemName);
    binding.table.setAdapter(leadTableAdapter);

    newPrepareGroupMap(responseJsonObj, leadTableAdapter); //prepare group map

    List<Map<String, Object>> arrayDetail =
        (List<Map<String, Object>>) responseJsonObj.get("Table");
    ArrayList<String> columns = new ArrayList<String>();
    ArrayList<String> header = new ArrayList<String>();

    if (arrayDetail.size() != 0) {

      for (int y = 2; y < arrayDetail.size(); y++) {
        Map<String, Object> objDetailSecond =
            arrayDetail.get(0);
        if (columns.size() == 0) {
          for (Object keySetE : objDetailSecond.keySet()) {
            columns.add(keySetE.toString());
          }
          for (String column : columns) {
            String columnData = objDetailSecond.get(column).toString().equals("") ? ""
                : objDetailSecond.get(column).toString();
            header.add(columnData);
          }
          String[] stockArr = new String[header.size()];
          stockArr = header.toArray(stockArr);
          leadTableAdapter.setHeadersSecond(stockArr);
        }
        Map<String, Object> objDetail =
            arrayDetail.get(y);
        String[] stockArr = new String[columns.size()];
        stockArr = columns.toArray(stockArr);
        leadTableAdapter.setHeaders(stockArr);

        LinkedHashMap<String, String> hm = new LinkedHashMap<>();
        for (String column : columns) {
          String columnData =
              objDetail.get(column)
                  .toString()
                  .equals("") ? "0" : objDetail.get(column).toString();
          hm.put(column, columnData);
        }
        leadTableAdapter.add(hm);
      }
      leadTableAdapter.notifyDataSetChanged();
      binding.table.setVisibility(View.VISIBLE);
      binding.tvNochart.setVisibility(View.GONE);
    } else {
      binding.table.setVisibility(View.GONE);
      binding.tvNochart.setVisibility(View.VISIBLE);
    }
  }

  private void renderClientLeadShow(UiModel<Map<String, Object>> uiModel) {
    toggleProgress(uiModel.inProgress);

    Throwable error = uiModel.error;
    if (error != null) {
      showOnFailurePopup(error);
      binding.tvNochart.setVisibility(View.VISIBLE);
    }

    Map<String, Object> data = uiModel.data;
    if (data != null) {
      try {
        renderLeadTable(data);
      } catch (Exception e) {
        alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
      }
    }
  }

  private void newWebServiceCall(String systemName,
      Map<String, String> getAddtionalParameterMap) {
    if (CRMAppUtil.isNetworkEnabled(requireContext())) {
      String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
      CommonRequest request = new CommonRequest(
          businessProfileId,
          getAddtionalParameterMap,
          systemName,
          dashboardModel.getProductId(),
          dashboardModel.getEmployeeCode()
      );

      Disposable subscribe = getSaathiApi().getDashboardData(request)
          .map(responseS -> {
            Gson mGson = new Gson();
            Type type = new TypeToken<HashMap<String, Object>>() {
            }.getType();
            HashMap<String, Object> response = mGson.fromJson(responseS, type);
            Map<String, Object> responseJsonObj =
                (Map<String, Object>) response.get("Response");
            String status = response.get(STATUS).toString();
            if (status.equalsIgnoreCase("true")) {
              return responseJsonObj;
            } else {
              String message = response.get(MESSAGE).toString();
              throw new ServiceFailureException(message, message);
            }
          })
          .compose(applyUiModel())
          .subscribe(this::renderClientLeadShow);
    } else {
      noInternetConnectionDialog();
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    if (id == R.id.tvShowLess) {
      Bundle args = new Bundle();
      CommonFragment leadsFragment;
      if (this.getArguments().
          getString(TYPESOFLEAD).equalsIgnoreCase(LEAD)) {
        leadsFragment = new ClientsLeadsFragmentOld();
      } else {
        leadsFragment = new LeadsFragment();
        args.putInt(FLT_BANKNAME, this.getArguments().getInt(FLT_BANKNAME));
        args.putInt(FLT_BANKBRANCHCODE, this.getArguments().getInt(FLT_BANKBRANCHCODE));
        args.putInt(FLT_BANKSUBBROKERCODE, this.getArguments().getInt(FLT_BANKSUBBROKERCODE));
      }
      args.putSerializable(DASHBOARD_MODEL, dashboardModel);
      args.putInt(PERIOD_FILTER, this.getArguments().getInt(PERIOD_FILTER));
      args.putInt(PRODUCT_FILTER, this.getArguments().getInt(PRODUCT_FILTER));
      args.putInt(SOURCE_FILTER, this.getArguments().getInt(SOURCE_FILTER));
      leadsFragment.setArguments(args);
      requireActivity().getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.llReplaceClientFragment, leadsFragment)
          .commit();
    }
  }

  private void newPrepareGroupMap(Map<String, Object> responseJsonObj,
      LeadTableAdapterJava leadTableAdapter) {
    try {
      if (responseJsonObj.get("Table2") == null) return;
      List<Map<String, Object>> jsonArray =
          (List<Map<String, Object>>) responseJsonObj.get("Table2");
      SparseArray tableHeaderMap = new SparseArray<>();
      if (jsonArray == null) return;
      for (int ctr = 0; ctr < jsonArray.size(); ctr++) {
        Map<String, Object> jsonRowObject = jsonArray.get(ctr);
        int groupId = Integer.parseInt(jsonRowObject.get("GroupId").toString());
        int childid = Integer.parseInt(jsonRowObject.get("childid").toString());
        String rowFilter = jsonRowObject.get("RowFilter").toString();

        List<Map> childList = (List) tableHeaderMap.get(groupId);
        if (childList == null) {
          childList = new ArrayList<>();
        }
        Map childMap = new HashMap();
        childMap.put(childid, rowFilter);
        childList.add(childMap);
        tableHeaderMap.put(groupId, childList);
        leadTableAdapter.setTableMap(tableHeaderMap);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public class LeadTableAdapter extends BaseTableAdapter {
    private final CommonHeader[] commonHeaders;

    private String[] headers = {};

    private String[] headers2 = {};

    private ArrayList<HashMap<String, String>> hashArray;
    private FragmentActivity requireActivity;
    private String widgetSystemsName;
    private ScreenNavigatorImpl navigator;
    private Map<String, String> map;
    private SparseArray tableHeaderMap;

    LeadTableAdapter(FragmentActivity requireActivity, Map<String, String> map,
        ScreenNavigatorImpl navigator,
        String widgetSystemsName) {
      commonHeaders = new CommonHeader[] { new CommonHeader("asd") };
      hashArray = new ArrayList<>();
      this.requireActivity = requireActivity;
      this.map = map;
      this.navigator = navigator;
      this.widgetSystemsName = widgetSystemsName;
    }

    private void setHeaders(String[] headers) {
      this.headers = headers;
    }

    private void setTableMap(SparseArray tableHeaderMap) {
      this.tableHeaderMap = tableHeaderMap;
    }

    private void setHeadersSecond(String[] headers) {
      this.headers2 = headers;
    }

    @Override
    public int getRowCount() {
      return commonHeaders[0].list.size();
    }

    public void clear() {
    }

    public void add(HashMap<String, String> row) {
      hashArray.add(row);

      String[] rowData = new String[headers.length];
      int ctr = 0;
      for (String columnName : headers) {
        String value = row.get(columnName);
        rowData[ctr] = value;
        ctr++;
      }

      commonHeaders[0].list.add(new Item(rowData));
    }

    private String getRowKeyName(Double childId) {
      for (int ctr = 0; ctr < tableHeaderMap.size(); ctr++) {
        int groupId = tableHeaderMap.keyAt(ctr);
        List<Map> childList = (List<Map>) tableHeaderMap.get(groupId);
        for (Map childMap : childList) {
          Set<Integer> childListSet = childMap.keySet();
          for (Integer childIdFromMap : childListSet) {
            if (childId == childIdFromMap.intValue()) {
              return (String) childMap.get(childIdFromMap);
            }
          }
        }
      }
      return null;
    }

    private boolean isGroup(double id) {
      for (int ctr = 0; ctr < tableHeaderMap.size(); ctr++) {
        int groupId = tableHeaderMap.keyAt(ctr);
        if (groupId == id) return true;
      }
      return false;
    }

    @Override
    public int getColumnCount() {
      return headers.length - 1;
    }

    @Override
    public View getView(final int row, final int column, View convertView, ViewGroup parent) {
      final View view;

      switch (getItemViewType(row, column)) {
        case 0:
          view = getFirstHeader(requireActivity);
          break;
        case 1:
          view = getHeader(column, requireActivity);
          break;
        case 2:
          view = getFirstBody(row, column, requireActivity);

          break;
        case 3:
          view = getBody(row, column, requireActivity);
          view.setOnClickListener(view1 -> {
            int newColumn = column + 1;
            Set<String> stringSet = hashArray.get(row).keySet();
            String columnName = stringSet.toArray()[newColumn].toString();
            Map<String, String> addtionalParameterMap = new HashMap<>();
            addtionalParameterMap.put(COL_FILTER, columnName);
            addtionalParameterMap.put(ROW_FILTER, getRowKeyName(
                Double.parseDouble(hashArray.get(row).get("id"))));
            addtionalParameterMap.putAll(map);
            Bundle args = new Bundle();
            args.putString(SYSTEM_NAME, widgetSystemsName);
            args.putString(LABEL, LEAD);
            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
            LinkedHashMap lMap = (LinkedHashMap) hashArray.get(row);
            String columnData = lMap.get(columnName).toString();
            if (!columnData.trim().equalsIgnoreCase("0")) {
              navigator.openPage(
                  NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
                  args,
                  true, false);
            }
          });
          break;
        case 4:
          view = getFamilyView(row, column, requireActivity);
          break;
        default:
          throw new RuntimeException("runtime exception");
      }
      return view;
    }

    //click on symbol header
    private View getFirstHeader(FragmentActivity context) {
      LayoutInflater inflater = LayoutInflater.from(context);
      ItemTableHeaderFirstLeadBinding binding =
          ItemTableHeaderFirstLeadBinding.inflate(inflater);
      binding.text1.setText(headers[1]);
      binding.text1.setTypeface(null, Typeface.BOLD);
      return binding.getRoot();
    }

    //click on header other than symbol
    private View getHeader(int column, FragmentActivity context) {
      LayoutInflater inflater = LayoutInflater.from(context);
      ItemTableHeaderLeadBinding binding =
          ItemTableHeaderLeadBinding.inflate(inflater);
      binding.text1.setText(headers[column + 1]);
      binding.text2.setText(headers2[column + 1]);

      return binding.getRoot();
    }

    private View getFirstBody(int row, int column, FragmentActivity context) {
      LayoutInflater inflater = LayoutInflater.from(context);
      ItemTableFirstLeadBinding binding =
          ItemTableFirstLeadBinding.inflate(inflater);

      try {
        if (hashArray.get(row).get("id") != null && isGroup(
            Double.parseDouble(hashArray.get(row).get("id")))) {
          binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
          binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.flush_orange));
          binding.itemTableFirstLead.setBackgroundResource(R.color.lead_self_back);
        } else if (hashArray.get(row).get("stage") != null && (hashArray.get(row)
            .get("stage")).equalsIgnoreCase("total")) {
          binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
          binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.black));
          binding.itemTableFirstLead.setBackgroundResource(R.color.secondaryBackground);
        } else {
          binding.tvTableFirst.setTypeface(null, Typeface.BOLD);
          binding.tvTableFirst.setTextColor(ContextCompat.getColor(context, R.color.black));
          binding.itemTableFirstLead.setBackgroundResource(R.color.lead_ekyc_back);
        }
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } catch (Resources.NotFoundException e) {
        e.printStackTrace();
      }
      binding.tvTableFirst.setText(getDevice(row).data[column + 2]);
      return binding.getRoot();
    }

    private View getBody(final int row, int column, FragmentActivity context) {
      LayoutInflater inflater = LayoutInflater.from(context);
      ItemTableBinding binding =
          ItemTableBinding.inflate(inflater);
      try {
        if (hashArray.get(row).get("id") != null && isGroup(
            Double.parseDouble(hashArray.get(row).get("id")))) {
          binding.text1.setTextColor(ContextCompat.getColor(context, R.color.flush_orange));
          binding.itemTable.setBackgroundResource(R.color.lead_self_back);
        } else if (hashArray.get(row).get("stage") != null && hashArray.get(row)
            .get("stage")
            .equalsIgnoreCase("total")) {
          binding.text1.setTypeface(null, Typeface.BOLD);
          binding.text1.setTextColor(getResources().getColor(R.color.black));
          binding.itemTable.setBackgroundResource(R.color.lead_total_back);
        } else {
          binding.text1.setTextColor(getResources().getColor(R.color.black));
          binding.itemTable.setBackgroundResource(R.color.lead_ekyc_back);
        }
        binding.text1.setText(getDevice(row).data[column + 1]);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } catch (Resources.NotFoundException e) {
        e.printStackTrace();
      }
      return binding.getRoot();
    }

    private View getFamilyView(int row, int column, FragmentActivity context) {
      LayoutInflater inflater = LayoutInflater.from(context);
      ItemTableFamilyBinding binding =
          ItemTableFamilyBinding.inflate(inflater);

      String string;
      if (column == -1) {
        string = getFamily(row).name;
      } else {
        string = "";
      }
      binding.text1.setText(string);
      return binding.getRoot();
    }

    @Override
    public int getWidth(int column) {
      int widthSize = 0;
      if (column == -1) {
        widthSize = getResources().getDimensionPixelSize(R.dimen._90sdp);
      }
      for (int i = 0; i < column; i++) {
        widthSize = getResources().getDimensionPixelSize(R.dimen._135sdp);
      }
      return widthSize;
    }

    @Override
    public int getHeight(int row) {
      final int height;
      if (row == -1) {
        height = getResources().getDimensionPixelSize(R.dimen._40sdp);
      } else {
        height = getResources().getDimensionPixelSize(R.dimen._40sdp);
      }
      return height;
    }

    @Override
    public int getItemViewType(int row, int column) {
      final int itemViewType;
      if (row == -1 && column == -1) {
        itemViewType = 0;
      } else if (row == -1) {
        itemViewType = 1;
      } else if (column == -1) {
        itemViewType = 2;
      } else {
        itemViewType = 3;
      }
      return itemViewType;
    }

    private CommonHeader getFamily(int row) {
      int family = 0;
      while (row >= 0) {
        row -= commonHeaders[family].size();
      }
      return commonHeaders[family];
    }

    private Item getDevice(int row) {
      int family = 0;
      while (row >= 0) {
        row -= commonHeaders[family].size();
      }
      return commonHeaders[family].get(row + commonHeaders[family].size());
    }

    @Override
    public int getViewTypeCount() {
      return 5;
    }

    private class CommonHeader {
      private final String name;
      private final List<Item> list;

      CommonHeader(String name) {
        this.name = name;
        list = new ArrayList<>();
      }

      public int size() {
        return list.size();
      }

      public Item get(int i) {
        return list.get(i);
      }
    }

    private class Item {
      private final String[] data;

      private Item(String[] rowData) {
        data = rowData;
      }
    }
  }
}
