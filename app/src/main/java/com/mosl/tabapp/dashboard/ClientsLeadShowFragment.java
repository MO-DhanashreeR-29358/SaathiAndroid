package com.mosl.tabapp.dashboard;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsFragment;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.reactivex.disposables.Disposable;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.FILTERFIRST;
import static com.mosl.tabapp.constants.MOSLConstants.FILTERSECOND;
import static com.mosl.tabapp.constants.MOSLConstants.FILTERTHIRD;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TYPESOFLEAD;
import static com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsFragment.FLT_BANKBRANCHCODE;
import static com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsFragment.FLT_BANKNAME;
import static com.mosl.tabapp.dashboard.ausfb.leads.AuLeadsFragment.FLT_BANKSUBBROKERCODE;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/15/2017.
 */

public class ClientsLeadShowFragment extends CommonFragment implements View.OnClickListener {

    private TableFixHeaders table;
    private TextView tvNochart;
    private DashboardModel dashboardModel;
    private HashMap<String, String> map;
    private String widgetSystemName;
    private Map<String, String> addtionalParameterMap = new HashMap<>();
    private LinkedHashMap lMap;
    private SparseArray tabaleHeaderMap = new SparseArray<>();

    public static ClientsLeadShowFragment newInstance() {
        ClientsLeadShowFragment fragment = new ClientsLeadShowFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View leadShowView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(Objects.requireNonNull(leadShowView));
        hideAppTitle();
        return leadShowView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_table_lead_show;
    }

    private void setUpView(View leadShowView) {
        table = leadShowView.findViewById(R.id.table);
        tvNochart = leadShowView.findViewById(R.id.tvNochart);
        TextView tvShowLess = leadShowView.findViewById(R.id.tvShowLess);
        tvShowLess.setPaintFlags(tvShowLess.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        dashboardModel = (DashboardModel) Objects.requireNonNull(this.getArguments()).getSerializable(DASHBOARD_MODEL);
        try {
            map = (HashMap<String, String>) this.getArguments().getSerializable(MAP);
            widgetSystemName = this.getArguments().getString(SYSTEM_NAME);

        } catch (Exception e) {
            e.printStackTrace();
        }

        newWebServiceCall(widgetSystemName, map);
        tvShowLess.setOnClickListener(this);
    }

    private void renderLeadTable(Map<String, Object> responseJsonObj) {
        //hideProgress();
        LeadTableAdapter leadTableAdapter = new LeadTableAdapter(new ArrayList<HashMap<String, String>>());
        table.setAdapter(leadTableAdapter);

        newPrepareGroupMap(responseJsonObj); //prepare group map

        List<Map<String, Object>> arrayDetail =
                (List<Map<String, Object>>) responseJsonObj.get("Table");
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> header = new ArrayList<String>();

        if (Objects.requireNonNull(arrayDetail).size() != 0) {

            for (int y = 2; y < arrayDetail.size(); y++) {
                Map<String, Object> objDetailSecond =
                        arrayDetail.get(0);
                if (columns.size() == 0) {
                    for (Object keySetE : objDetailSecond.keySet()) {
                        columns.add(keySetE.toString());
                    }
                    for (String column : columns) {
                        String columnData = Objects.requireNonNull(objDetailSecond.get(column)).toString().equals("") ? ""
                                : Objects.requireNonNull(objDetailSecond.get(column)).toString();
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
                            Objects.requireNonNull(objDetail.get(column))
                                    .toString()
                                    .equals("") ? "0" : Objects.requireNonNull(objDetail.get(column)).toString();
                    hm.put(column, columnData);
                }
                leadTableAdapter.add(hm);
            }
            leadTableAdapter.notifyDataSetChanged();
            table.setVisibility(View.VISIBLE);
            tvNochart.setVisibility(View.GONE);
        } else {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
        }
    }

    private void renderClientLeadShow(UiModel<Map<String, Object>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
            tvNochart.setVisibility(View.VISIBLE);
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
            ClientRequest request = new ClientRequest(
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
                        String status = Objects.requireNonNull(response.get(STATUS)).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return responseJsonObj;
                        } else {
                            String message = Objects.requireNonNull(response.get(MESSAGE)).toString();
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
            if (Objects.requireNonNull(Objects.requireNonNull(this.getArguments()).
                    getString(TYPESOFLEAD)).equalsIgnoreCase(LEAD)) {
                leadsFragment = new ClientsLeadsFragment();
            } else {
                leadsFragment = new AuLeadsFragment();
                args.putInt(FLT_BANKNAME, Objects.requireNonNull(this.getArguments()).getInt(FLT_BANKNAME));
                args.putInt(FLT_BANKBRANCHCODE, Objects.requireNonNull(this.getArguments()).getInt(FLT_BANKBRANCHCODE));
                args.putInt(FLT_BANKSUBBROKERCODE, Objects.requireNonNull(this.getArguments()).getInt(FLT_BANKSUBBROKERCODE));
            }
            args.putSerializable(DASHBOARD_MODEL, dashboardModel);
            args.putInt(FILTERFIRST, Objects.requireNonNull(this.getArguments()).getInt(FILTERFIRST));
            args.putInt(FILTERSECOND, Objects.requireNonNull(this.getArguments()).getInt(FILTERSECOND));
            args.putInt(FILTERTHIRD, Objects.requireNonNull(this.getArguments()).getInt(FILTERTHIRD));
            leadsFragment.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.llReplaceClientFragment, leadsFragment)
                    .commit();
        }
    }

    private class CommonHeader {
        private final String name;
        private final List<Item> list;

        CommonHeader(String name) {
            this.name = name;
            list = new ArrayList<Item>();
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

    private String getRowKeyName(Double childId) {
        for (int ctr = 0; ctr < tabaleHeaderMap.size(); ctr++) {
            int groupId = tabaleHeaderMap.keyAt(ctr);
            List<Map> childList = (List) tabaleHeaderMap.get(groupId);
            for (Map childMap : childList) {
                Set<Integer> childListSet = childMap.keySet();
                for (Integer childIdFromMap : childListSet) {
                    if (childId == childIdFromMap.intValue()) {
                        return Objects.requireNonNull(childMap.get(childIdFromMap)).toString();
                    }
                }
            }
        }
        return null;
    }

    private boolean isGroup(double id) {
        for (int ctr = 0; ctr < tabaleHeaderMap.size(); ctr++) {
            int groupId = tabaleHeaderMap.keyAt(ctr);
            if (groupId == id) return true;
        }
        return false;
    }

    private void newPrepareGroupMap(Map<String, Object> responseJsonObj) {
        try {
            if (responseJsonObj.get("Table2") == null) return;
            List<Map<String, Object>> jsonArray =
                    (List<Map<String, Object>>) responseJsonObj.get("Table2");
            if (jsonArray == null) return;
            for (int ctr = 0; ctr < jsonArray.size(); ctr++) {
                Map<String, Object> jsonRowObject = jsonArray.get(ctr);
                int groupId = Integer.parseInt(Objects.requireNonNull(jsonRowObject.get("GroupId")).toString());
                int childid = Integer.parseInt(Objects.requireNonNull(jsonRowObject.get("childid")).toString());
                String rowFilter = Objects.requireNonNull(jsonRowObject.get("RowFilter")).toString();

                List<Map> childList = (List) tabaleHeaderMap.get(groupId);
                if (childList == null) {
                    childList = new ArrayList<>();
                }
                Map childMap = new HashMap();
                childMap.put(childid, rowFilter);
                childList.add(childMap);
                tabaleHeaderMap.put(groupId, childList);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class LeadTableAdapter extends BaseTableAdapter {
        private final CommonHeader[] commonHeaders;

        private String[] headers = {};

        private String[] headers2 = {};

        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        LeadTableAdapter(ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[]{new CommonHeader("asd")};
            this.hashArray = hashArray;
        }

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        void setHeadersSecond(String[] headers) {
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

        @Override
        public int getColumnCount() {
            return headers.length - 1;
        }

        @Override
        public View getView(final int row, final int column, View convertView, ViewGroup parent) {
            final View view;

            switch (getItemViewType(row, column)) {
                case 0:
                    view = getFirstHeader(convertView, parent);
                    break;
                case 1:
                    view = getHeader(column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(view1 -> {
                        int newColumn = column + 1;
                        Set<String> stringSet = hashArray.get(row).keySet();
                        String columnName = Objects.requireNonNull(stringSet.toArray())[newColumn].toString();
                        addtionalParameterMap.clear();
                        addtionalParameterMap.put(COL_FILTER, columnName);
                        addtionalParameterMap.put(ROW_FILTER, Objects.requireNonNull(getRowKeyName(
                                Double.parseDouble(Objects.requireNonNull(hashArray.get(row).get("id"))))));
                        addtionalParameterMap.putAll(map);
                        Bundle args = new Bundle();
                        args.putString(SYSTEM_NAME, widgetSystemName);
                        args.putString(LABEL, LEAD);
                        args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                        lMap = (LinkedHashMap) hashArray.get(row);
                        String columnData = Objects.requireNonNull(lMap.get(columnName)).toString();
                        if (!columnData.trim().equalsIgnoreCase("0")) {
                            navigator.openPage(
                                    NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
                                    args,
                                    true, false);
                        }

                    });
                    break;
                case 4:
                    view = getFamilyView(row, column, convertView, parent);
                    break;
                default:
                    throw new RuntimeException("runtime exception");
            }
            return view;
        }

        //click on symbol header
        private View getFirstHeader(View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView =
                        inflater.inflate(R.layout.item_table_header_first_lead, parent, false);
            }
            TextView head = convertView.findViewById(android.R.id.text1);
            try {
                head.setText(headers[1]);
            } catch (Exception e) {
                e.printStackTrace();
                tvNochart.setVisibility(View.VISIBLE);
            }
            head.setTypeface(null, Typeface.BOLD);
            return convertView;
        }

        //click on header other than symbol
        private View getHeader(int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_lead, parent, false);
            }
            TextView head = convertView.findViewById(android.R.id.text1);

            TextView head1 = convertView.findViewById(android.R.id.text2);

            try {
                head1.setText(headers[column + 1]);
                head.setText(headers2[column + 1]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first_lead, parent, false);
            }
            LinearLayout listLayout =
                    convertView.findViewById(R.id.item_table_first_lead);
            TextView tv_table_first = convertView.findViewById(R.id.tv_table_first);

            try {
                if (hashArray.get(row).get("id") != null && isGroup(
                        Double.parseDouble(Objects.requireNonNull(hashArray.get(row).get("id"))))) {
                    tv_table_first.setTypeface(null, Typeface.BOLD);
                    tv_table_first.setTextColor(getResources().getColor(R.color.flush_orange));
                    listLayout.setBackgroundResource(R.color.lead_self_back);
                } else if (hashArray.get(row).get("stage") != null && (hashArray.get(row)
                        .get("stage")).equalsIgnoreCase("total")) {
                    tv_table_first.setTypeface(null, Typeface.BOLD);
                    tv_table_first.setTextColor(getResources().getColor(R.color.black));
                    listLayout.setBackgroundResource(R.color.secondaryBackground);
                } else {
                    tv_table_first.setTypeface(null, Typeface.BOLD);
                    tv_table_first.setTextColor(getResources().getColor(R.color.black));
                    listLayout.setBackgroundResource(R.color.lead_ekyc_back);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
            tv_table_first.setText(getDevice(row).data[column + 2]);
            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = convertView.findViewById(R.id.item_table);
            try {
                ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                        getDevice(row).data[column + 1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView text1 = convertView.findViewById(android.R.id.text1);
            if (hashArray.get(row).get("id") != null && isGroup(
                    Double.parseDouble(Objects.requireNonNull(hashArray.get(row).get("id"))))) {
                text1.setTextColor(getResources().getColor(R.color.flush_orange));
                listLayout.setBackgroundResource(R.color.lead_self_back);
            } else if (hashArray.get(row).get("stage") != null && hashArray.get(row)
                    .get("stage")
                    .equalsIgnoreCase("total")) {
                text1.setTypeface(null, Typeface.BOLD);
                text1.setTextColor(getResources().getColor(R.color.black));
                listLayout.setBackgroundResource(R.color.lead_total_back);
            } else {
                text1.setTextColor(getResources().getColor(R.color.black));
                listLayout.setBackgroundResource(R.color.lead_ekyc_back);
            }

            text1.setBackgroundResource(0);

            return convertView;
        }

        private View getFamilyView(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_family, parent, false);
            }
            final String string;
            if (column == -1) {
                string = getFamily(row).name;
            } else {
                string = "";
            }
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(string);
            return convertView;
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
    }
}

