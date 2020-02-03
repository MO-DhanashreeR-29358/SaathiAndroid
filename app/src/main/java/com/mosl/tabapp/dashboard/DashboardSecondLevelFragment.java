package com.mosl.tabapp.dashboard;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.TypeActionableFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.disposables.Disposable;
import timber.log.Timber;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.constants.MOSLConstants.ACTIONABLE;
import static com.mosl.tabapp.constants.MOSLConstants.ADDITIONAL_MAP;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_REPORT_PAGE_SIZE;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_TO_DISPLAY;
import static com.mosl.tabapp.constants.MOSLConstants.FILTER_LIST;
import static com.mosl.tabapp.constants.MOSLConstants.FLT_DATE;
import static com.mosl.tabapp.constants.MOSLConstants.FLT_DATE_TIME;
import static com.mosl.tabapp.constants.MOSLConstants.HIERARCHY_MAP;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.NEWDATE_FORMAT_OF_DD_MMM_YYYY;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.SPINNER_LIST;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;
import static com.mosl.tabapp.constants.MOSLConstants.WIDGETNAME;
import static com.mosl.tabapp.constants.MOSLConstants.WIDGETTYPE;
import static com.mosl.tabapp.navigation.NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 6/21/2017.
 */

public class DashboardSecondLevelFragment extends CommonFragment
        implements View.OnClickListener {
    private final static String TAG = DashboardSecondLevelFragment.class.getSimpleName();
    private boolean dateFlag;
    private ArrayList<HashMap<String, String>> hierarchyMapList;
    private TableFixHeaders table;
    private TextView tvNochart, txtLables;
    private Spinner spFilter, spTypeFilter;
    private String widgetSystemName;
    private ArrayList<WidgetFilterModel> filterList = new ArrayList<>();
    private String filterString, typeFilterString;
    private Map addtionalParameterMap;
    private Map addtionalParameterMapNew;
    private Map hierarchyMap = new HashMap<>();
    private String leadId = "0", ucid = "0";
    private TextView tvDateTime;
    private Date fromDat;
    private Dialog dialog;
    private int hour, minute;
    private String mainFromDate, fromPostingDate, dateType, isActionable
            = "";
    private boolean spinnerDataFlag;

    @Override
    protected int getLayout() {
        return R.layout.frag_dashboard_second_click;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View secondClickView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(secondClickView);
        if (Objects.requireNonNull(getArguments()).containsKey(FILTER_LIST) && getArguments().containsKey(SPINNER_LIST)) {
            spinnerDataFlag = true;
        } else {
            spinnerDataFlag = false;
        }
        if (getArguments().containsKey(LABEL)) {
            isActionable = this.getArguments().getString(LABEL);
        }
        hideAppTitle();
        return secondClickView;
    }

    private void setUpView(View secondClickView) {
        table = secondClickView.findViewById(R.id.table);
        tvNochart = secondClickView.findViewById(R.id.tvNochart);
        txtLables = secondClickView.findViewById(R.id.txtLables);
        RelativeLayout rlDate = secondClickView.findViewById(R.id.rlDate);
        tvDateTime = secondClickView.findViewById(R.id.tvDate);
        rlDate.setOnClickListener(this);
        spFilter = secondClickView.findViewById(R.id.spFilter);
        spTypeFilter = secondClickView.findViewById(R.id.spTypeFilter);
        LinearLayout linearLayout = secondClickView.findViewById(R.id.linearLayout);
        try {
            if (getArguments() != null) {
                widgetSystemName = this.getArguments().getString(SYSTEM_NAME);
                if (getArguments().containsKey(LABEL)) {
                    if (getArguments().containsKey(WIDGETNAME)) {
                        txtLables.setText(
                                this.getArguments().getString(LABEL) + "-" + getArguments().getString(
                                        WIDGETNAME));
                    } else {
                        txtLables.setText(this.getArguments().getString(LABEL));
                    }
                }
                if (getArguments().containsKey(MAP)) {
                    addtionalParameterMapNew =
                            (Map<String, String>) this.getArguments().getSerializable(MAP);
                }
                if (getArguments().containsKey(FILTER_LIST)) {
                    filterList = this.getArguments().getParcelableArrayList(FILTER_LIST);
                    if (filterList != null && filterList.size() > 0) {
                        if (filterList.get(0).getKeyValue().equalsIgnoreCase(FLT_DATE)) {
                            linearLayout.setVisibility(View.VISIBLE);
                            rlDate.setVisibility(View.VISIBLE);
                            dateFlag = true;
                            tvDateTime.setText(
                                    CRMAppUtil.getDateStr(new Date(), NEWDATE_FORMAT_OF_DD_MMM_YYYY));
                            dateType = FLT_DATE;
                            addtionalParameterMapNew.put(dateType, tvDateTime.getText().toString());
                            webServiceCall();
                        } else if (filterList.get(0)
                                .getKeyValue()
                                .equalsIgnoreCase(FLT_DATE_TIME)) {
                            linearLayout.setVisibility(View.VISIBLE);
                            rlDate.setVisibility(View.VISIBLE);
                            dateFlag = false;
                            tvDateTime.setText(
                                    CRMAppUtil.getDBDate(new Date(), DATE_FORMAT_TO_DISPLAY));
                            dateType = FLT_DATE_TIME;
                            addtionalParameterMapNew.put(dateType, tvDateTime.getText().toString());
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

                    ArrayList<TypeActionableFilterModel> typeFilterList = this.getArguments().getParcelableArrayList(SPINNER_LIST);
                    if (typeFilterList != null && !typeFilterList.isEmpty()) {
                        spTypeFilter.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.VISIBLE);
                        ArrayAdapter<TypeActionableFilterModel> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                        spTypeFilter.setAdapter(adapter1);
                        AdapterUtils.setSpinnerData(adapter1, spTypeFilter, typeFilterList);
                        getSpinnerSelection();
                    }
                }
                if (filterList != null && filterList.size() > 0) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter, filterList.get(0).getFilterData());
                    getSpinnerSelection();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isActionable.trim().equalsIgnoreCase(ACTIONABLE)) {
            webServiceCall();
        } else if (isActionable.trim().equalsIgnoreCase(ACTIONABLE)
                && !Objects.requireNonNull(getArguments()).containsKey(
                SPINNER_LIST)
                && !getArguments().containsKey(FILTER_LIST)) {
            webServiceCall();
        }
    }

    private void getSpinnerSelection() {
        disposable.add(
                itemSelections(spFilter)
                        .filter(InvalidSelectionPredicate)
                        .skip(1)
                        .subscribe(xyz -> {
                            filterString = ((FilterRowData) spFilter.getSelectedItem()).getValue()
                                    .replaceAll("_", " ");
                            if (spinnerDataFlag) {
                                isDataPresent();
                            } else {
                                webServiceCall();
                            }
                        }));
        disposable.add(
                itemSelections(spTypeFilter)
                        .filter(InvalidSelectionPredicate)
                        .skip(1)
                        .subscribe(xyz -> {
                            typeFilterString =
                                    spTypeFilter.getSelectedItem().toString().replaceAll("_", " ");
                            if (spinnerDataFlag) {
                                isDataPresent();
                            } else {
                                webServiceCall();
                            }
                        }));

    }

    private void isDataPresent() {
        if (!isBlank(filterString) && !isBlank(typeFilterString)) {
            webServiceCall();
        }
    }


    private void renderDashboardSecondLevel(UiModel<List<Map<String, Object>>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
            showOnFailurePopup(error);
        }

        List<Map<String, Object>> data = uiModel.data;
        if (data != null) {
            try {
                renderTable(data);
            } catch (Exception e) {
                alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void renderTable(List<Map<String, Object>> tableList) {
        hierarchyMapList = new ArrayList<>();
        SecondClickTableAdapter secondClickTableAdapter = new SecondClickTableAdapter(new ArrayList<>());
        table.setAdapter(secondClickTableAdapter);
        if (tableList.size() != 0) {
            ArrayList<String> columns = new ArrayList<>();
            ArrayList<String> columnsHeader = new ArrayList<>();
            for (Map<String, Object> objDetail : tableList) {
                if (columns.size() == 0) {
                    for (Object columnName : objDetail.keySet()) {
                        columns.add(columnName.toString());
                        if (columnName.toString()
                                .equalsIgnoreCase(
                                        "WidgetSystemName")
                                || columnName.toString()
                                .equalsIgnoreCase("flg_empcode")) {
                        } else {
                            columnsHeader.add(
                                    getRefreshColumnName(
                                            columnName.toString()));
                        }
                    }
                    String[] stockArr =
                            new String[columnsHeader.size()];
                    stockArr = columnsHeader.toArray(stockArr);
                    secondClickTableAdapter.setHeaders(
                            stockArr);
                }
                HashMap map = new HashMap();
                LinkedHashMap<String, String> hm =
                        new LinkedHashMap<>();
                for (String column : columns) {
                    if (column.equalsIgnoreCase(
                            "WidgetSystemName") || column
                            .equalsIgnoreCase("flg_empcode")) {
                        map.put("flg_empcode",
                                objDetail.get("flg_empcode"));
                        map.put("WidgetSystemName",
                                objDetail.get("WidgetSystemName"));
                    } else {
                        String columnData =
                                (objDetail.get(column) == null
                                        || objDetail.get(column)
                                        .toString() == null) ? ""
                                        : objDetail.get(column)
                                        .toString();

                        if (objDetail.get(
                                column) instanceof Number) {
                            String strValue =
                                    objDetail.get(column)
                                            .toString()
                                            .trim();
                            if (strValue.indexOf(".") > -1
                                    && !strValue.endsWith(".0")
                                    && !strValue.endsWith(".00")) {
                                columnData =
                                        String.format("%.2f",
                                                Double.parseDouble(
                                                        objDetail.get(
                                                                column)
                                                                .toString()));
                            } else {
                                columnData =
                                        String.format("%.0f",
                                                Double.parseDouble(
                                                        objDetail.get(
                                                                column)
                                                                .toString()));
                            }
                        }
                        hm.put(getRefreshColumnName(column),
                                columnData);
                    }
                }
                secondClickTableAdapter.add(hm);
                if (map.size() != 0) {
                    hierarchyMapList.add(map);
                }
            }
            secondClickTableAdapter.notifyDataSetChanged();
            table.setVisibility(View.VISIBLE);
            tvNochart.setVisibility(View.GONE);
            hideProgress();
        } else {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
        }
    }

    private void webServiceCall() {
        addtionalParameterMap = new HashMap();
        addtionalParameterMap.putAll(addtionalParameterMapNew);
        if (!isBlank(filterString)) {
            addtionalParameterMap.put(COL_FILTER, filterString);
        }
        if (!isBlank(typeFilterString)) {
            addtionalParameterMap.put(ROW_FILTER, typeFilterString);
        }
        if (Objects.requireNonNull(this.getArguments()).getString(LABEL).trim().equalsIgnoreCase(ACTIONABLE)) {
            addtionalParameterMap.put(WIDGETTYPE, typeFilterString);
        }

        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            Map<String, String> parameterMap;
            if (addtionalParameterMap != null && !addtionalParameterMap.isEmpty()) {
                parameterMap = addtionalParameterMap;
            } else {
                parameterMap = null;
            }

            DashboardSecondLevelRequest request = new DashboardSecondLevelRequest(
                    parameterMap, widgetSystemName, DASHBOARD_REPORT_PAGE_SIZE, "1"
            );
            hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
            hierarchyMap.put("PageSize", DASHBOARD_REPORT_PAGE_SIZE);
            hierarchyMap.put("Page", "1");
            Disposable subscribe = getSaathiApi().getDashboardSecondLevel(request)
                    .map(responseS -> {
                        Gson mGson = new Gson();
                        Type type = new TypeToken<HashMap<String, Object>>() {
                        }.getType();
                        HashMap<String, Object> response = mGson.fromJson(responseS, type);
                        Map<String, Object> responseJsonObj =
                                (Map<String, Object>) response.get("Response");
                        List<Map<String, Object>> tableResponseList =
                                (List<Map<String, Object>>) responseJsonObj.get(
                                        "Data");
                        String status = response.get(STATUS).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return tableResponseList;
                        } else {
                            String message = response.get(MESSAGE).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::renderDashboardSecondLevel);
        } else {
            noInternetConnectionDialog();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlDate) {
            selectDate();
        }
    }

    private void selectDate() {
        Calendar today = Calendar.getInstance();

        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker);
        dialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        DatePicker dpFromDate = dialog.findViewById(R.id.datePickerFromDate);
        dpFromDate.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        ImageView close = dialog.findViewById(R.id.ibtnClose);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        final TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        timePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        Calendar cal = Calendar.getInstance();
        cal.add(cal.DAY_OF_MONTH, -3);
        if (mainFromDate != null) {
            try {
                String[] dateSplit = mainFromDate.split("-");
                dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                        Integer.parseInt(dateSplit[2]), (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
            } catch (Exception e) {
                Timber.d(e, TAG);
            }

        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                    today.get(Calendar.DATE),
                    (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
        }

        timePicker.setOnTimeChangedListener((arg0, arg1, arg2) -> {
            // TODO Auto-generated method stub
            hour = arg1;
            minute = arg2;
        });
        close.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            try {
                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");

                if (mainFromDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    fromPostingDate = CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd");
                    if (dateFlag) {
                        tvDateTime.setText(
                                CRMAppUtil.getDateStr(fromDat, NEWDATE_FORMAT_OF_DD_MMM_YYYY));
                        addtionalParameterMapNew.put(dateType, tvDateTime.getText().toString());
                        webServiceCall();
                    } else {
                        String splitEndDate = stringArray(fromPostingDate);
                        String[] newEndDate = splitEndDate.split("-");
                        tvDateTime.setText(CRMAppUtil.getDBDate(
                                CRMAppUtil.getDBDateObj(Integer.parseInt(newEndDate[0]),
                                        Integer.parseInt(newEndDate[1]),
                                        Integer.parseInt(newEndDate[2]), hour,
                                        minute), DATE_FORMAT_TO_DISPLAY));
                        addtionalParameterMapNew.put(dateType, tvDateTime.getText().toString());
                        webServiceCall();
                    }
                }
                dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
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

    private String getRefreshColumnName(String columnName) {
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


    private String stringArray(String date) {
        String[] spaceString = date.split(" ");

        return spaceString[0];
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

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final CommonHeader[] commonHeaders;
        ArrayList<HashMap<String, String>> hashArray;
        private String[] headers = {};
        private LayoutInflater inflater = null;

        SecondClickTableAdapter(
                ArrayList<HashMap<String, String>> hashArray) {
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
            return headers.length;
        }

        @Override
        public View getView(final int row, int column, View convertView, ViewGroup parent) {
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

                    view.setOnClickListener(v -> {
                        if (!isBlank(
                                getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode",
                                        "LEADID/CLIENTCODE"))) {
                            leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                    "LeadId ClientCode",
                                    "LEADID/CLIENTCODE");
                        } else {
                            leadId = "0";
                        }
                        if (!isBlank(
                                getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid"))) {
                            ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                    "Client ucid");
                        } else {
                            ucid = "0";
                        }

                        if (hierarchyMapList.size() >= 1) {
                            if (!isBlank(getValue(row, "RM Name"))) {
                                addtionalParameterMap.put("flt_level",
                                        getValue(row, "Hierarchy"));
                                addtionalParameterMap.put("flt_Hierarchy_empCode",
                                        hierarchyMapList.get(row).get("flg_empcode"));
                                hierarchyMap.put("WidgetSystemName",
                                        hierarchyMapList.get(row)
                                                .get("WidgetSystemName"));
                                hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(HIERARCHY_MAP,
                                        (Serializable) hierarchyMap);
                                bundle.putSerializable(ADDITIONAL_MAP,
                                        (Serializable) addtionalParameterMap);
                                bundle.putString(LABEL, txtLables.getText().toString());
                                navigator.openPage(
                                        NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT,
                                        bundle, true, false);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(LEAD_ID, leadId);
                        bundle.putString(UCID, ucid);
                        if (!leadId.equalsIgnoreCase("0") || !ucid.equalsIgnoreCase("0")) {
                            //navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                            //    true);
                            navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                                    false);
                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(v -> {
                        if (!isBlank(
                                getValue(row, "LEAD ID", "LeadId", "Lead_Id", "LeadId ClientCode",
                                        "LEADID/CLIENTCODE"))) {
                            leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                    "LeadId ClientCode",
                                    "LEADID/CLIENTCODE");
                        } else {
                            leadId = "0";
                        }
                        if (!isBlank(
                                getValue(row, UCID, "Client UCID", "CLIENT UCID", "Client ucid"))) {
                            ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                    "Client ucid");
                        } else {
                            ucid = "0";
                        }
                        if (hierarchyMapList.size() >= 1) {
                            if (!isBlank(getValue(row, "RM Name"))) {
                                addtionalParameterMap.put("flt_level",
                                        getValue(row, "Hierarchy"));
                                addtionalParameterMap.put("flt_Hierarchy_empCode",
                                        hierarchyMapList.get(row).get("flg_empcode").toString());
                                hierarchyMap.put("WidgetSystemName",
                                        hierarchyMapList.get(row)
                                                .get("WidgetSystemName"));
                                hierarchyMap.put("AddtionalParameters", addtionalParameterMap);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(HIERARCHY_MAP,
                                        (Serializable) hierarchyMap);
                                bundle.putSerializable(ADDITIONAL_MAP,
                                        (Serializable) addtionalParameterMap);
                                bundle.putString(LABEL, txtLables.getText().toString());
                                navigator.openPage(
                                        NavigationConstants.NAVIGATE_TO_SECOND_LEVEL_HIERARCHY_REPORT,
                                        bundle, true, false);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString(LEAD_ID, leadId);
                        bundle.putString(UCID, ucid);
                        if (!leadId.equalsIgnoreCase("0") || !ucid.equalsIgnoreCase("0")) {
                            //navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                            //    true);
                            navigator.openPage(NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, true,
                                    false);
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
        private View getFirstHeader(View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
            }
            RelativeLayout relativeLayout =
                    convertView.findViewById(R.id.relativeLayout);
            TextView head = (convertView.findViewById(android.R.id.text1));
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                tvNochart.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }

            return convertView;
        }

        private View getHeader(int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = (convertView.findViewById(android.R.id.text1));
            head.setText(headers[column]);

            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first, parent, false);
            }
            LinearLayout listLayout =
                    convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(
                    row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first =
                    convertView.findViewById(R.id.tv_table_first);
            tv_table_first.setText(getDevice(row).data[column + 1]);

            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = convertView.findViewById(R.id.item_table);
            listLayout.setBackgroundResource(
                    row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                    getDevice(row).data[column]);
            TextView text1 = convertView.findViewById(android.R.id.text1);
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