package com.mosl.tabapp.dashboard.leads.report;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mosl.tabapp.model.dashboard.DashboardModel;
import io.reactivex.disposables.Disposable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
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
    LeadTableAdapter
        leadTableAdapter =
        new LeadTableAdapter(requireActivity(), map, navigator, widgetSystemName);
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
      LeadTableAdapter leadTableAdapter) {
    try {
      if (responseJsonObj.get("Table2") == null) return;
      List<Map<String, Object>> jsonArray =
          (List<Map<String, Object>>) responseJsonObj.get("Table2");
      Map<Integer, List<Map<Integer, String>>> tableHeaderMap = new HashMap<>();
      if (jsonArray == null) return;
      for (int ctr = 0; ctr < jsonArray.size(); ctr++) {
        Map<String, Object> jsonRowObject = jsonArray.get(ctr);
        int groupId = Integer.parseInt(jsonRowObject.get("GroupId").toString());
        int childid = Integer.parseInt(jsonRowObject.get("childid").toString());
        String rowFilter = jsonRowObject.get("RowFilter").toString();

        List<Map<Integer, String>> childList = tableHeaderMap.get(groupId);
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
}
