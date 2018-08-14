package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;

/**
 * Created by Raju on 24-07-2018.
 */
public class DashboardHierarchyFragment extends MOSLCommonFragment {
    private View secondClickView;
    private TableFixHeaders table;
    private SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    static Context mContext;
    private Map addtionalParameterMap;
    private Map hierarchyMap = new HashMap<>();
    private String leadId = "0", ucid = "0";
    private String mobileNo;
    private boolean dataFoundFlag;
    ArrayList<HashMap<String, String>> hierarchyMapList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        secondClickView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_dashboard_second_click);
        setUpView(secondClickView);
        hideAppTitle(getClass().toString());
        setUserVisibleHint(true);
        return secondClickView;
    }


    private void setUpView(View secondClickView) {
        table = (TableFixHeaders) secondClickView.findViewById(R.id.table);
        tvNochart = (TextView) secondClickView.findViewById(R.id.tvNochart);
        txtLables = (TextView) secondClickView.findViewById(R.id.txtLables);
        if (getArguments().containsKey(LABEL)) {
            txtLables.setText(this.getArguments().getString(LABEL));
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && secondClickView != null) {
                Bundle bundle = this.getArguments();
                if (bundle != null) {
                    hierarchyMap = (Map) bundle.getSerializable(HIERARCHY_MAP);
                    addtionalParameterMap = (Map) bundle.getSerializable(ADDITIONAL_MAP);
                }
                webServiceCall();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void webServiceCall() {

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoaddetailsWidgeAsync";
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), hierarchyMap, new TextHttpResponseHandler() {

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
                                        secondClickTableAdapter = new SecondClickTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
                                        table.setAdapter(secondClickTableAdapter);
                                        StringMap responseJsonObj = (StringMap) response.get("Response");
                                        List<StringMap> arrayDetail = (List<StringMap>) responseJsonObj.get("Data");
                                        ArrayList<String> columns = new ArrayList<String>();
                                        ArrayList<String> columnsHeader = new ArrayList<String>();
                                        if (arrayDetail.size() != 0) {
                                            for (StringMap objDetail : arrayDetail) {
                                                if (columns.size() == 0) {
                                                    for (Object columnName : objDetail.keySet()) {
                                                        columns.add(columnName.toString());
                                                        if (columnName.toString().equalsIgnoreCase("WidgetSystemName") ||
                                                                columnName.toString().equalsIgnoreCase("flg_empcode")) {
                                                        } else {
                                                            columnsHeader.add(getRefreshColumnName(columnName.toString()));

                                                        }
                                                    }
                                                    String[] stockArr = new String[columnsHeader.size()];
                                                    stockArr = columnsHeader.toArray(stockArr);
                                                    secondClickTableAdapter.setHeaders(stockArr);
                                                }
                                                HashMap map = new HashMap();
                                                LinkedHashMap<String, String> hm = new LinkedHashMap<>();
                                                for (String column : columns) {
                                                    if (column.equalsIgnoreCase("WidgetSystemName") ||
                                                            column.toString().equalsIgnoreCase("flg_empcode")) {
                                                        map.put("flg_empcode", objDetail.get("flg_empcode").toString());
                                                        map.put("WidgetSystemName", objDetail.get("WidgetSystemName").toString());

                                                    } else {
                                                        String columnData = (objDetail.get(column) == null || objDetail.get(column).toString() == null) ? "" : objDetail.get(column).toString();
                                                        if (column.equalsIgnoreCase("MOBILE")) {
                                                            mobileNo = columnData;
                                                        } else if (column.equalsIgnoreCase("MOBILE_NUMBER")) {
                                                            mobileNo = columnData;
                                                        }
                                                        if (objDetail.get(column) instanceof Number) {
                                                            String strValue = objDetail.get(column).toString().trim();
                                                            if (strValue.indexOf(".") > -1 && !strValue.endsWith(".0") && !strValue.endsWith(".00")) {
                                                                columnData = String.format("%.2f", Double.parseDouble(objDetail.get(column).toString()));
                                                            } else {
                                                                columnData = String.format("%.0f", Double.parseDouble(objDetail.get(column).toString()));
                                                            }
                                                        }
                                                        hm.put(getRefreshColumnName(column), columnData);
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
                                            hideProgress();
                                        } else {
                                            hideProgress();
                                            dataFoundFlag = false;
                                            table.setVisibility(View.GONE);
                                            tvNochart.setVisibility(View.VISIBLE);
                                        }

                                    } else {
                                        hideProgress();
                                        dataFoundFlag = false;
                                        table.setVisibility(View.GONE);
                                        tvNochart.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    hideProgress();
                                    dataFoundFlag = false;
                                    table.setVisibility(View.GONE);
                                    tvNochart.setVisibility(View.VISIBLE);
                                }

                            } else {
                                hideProgress();
                                dataFoundFlag = false;
                                table.setVisibility(View.GONE);
                                tvNochart.setVisibility(View.VISIBLE);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            dataFoundFlag = false;
                            table.setVisibility(View.GONE);
                            tvNochart.setVisibility(View.VISIBLE);
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
                                dataFoundFlag = false;
                                table.setVisibility(View.GONE);
                                tvNochart.setVisibility(View.VISIBLE);
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }


    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
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

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final CommonHeader commonHeaders[];
        private String headers[] = {};
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public SecondClickTableAdapter(Context context, ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[]{new CommonHeader("asd")};
            this.hashArray = hashArray;
        }

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        @Override
        public int getRowCount() {
            try {
                return commonHeaders[0].list.size();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }

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
        public View getView(final int row, int column, View convertView, ViewGroup parent) {
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
                        public void onClick(View v) {
                            if (!isBlank(getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode", "LEADID/CLIENTCODE"))) {
                                leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode", "LEADID/CLIENTCODE");
                            } else {
                                leadId = "0";
                            }
                            if (!isBlank(getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid"))) {
                                ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid");
                            } else {
                                ucid = "0";
                            }

                            if (hierarchyMapList.size() >= 1) {
                                if (!isBlank(getValue(row, "RM Name"))) {
                                    addtionalParameterMap.put("flt_level", getValue(row, "Hierarchy"));
                                    addtionalParameterMap.put("flt_Hierarchy_empCode", hierarchyMapList.get(row).get("flg_empcode").toString());
                                    hierarchyMap.put("WidgetSystemName", hierarchyMapList.get(row).get("WidgetSystemName").toString());
                                    hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(HIERARCHY_MAP, (Serializable) hierarchyMap);
                                    bundle.putSerializable(ADDITIONAL_MAP, (Serializable) addtionalParameterMap);
                                    bundle.putString(LABEL, txtLables.getText().toString());
                                    navigateTo(NAV_TO_SECOND_LEVEL_HIERARCHY_REPORT, bundle, getFragmentManager(), true);

                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString(LEAD_ID, leadId);
                            bundle.putString(UCID, ucid);
                            if (!leadId.equalsIgnoreCase("0") || !ucid.equalsIgnoreCase("0")) {
                                navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(), true);
                            }

                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isBlank(getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode", "LEADID/CLIENTCODE"))) {
                                leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode", "LEADID/CLIENTCODE");
                            } else {
                                leadId = "0";
                            }
                            if (!isBlank(getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid"))) {
                                ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid");
                            } else {
                                ucid = "0";
                            }
                            if (hierarchyMapList.size() >= 1) {
                                if (!isBlank(getValue(row, "RM Name"))) {
                                    addtionalParameterMap.put("flt_level", getValue(row, "Hierarchy"));
                                    addtionalParameterMap.put("flt_Hierarchy_empCode", hierarchyMapList.get(row).get("flg_empcode").toString());
                                    hierarchyMap.put("WidgetSystemName", hierarchyMapList.get(row).get("WidgetSystemName").toString());
                                    hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(HIERARCHY_MAP, (Serializable) hierarchyMap);
                                    bundle.putSerializable(ADDITIONAL_MAP, (Serializable) addtionalParameterMap);
                                    bundle.putString(LABEL, txtLables.getText().toString());
                                    navigateTo(NAV_TO_SECOND_LEVEL_HIERARCHY_REPORT, bundle, getFragmentManager(), true);

                                }
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString(LEAD_ID, leadId);
                            bundle.putString(UCID, ucid);
                            if (!leadId.equalsIgnoreCase("0") || !ucid.equalsIgnoreCase("0")) {
                                navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(), true);
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
                height = getResources().getDimensionPixelSize(R.dimen._45sdp);
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


    private String getRefreshColumnName(String columnName) {
        int kendoPosition = columnName.indexOf("__KENDOGRP__");
        if (kendoPosition > 0) {

            String prefixName = columnName.split("__KENDOGRP__")[0];

            if (headerNameRemove(prefixName)) { //remove
                columnName = columnName.substring(kendoPosition);
            }

        }
        if (columnName.equalsIgnoreCase("lead_id")) {
            columnName = columnName.toUpperCase();
        }
        columnName = columnName.replace("__KENDOGRP__", " ");
        columnName = columnName.replace("__KENDOPERCENTAGE__", "%");
        columnName = columnName.replace("__KENDOSLASH__", "/");
        columnName = columnName.replace("__KENDOPLUS__", "+");
        columnName = columnName.replace("__KENDODASH__", "-");
        columnName = columnName.replace("__KENDODOT__", ".");
        columnName = columnName.replace("__KENDORNDBRCOPN__", "(");
        columnName = columnName.replace("__KENDORNDBRCCLS__", ")");
        columnName = columnName.replace("__KENDOEMPTY__", " ");
        columnName = columnName.replace("flg_Hierarchy", "Hierarchy");
        columnName = columnName.replace("flt_Hierarchy_empCode", "RM Name");
        columnName = columnName.replace("_", " ");

        return columnName;
    }

    private boolean headerNameRemove(String prefixName) {
        String[] headerArray = {"Mtd_Revenue", "Ytd_Revenue"};

        for (String headerNmae : headerArray) {
            if (headerNmae.equalsIgnoreCase(prefixName)) {
                return false;
            }
        }
        return true;
    }


}