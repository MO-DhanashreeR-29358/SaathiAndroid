package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 6/30/2017.
 */

public class BusinessSnapshotFragment extends MOSLCommonFragment {
    private View businessSnapshotView;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Spinner spFilter1, spFilter2, spFilter3;
    private TableFixHeaders table;
    private SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    private boolean dataFoundFlag;
    private Map addtionalParameterMap = new HashMap<>();
    private LinkedHashMap lMap;
    ArrayList<HashMap<String, String>> hierarchyMapList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        businessSnapshotView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_business_snapshot);
        hideAppTitle(getClass().toString());
        setUpView(businessSnapshotView);
        setUserVisibleHint(true);
        return businessSnapshotView;
    }

    public static BusinessSnapshotFragment newInstance(Context context) {
        BusinessSnapshotFragment fragment = new BusinessSnapshotFragment();

        return fragment;
    }

    private void setUpView(View businessSnapshotView) {

        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
        table = (TableFixHeaders) businessSnapshotView.findViewById(R.id.table);
        tvNochart = (TextView) businessSnapshotView.findViewById(R.id.tvNochart);
        spFilter1 = (Spinner) businessSnapshotView.findViewById(R.id.spFilter1);
        spFilter2 = (Spinner) businessSnapshotView.findViewById(R.id.spFilter2);
        spFilter3 = (Spinner) businessSnapshotView.findViewById(R.id.spFilter3);

        spFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        spFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        spFilter3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        if (dashboardModel != null) {
            DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getBusinessSnapshotSystemName());
            WidgetModel model = null;
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                model = widgetList.get(0);
                widgetSystemName = model.getSystemName();

                //set filter data
                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 2) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);


                    ArrayAdapter<FilterRowData> adapter2 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(1).getFilterData());
                    adapter2.setDropDownViewResource(R.layout.mdropdown);
                    spFilter2.setAdapter(adapter2);
                    spFilter2.setVisibility(View.VISIBLE);

                }
                if (filterList != null && filterList.size() == 3) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);


                    ArrayAdapter<FilterRowData> adapter2 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(1).getFilterData());
                    adapter2.setDropDownViewResource(R.layout.mdropdown);
                    spFilter2.setAdapter(adapter2);


                    ArrayAdapter<FilterRowData> adapter3 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(2).getFilterData());
                    adapter3.setDropDownViewResource(R.layout.mdropdown);
                    spFilter3.setAdapter(adapter3);

                    spFilter2.setVisibility(View.VISIBLE);
                    spFilter3.setVisibility(View.VISIBLE);
                } else if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);

                }
            }
        }

    }

    private Map getAddtionalParameterMap() {

        Map addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spFilter1.getSelectedItem();
            FilterRowData filter2 = (FilterRowData) spFilter2.getSelectedItem();
            FilterRowData filter3 = (FilterRowData) spFilter3.getSelectedItem();

            if (filter1 != null) {
                addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());

            }
            if (filter2 != null) {
                addtionalParameterMap.put(filter2.getKeyValue(), filter2.getValue());

            }
            if (filter3 != null) {
                addtionalParameterMap.put(filter3.getKeyValue(), filter3.getValue());

            }
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", widgetSystemName);
            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseS) {
                        try {
                            if (statusCode == 200) {
                                hierarchyMapList = new ArrayList<HashMap<String, String>>();
                                Gson mGson = new Gson();
                                Type type = new TypeToken<HashMap<String, Object>>() {
                                }.getType();
                                HashMap<String, Object> response = mGson.fromJson(responseS, type);
                                if (response.size() != 0) {
                                    String status = response.get(STATUS).toString();
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();
                                        secondClickTableAdapter = new SecondClickTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
                                        table.setAdapter(secondClickTableAdapter);
                                        StringMap responseJSONObject = (StringMap) response.get("Response");
                                        List<StringMap> arrayDetail = (List<StringMap>) responseJSONObject.get("Table");
                                        ArrayList<String> columns = new ArrayList<String>();
                                        ArrayList<String> columnsHeader = new ArrayList<String>();
                                        if (arrayDetail.size() != 0) {
                                            for (int y = 0; y < arrayDetail.size(); y++) {
                                                StringMap objDetail = arrayDetail.get(y);
                                                if (columns.size() == 0) {
                                                    for (Object columnName : objDetail.keySet()) {
                                                        if (!columnName.toString().equalsIgnoreCase("DisplayOrder")) {
                                                            columns.add(columnName.toString());
                                                            if (columnName.toString().equalsIgnoreCase("flg_empcode")) {
                                                            } else {
                                                                columnsHeader.add(getRefreshColumnName(columnName.toString()));
                                                            }
                                                        }
                                                    }
                                                    String[] stockArr = new String[columnsHeader.size()];
                                                    stockArr = columnsHeader.toArray(stockArr);
                                                    secondClickTableAdapter.setHeaders(stockArr);
                                                }
                                                HashMap map = new HashMap();
                                                LinkedHashMap<String, String> hm = new LinkedHashMap<>();
                                                for (String column : columns) {
                                                    if (column.toString().equalsIgnoreCase("flg_empcode")) {
                                                        map.put("flg_empcode", objDetail.get("flg_empcode").toString());

                                                    } else {
                                                        String columnData = (objDetail.get(column) == null || objDetail.get(column).toString() == null) ? "" : objDetail.get(column).toString();

                                                        if (objDetail.get(column) instanceof Number) {
                                                            String strValue = objDetail.get(column).toString().trim();
                                                            if (strValue.indexOf(".") > -1 && !strValue.endsWith(".0") && !strValue.endsWith(".00")) {
                                                                columnData = String.format("%.2f", Double.parseDouble(objDetail.get(column).toString()));
                                                            } else {
                                                                columnData = String.format("%.0f", Double.parseDouble(objDetail.get(column).toString()));
                                                            }
                                                        }

                                                        if (column.equalsIgnoreCase("lead_id")) {
                                                            column = column.toUpperCase();
                                                        }

                                                        hm.put(CRMAppUtil.getRefreshColumnName(column), columnData.trim());
                                                    }
                                                }
                                                secondClickTableAdapter.add(hm);
                                                if (map.size() != 0) {
                                                    hierarchyMapList.add(map);
                                                }
                                            }
                                            secondClickTableAdapter.notifyDataSetChanged();
                                            dataFoundFlag = true;
                                            table.setVisibility(View.VISIBLE);
                                            tvNochart.setVisibility(View.GONE);
                                        } else {
                                            dataFoundFlag = false;
                                            table.setVisibility(View.GONE);
                                            tvNochart.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        hideProgress();
                                        String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(new JSONObject(responseS));
                                        table.setVisibility(View.GONE);
                                        dataFoundFlag = false;
                                        tvNochart.setVisibility(View.VISIBLE);
                                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }
                                } else {
                                    dataFoundFlag = false;
                                    table.setVisibility(View.GONE);
                                    tvNochart.setVisibility(View.VISIBLE);
                                }

                            } else {
                                dataFoundFlag = false;
                                table.setVisibility(View.GONE);
                                tvNochart.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            dataFoundFlag = false;
                            table.setVisibility(View.GONE);
                            tvNochart.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                        hideProgress();
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {
                            JSONObject errorResponse = new JSONObject(responseString);
                            hideProgress();
                            if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), UNAUTHORIZE_USER, "OK", false, new MSFDialog.DialogListener() {
                                    @Override
                                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                        if (action == MSFDialog.Action.OK) {
                                            Intent i = new Intent(getMainActivity(), LoginActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                                return;
                            } else {
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }


                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }

    ///SDTable
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

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final CommonHeader commonHeaders[];
        private String headers[] = {};
        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public SecondClickTableAdapter(Context context, ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[]{new CommonHeader("asd")};
            this.hashArray = hashArray;
            density = context.getResources().getDisplayMetrics().density;


        }

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        @Override
        public int getRowCount() {
            return commonHeaders[0].list.size();
        }

        public void clear() {
            //    hashArray.clear();
            //    commonHeaders[0].list.clear();
        }

        public void add(HashMap<String, String> row) {
            hashArray.add(row);

            String rowData[] = new String[headers.length];
            int ctr = 0;
            for (String columnName : headers) {
                String value = row.get(columnName).toString();
                rowData[ctr] = value;
                ctr++;
            }

            commonHeaders[0].list.add(new Item(rowData));

        }

        @Override
        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public View getView(final int row, final int column, View convertView, ViewGroup parent) {
            final View view;

            switch (getItemViewType(row, column)) {
                case 0:
                    view = getFirstHeader(row, column, convertView, parent);
                    break;
                case 1:
                    view = getHeader(row, column, convertView, parent);
                    break;
                case 2:
                    view = getFirstBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });


                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Set<String> stringSet = hashArray.get(row).keySet();
                            String columnName = stringSet.toArray()[column].toString();
                            addtionalParameterMap.put(COL_FILTER, columnName);
                            addtionalParameterMap.put(ROW_FILTER, hashArray.get(row).values().toArray()[0].toString());
                            addtionalParameterMap.putAll(getAddtionalParameterMap());
                            addtionalParameterMap.put("flt_Hierarchy_empCode", hierarchyMapList.get(row).get("flg_empcode").toString());
                            Bundle args = new Bundle();
                            args.putString(SYSTEM_NAME, widgetSystemName);
                            args.putString(LABEL, BUSINESS_SNAPSHOT_TITLE);
                            args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                            lMap = (LinkedHashMap) hashArray.get(row);
                            String columnData = lMap.get(columnName).toString();
                            if (columnData != null) {
                                if (!columnData.trim().equalsIgnoreCase("0")) {
                                    navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                                }
                            }

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

        private String getValue(int row, String... keys) {
            for (String key : keys) {
                String value = hashArray.get(row).get(key);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        //click on symbol header
        private View getFirstHeader(final int row, int column, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
            }
            RelativeLayout relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                tvNochart.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }

            return convertView;
        }

        private void sortByAMCName(Integer sortVal, String clickedOn) {
            String sortCol = clickedOn; //((TextView) headerTextField).getText().toString();
            ArrayList<String> out = new ArrayList<String>();
            for (int j = 0; j < hashArray.size(); j++) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) hashArray.get(j);
                String val = (String) linkedHashMap.get(sortCol);
                out.add(val);
            }

            if (sortVal == 1) {
                Collections.sort(out, new Comparator<String>() {
                    public int compare(String c1, String c2) {
                        return c1.compareTo(c2);
                    }
                });
            } else {
                Collections.reverse(out);
            }

            commonHeaders[0].list.clear();
            ArrayList<HashMap<String, String>> tempHashArray = (ArrayList<HashMap<String, String>>) hashArray.clone();
            hashArray.clear();

            for (int i = 0; i < out.size(); i++) {
                String sortedValue = out.get(i);
                for (int j = 0; j < tempHashArray.size(); j++) {
                    LinkedHashMap sortLinkedMap = (LinkedHashMap) tempHashArray.get(j);
                    String sortColVal = (String) sortLinkedMap.get(sortCol);

                    if (sortColVal.equals(sortedValue)) {
                        secondClickTableAdapter.add(sortLinkedMap);
                        tempHashArray.remove(sortLinkedMap);
                    }
                }
            }
            secondClickTableAdapter.notifyDataSetChanged();
        }


        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            head.setText(headers[column]);
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first = (TextView) convertView.findViewById(R.id.tv_table_first);
            tv_table_first.setText(getDevice(row).data[column + 1]);

            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table);
            listLayout.setBackgroundResource(row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(getDevice(row).data[column]);
            TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
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
            } else if (isFamily(row)) {
                height = 0;
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
            } else if (isFamily(row)) {
                itemViewType = 4;
            } else if (column == -1) {
                itemViewType = 2;
            } else {
                itemViewType = 3;
            }
            return itemViewType;
        }

        private boolean isFamily(int row) {

            if (row == -1 || row == 0) {
                int family = 0;
                while (row > 0) {
                    row -= commonHeaders[family].size();
                    family++;
                }
                return row == 1;
            }
            return row == 0;
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
