package com.mosl.tabapp.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.model.TypeActionableFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;

/**
 * Created by raju on 6/21/2017.
 */


public class DashboardSecondLevelFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View secondClickView;
    private TableFixHeaders table;
    private SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    static Context mContext;
    private boolean dataFoundFlag;
    private Spinner spFilter, spTypeFilter;
    private String widgetSystemName;
    private ArrayList<WidgetFilterModel> filterList = new ArrayList<>();
    private ArrayList<TypeActionableFilterModel> typeFilterList = new ArrayList<>();
    private String filterString, typeFilterString;
    private LinearLayout linearLayout;
    private Map addtionalParameterMap = new HashMap<>();
    private Map hierarchyMap = new HashMap<>();
    private String leadId = "0", ucid = "0", leadName;
    private String mobileNo;
    private TextView tvDateTime;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate;
    private ImageView close;
    private Button btnSubmit;
    private int currentYear, month, day, hour, minute;
    private Calendar calendar = Calendar.getInstance();
    private RelativeLayout rlDate;
    private String mainFromDate, mainToDate, fromPostingDate, dateType, isActionable;
    private ImageView ivDatePicker;
    boolean dateFlag;
    private boolean spinnerDataFlag;
    ArrayList<HashMap<String, String>> hierarchyMapList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        secondClickView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_dashboard_second_click);
        setUpView(secondClickView);
        if (getArguments().containsKey(FILTER_LIST) && getArguments().containsKey(SPINNER_LIST)) {
            spinnerDataFlag = true;
        } else {
            spinnerDataFlag = false;
        }
        if (getArguments().containsKey(LABEL)) {
            isActionable = this.getArguments().getString(LABEL);
        }
        hideAppTitle(getClass().toString());
        setUserVisibleHint(true);
        return secondClickView;
    }


    private void setUpView(View secondClickView) {
        table = (TableFixHeaders) secondClickView.findViewById(R.id.table);
        tvNochart = (TextView) secondClickView.findViewById(R.id.tvNochart);
        txtLables = (TextView) secondClickView.findViewById(R.id.txtLables);
        rlDate = (RelativeLayout) secondClickView.findViewById(R.id.rlDate);
        tvDateTime = (TextView) secondClickView.findViewById(R.id.tvDate);
        ivDatePicker = (ImageView) secondClickView.findViewById(R.id.ivDatePicker);
        rlDate.setOnClickListener(this);
        spFilter = (Spinner) secondClickView.findViewById(R.id.spFilter);
        spTypeFilter = (Spinner) secondClickView.findViewById(R.id.spTypeFilter);
        linearLayout = (LinearLayout) secondClickView.findViewById(R.id.linearLayout);
        try {
            if (getArguments() != null) {
                widgetSystemName = this.getArguments().getString(SYSTEM_NAME);
                if (getArguments().containsKey(LABEL)) {
                    if (getArguments().containsKey(WIDGETNAME)) {
                        txtLables.setText(this.getArguments().getString(LABEL) + "-" + getArguments().getString(WIDGETNAME));
                    } else {
                        txtLables.setText(this.getArguments().getString(LABEL));
                    }
                }
                if (getArguments().containsKey(MAP)) {
                    addtionalParameterMap = (Map<String, String>) this.getArguments().getSerializable(MAP);
                }
                if (getArguments().containsKey(FILTER_LIST)) {
                    filterList = this.getArguments().getParcelableArrayList(FILTER_LIST);
                    if (filterList != null && filterList.size() > 0) {
                        if (filterList.get(0).getKeyValue().equalsIgnoreCase(FLT_DATE)) {
                            linearLayout.setVisibility(View.VISIBLE);
                            rlDate.setVisibility(View.VISIBLE);
                            dateFlag = true;
                            tvDateTime.setText(CRMAppUtil.getDateStr(new Date(), NEWDATE_FORMAT_OF_DD_MMM_YYYY));
                            dateType = FLT_DATE;
                            addtionalParameterMap.put(dateType, tvDateTime.getText().toString());
                            webServiceCall();
                        } else if (filterList.get(0).getKeyValue().equalsIgnoreCase(FLT_DATE_TIME)) {
                            linearLayout.setVisibility(View.VISIBLE);
                            rlDate.setVisibility(View.VISIBLE);
                            dateFlag = false;
                            tvDateTime.setText(CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
                            dateType = FLT_DATE_TIME;
                            addtionalParameterMap.put(dateType, tvDateTime.getText().toString());
                            webServiceCall();
                        } else {
                            spFilter.setVisibility(View.VISIBLE);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        webServiceCall();
                    }
                }
                if (getArguments().containsKey(SPINNER_LIST)) {

                    typeFilterList = this.getArguments().getParcelableArrayList(SPINNER_LIST);
                    if (!typeFilterList.isEmpty()) {
                        spTypeFilter.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        ArrayAdapter<TypeActionableFilterModel> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, typeFilterList);
                        adapter1.setDropDownViewResource(R.layout.mdropdown);
                        spTypeFilter.setAdapter(adapter1);
                    }

                }
                if (filterList != null && filterList.size() > 0) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter.setAdapter(adapter1);
                }
            }
            spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    filterString = ((FilterRowData) spFilter.getSelectedItem()).getValue().toString().replaceAll("_", " ");
                    if (spinnerDataFlag) {
                        isDataPresent();
                    } else {
                        spinnerDataFlag = false;
                        webServiceCall();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            spTypeFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    typeFilterString = spTypeFilter.getSelectedItem().toString().replaceAll("_", " ");
                    if (spinnerDataFlag) {
                        isDataPresent();
                    } else {
                        spinnerDataFlag = false;
                        webServiceCall();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void isDataPresent() {
        if (!isBlank(filterString) && !isBlank(typeFilterString)) {
            webServiceCall();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && secondClickView != null) {
                if (!isActionable.trim().equalsIgnoreCase(ACTIONABLE)) {
                    webServiceCall();
                } else if (isActionable.trim().equalsIgnoreCase(ACTIONABLE) && !getArguments().containsKey(SPINNER_LIST) && !getArguments().containsKey(FILTER_LIST)) {
                    webServiceCall();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void webServiceCall() {
        if (!isBlank(filterString)) {
            addtionalParameterMap.put(COL_FILTER, filterString);
        }
        if (!isBlank(typeFilterString)) {
            addtionalParameterMap.put(ROW_FILTER, typeFilterString);
        }

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoaddetailsWidgeAsync";
            final Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", widgetSystemName);
            inputMap.put("PageSize", DASHBOARD_REPORT_PAGE_SIZE);
            inputMap.put("Page", "1");
            if (addtionalParameterMap != null && !addtionalParameterMap.isEmpty()) {
                inputMap.put("AddtionalParameters", addtionalParameterMap);
            }
            hierarchyMap.putAll(inputMap);
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
                                                        if (columnName.toString().equalsIgnoreCase("WidgetSystemName") || columnName.toString().equalsIgnoreCase("flg_empcode")) {
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
                                                    if (column.equalsIgnoreCase("WidgetSystemName") || column.toString().equalsIgnoreCase("flg_empcode")) {
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlDate) {
            selectDate();
        }
    }

    public void selectDate() {
        today = Calendar.getInstance();

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialog.findViewById(R.id.datePickerFromDate);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        cal.add(cal.DAY_OF_MONTH, -3);
        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    currentYear = year;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                }
            });

        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    currentYear = year;
                    month = monthOfYear + 1;
                    day = dayOfMonth;
                }
            });

        }


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
                hour = arg1;
                minute = arg2;
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");

                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                        if (dateFlag) {
                            tvDateTime.setText(CRMAppUtil.getDateStr(fromDat, NEWDATE_FORMAT_OF_DD_MMM_YYYY));
                            addtionalParameterMap.put(dateType, tvDateTime.getText().toString());
                            webServiceCall();
                        } else {
                            String splitEndDate = stringArray(fromPostingDate);
                            String[] newEndDate = splitEndDate.split("-");
                            tvDateTime.setText(CRMAppUtil.getDBDate(CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]), Integer.parseInt(newEndDate[1]), Integer.parseInt(newEndDate[2]), hour, minute), DATE_FORMAT_TO_DISPLAY));
                            addtionalParameterMap.put(dateType, tvDateTime.getText().toString());
                            webServiceCall();
                        }

                    }
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
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
                                    addtionalParameterMap.put("flt_level",getValue(row, "Hierarchy"));
                                    addtionalParameterMap.put("flt_Hierarchy_empCode", hierarchyMapList.get(row).get("flg_empcode").toString());
                                    hierarchyMap.put("WidgetSystemName", hierarchyMapList.get(row).get("WidgetSystemName").toString());
                                    hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(HIERARCHY_MAP, (Serializable) hierarchyMap);
                                    bundle.putSerializable(ADDITIONAL_MAP, (Serializable) addtionalParameterMap);
                                    bundle.putString(LABEL,txtLables.getText().toString());
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
                                    addtionalParameterMap.put("flt_level",getValue(row, "Hierarchy"));                                    addtionalParameterMap.put("flt_Hierarchy_empCode", hierarchyMapList.get(row).get("flg_empcode").toString());
                                    hierarchyMap.put("WidgetSystemName", hierarchyMapList.get(row).get("WidgetSystemName").toString());
                                    hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable(HIERARCHY_MAP, (Serializable) hierarchyMap);
                                    bundle.putSerializable(ADDITIONAL_MAP, (Serializable) addtionalParameterMap);
                                    bundle.putString(LABEL,txtLables.getText().toString());
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

    private String stringArray(String date) {
        String[] spaceString = date.split(" ");

        return spaceString[0];
    }
}