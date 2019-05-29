package com.mosl.tabapp.ui;

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
import com.example.saathiapi.model.reports.report.AssignedLeadReportRequest;
import com.example.saathiapi.model.reports.report.ResponseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import cz.msebera.android.httpclient.Header;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by Bhavesh on 09-10-2017.
 */

public class ReportFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View reportView;
    private TableFixHeaders table;
    private SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    private boolean dataFoundFlag;
    String clickedOn = "";
    int click = 1;
    private String widgetSystemName;
    private TextView tvStartDate, tvEndDate;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate;
    private Dialog dialog;
    private Calendar today;
    private ImageView close;
    private Button btnSubmit;
    private DatePicker dpFromDate, dpToDate;
    private Date fromDat, toDate;
    private RelativeLayout rlEndDate, rlStartDate;
    private Spinner spBranch, spChannel;
    private ArrayList<RowData> arrSelectChannel, arrSelectBranch;
    private ArrayAdapter<RowData> arrAdapSelectChannel, arrAdapSelectBranch;
    private String channelId, branchCode;
    private String leadId = "0", ucid = "0", leadName;

    private AdapterView.OnItemSelectedListener selectBranchListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    branchCode = ((RowData) spBranch.getSelectedItem()).getValue();
                    if (branchCode.equalsIgnoreCase("Select")) {
                        branchCode = "";
                    } else {
                        branchCode = ((RowData) spBranch.getSelectedItem()).getValue();
                    }
                    isDataPresent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    private void setUpView(View reportView) {
        widgetSystemName = this.getArguments().getString(MOSLConstants.SYSTEM_NAME);

        tvNochart = (TextView) reportView.findViewById(R.id.tvNochart);
        txtLables = (TextView) reportView.findViewById(R.id.txtLables);
        table = (TableFixHeaders) reportView.findViewById(R.id.table);
        tvStartDate = (TextView) reportView.findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) reportView.findViewById(R.id.tvEndDate);
        rlEndDate = (RelativeLayout) reportView.findViewById(R.id.rlEndDate);
        rlStartDate = (RelativeLayout) reportView.findViewById(R.id.rlStartDate);

        txtLables.setText(this.getArguments().getString(MOSLConstants.NAME));
        spBranch = (Spinner) reportView.findViewById(R.id.spBranch);
        spChannel = (Spinner) reportView.findViewById(R.id.spChannel);
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
        tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY));
        rlEndDate.setOnClickListener(this);
        tvEndDate.setText(CRMAppUtil.getDateStr(toDate, DATE_FORMAT_OF_DD_MMM_YYYY));
        rlStartDate.setOnClickListener(this);
        selectBranch();
    }

    private AdapterView.OnItemSelectedListener selectChannelListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    channelId = ((RowData) spChannel.getSelectedItem()).getKey();
                    selectBranch();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        reportView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_report);

        setUpView(reportView);
        hideAppTitle();
        setUserVisibleHint(true);

        return reportView;
    }

    private void selectBranch() {
        showProgress();
        AssignedLeadReportRequest request = new AssignedLeadReportRequest("-1");
        Disposable subscribe = CRMWebServiceHandler.getApiInstance(getMainActivity())
            .postAssignedLeadReport(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
                if (response.isSuccess()) {
                    List<ResponseItem> list = response.getResponse();
                    for (int i = 0; i < list.size(); i++) {
                        ResponseItem responseItem = list.get(i);
                        String businessUnitId = String.valueOf(responseItem.getBusinessUnitId());
                        String branchCode = responseItem.getBranchCode();
                        arrSelectBranch.add(
                            new RowData(i + 1, businessUnitId, branchCode));
                    }
                    arrAdapSelectBranch = new ArrayAdapter<>(getMainActivity(),
                        R.layout.simple_spinner_item, arrSelectBranch);
                    arrAdapSelectBranch.setDropDownViewResource(
                        R.layout.mdropdown);
                    spBranch.setAdapter(arrAdapSelectBranch);
                    spBranch.setOnItemSelectedListener(selectBranchListener);
                    arrAdapSelectBranch.notifyDataSetChanged();
                    arrAdapSelectBranch.setNotifyOnChange(true);
                } else {
                    hideProgress();
                    MSFDialog.alertDialog(getMainActivity(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        response.getMessage(), "OK", true, null);
                }
            });
        /*if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            Map inputMap = new HashMap<>();
            inputMap.put("ChannelIds", "-1");
            final String serviceMethod = "UserApi/GetUserBusinessProfileBranchByHierarchy";
            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            arrSelectBranch = new ArrayList<>();
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    if (status.equalsIgnoreCase("true")) {
                                        arrSelectBranch.add(new RowData(0, "", "Select"));
                                        JSONArray jsonArray = response.getJSONArray(RESPONSE);

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject objDetail = jsonArray.getJSONObject(i);
                                            String lookUpId = (objDetail.isNull("BusinessUnitId")
                                                || objDetail.getString("BusinessUnitId").equals(""))
                                                ? "" : objDetail.getString("BusinessUnitId");
                                            String lookUpName = (objDetail.isNull("BranchCode")
                                                || objDetail.getString("BranchCode").equals(""))
                                                ? "" : objDetail.getString("BranchCode");
                                            arrSelectBranch.add(
                                                new RowData(i + 1, lookUpId, lookUpName));
                                        }
                                        arrAdapSelectBranch = new ArrayAdapter<>(getMainActivity(),
                                            R.layout.simple_spinner_item, arrSelectBranch);
                                        arrAdapSelectBranch.setDropDownViewResource(
                                            R.layout.mdropdown);
                                        spBranch.setAdapter(arrAdapSelectBranch);
                                        spBranch.setOnItemSelectedListener(selectBranchListener);
                                        arrAdapSelectBranch.notifyDataSetChanged();
                                        arrAdapSelectBranch.setNotifyOnChange(true);
                                    } else {
                                        hideProgress();
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(getMainActivity(), 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            errorMsg, "OK", true, null);
                                    }
                                } else {
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            hideProgress();
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            hideProgress();
                        }

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            hideProgress();
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            try {
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }

                        @Override public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
            return;
        }*/
    }

    private void getReport() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            String serviceMethod = "Report/LoadReportViewDataAsync";

            showProgress();
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(),
                    getRespBody(AccountDetails.getEmployeeCode()), new TextHttpResponseHandler() {

                        @Override public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseS) {
                            try {
                                if (statusCode == 200) {
                                    Gson mGson = new Gson();
                                    Type type = new TypeToken<HashMap<String, Object>>() {
                                    }.getType();
                                    HashMap<String, Object> response =
                                        mGson.fromJson(responseS, type);
                                    if (response.size() != 0) {
                                        String status = response.get(STATUS).toString();
                                        if (status.equalsIgnoreCase("true")) {
                                            hideProgress();
                                            secondClickTableAdapter =
                                                new SecondClickTableAdapter(getActivity(),
                                                    new ArrayList<HashMap<String, String>>());
                                            table.setAdapter(secondClickTableAdapter);
                                            Map<String, Object> responseJSONObject =
                                                (Map<String, Object>) response.get("Response");
                                            List<Map<String, Object>> arrayDetail =
                                                (List<Map<String, Object>>) responseJSONObject.get(
                                                    "Data");
                                            ArrayList<String> columns = new ArrayList<String>();
                                            ArrayList<String> columnsHeader =
                                                new ArrayList<String>();

                                            if (arrayDetail.size() != 0) {
                                                for (int y = 0; y < arrayDetail.size(); y++) {
                                                    Map<String, Object> objDetail =
                                                        arrayDetail.get(y);

                                                    if (columns.size() == 0) {
                                                        for (Object columnName : objDetail.keySet()) {
                                                            columns.add(columnName.toString());
                                                            if (columnName.toString()
                                                                .equalsIgnoreCase("lead_id")) {
                                                                columnName = columnName.toString()
                                                                    .toUpperCase();
                                                            }
                                                            columnsHeader.add(getRefreshColumnName(
                                                                columnName.toString()));
                                                        }
                                                        String[] stockArr =
                                                            new String[columnsHeader.size()];
                                                        stockArr = columnsHeader.toArray(stockArr);
                                                        secondClickTableAdapter.setHeaders(
                                                            stockArr);
                                                    }
                                                    LinkedHashMap<String, String> hm =
                                                        new LinkedHashMap<>();
                                                    for (String column : columns) {
                                                        String columnData =
                                                            (objDetail.get(column) == null
                                                                || objDetail.get(column).toString()
                                                                == null) ? ""
                                                                : objDetail.get(column).toString();

                                                        if (objDetail.get(
                                                            column) instanceof Number) {
                                                            String strValue = objDetail.get(column)
                                                                .toString()
                                                                .trim();

                                                            if (strValue.indexOf(".") > -1
                                                                && !strValue.endsWith(".0")
                                                                && !strValue.endsWith(".00")) {
                                                                columnData = String.format("%.2f",
                                                                    Double.parseDouble(
                                                                        objDetail.get(column)
                                                                            .toString()));
                                                            } else {
                                                                columnData = String.format("%.0f",
                                                                    Double.parseDouble(
                                                                        objDetail.get(column)
                                                                            .toString()));
                                                            }
                                                        }
                                                        if (column.equalsIgnoreCase("lead_id")) {
                                                            column = column.toUpperCase();
                                                        }

                                                        hm.put(
                                                            CRMAppUtil.getRefreshColumnName(column),
                                                            columnData.trim());
                                                    }
                                                    secondClickTableAdapter.add(hm);
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
                                            String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(
                                                new JSONObject(responseS));
                                            table.setVisibility(View.GONE);
                                            dataFoundFlag = false;
                                            tvNochart.setVisibility(View.VISIBLE);
                                            alertDialog(getMainActivity(), 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
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

                        @Override public void onFailure(int statusCode, Header[] headers,
                            String responseString, Throwable throwable) {
                            try {
                                JSONObject errorResponse = new JSONObject(responseString);
                                hideProgress();
                                if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        UNAUTHORIZE_USER, "OK", false,
                                        new MSFDialog.DialogListener() {
                                            @Override
                                            public void alertDialogAction(MSFDialog.Action action,
                                                Object... data) {
                                                if (action == MSFDialog.Action.OK) {
                                                    Intent i = new Intent(getMainActivity(),
                                                        LoginActivity.class);
                                                    startActivity(i);
                                                    getActivity().finish();
                                                }
                                            }
                                        });
                                    return;
                                } else {
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    MSFDialog.alertDialog(getMainActivity(), 0,
                                        getResources().getString(R.string.motilal_oswal), error,
                                        "OK", true, null);
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
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
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

    @Override public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlStartDate) {
            selectDate();
        } else if (id == R.id.rlEndDate) {
            selectDate();
        }
    }

    public void selectDate() {
        today = Calendar.getInstance();
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker_from_to);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        dpFromDate = (DatePicker) dialog.findViewById(R.id.datePickerFromDate);
        dpToDate = (DatePicker) dialog.findViewById(R.id.datePickerToDate);

        dpToDate.setMaxDate(System.currentTimeMillis() - 1000);
        dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                    @Override public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    }
                });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), Calendar.APRIL, today.getFirstDayOfWeek(),
                new DatePicker.OnDateChangedListener() {
                    @Override public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                        mainFromDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    }
                });
        }

        if (mainFromDate != null) {
            String[] dateSplit = mainToDate.split("-");
            dpToDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                    @Override public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {

                        mainToDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    }
                });
        } else {
            dpToDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override public void onDateChanged(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {

                        today.set(year, monthOfYear, dayOfMonth);
                        long dayy = today.getActualMaximum(Calendar.DATE);
                        mainToDate =
                            year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                    }
                });
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                if (fromDat.after(toDate)) {
                    MSFDialog.alertDialog(getMainActivity(), 0, "Motilal Oswal", INVALID_DATE_RANGE,
                        "OK", true, null);
                } else {
                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate =
                            CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY);
                        tvStartDate.setText(fromPostingDate);
                    }
                    if (mainToDate != null) {
                        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                        toPostingDate = CRMAppUtil.getDateStr(toDate, DATE_FORMAT_OF_DD_MMM_YYYY);
                        tvEndDate.setText(toPostingDate);
                    }
                    getReport();

                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final CommonHeader commonHeaders[];
        private String headers[] = {};
        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public SecondClickTableAdapter(Context context,
            ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[] { new CommonHeader("asd") };
            this.hashArray = hashArray;
            density = context.getResources().getDisplayMetrics().density;
        }

        public String[] getHeaders() {
            return headers;
        }

        public void setHeaders(String[] headers) {
            this.headers = headers;
        }

        @Override public int getRowCount() {
            return commonHeaders[0].list.size();
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

        @Override public int getColumnCount() {
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
                        @Override public void onClick(View view) {
                            if (!isBlank(getValue(row, "EMPLOYEE CODE", "EMPLOYEE_CODE"))) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(SecondLevelReportFragment.STRING_MAP,
                                    getRespBody(getValue(row, "EMPLOYEE CODE")));
                                String nextTitle = ReportFragment.this.getArguments()
                                    .getString(MOSLConstants.NAME);
                                bundle.putString(SecondLevelReportFragment.STRING_TITLE, nextTitle);
                                navigateTo(NAV_TO_SECOND_LEVEL_REPORT, bundle, getFragmentManager(),
                                    true);
                            } else {

                                if (!isBlank(getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                    "LeadId ClientCode", "LEADID/CLIENTCODE"))) {
                                    leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                        "LeadId ClientCode", "LEADID/CLIENTCODE");
                                } else {
                                    leadId = "0";
                                }
                                if (!isBlank(getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                    "Client ucid"))) {
                                    ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                        "Client ucid");
                                } else {
                                    ucid = "0";
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString(LEAD_ID, leadId);
                                bundle.putString(UCID, ucid);
                                navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                                    true);
                            }
                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {
                            if (!isBlank(getValue(row, "EMPLOYEE CODE", "EMPLOYEE_CODE"))) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(SecondLevelReportFragment.STRING_MAP,
                                    getRespBody(getValue(row, "EMPLOYEE CODE")));
                                String nextTitle = ReportFragment.this.getArguments()
                                    .getString(MOSLConstants.NAME);
                                bundle.putString(SecondLevelReportFragment.STRING_TITLE, nextTitle);
                                navigateTo(NAV_TO_SECOND_LEVEL_REPORT, bundle, getFragmentManager(),
                                    true);
                            } else {

                                if (!isBlank(getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                    "LeadId ClientCode", "LEADID/CLIENTCODE"))) {
                                    leadId = getValue(row, "LEAD ID", "LeadId", "Lead_Id",
                                        "LeadId ClientCode", "LEADID/CLIENTCODE");
                                } else {
                                    leadId = "0";
                                }
                                if (!isBlank(getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                    "Client ucid"))) {
                                    ucid = getValue(row, UCID, "Client UCID", "CLIENT UCID",
                                        "Client ucid");
                                } else {
                                    ucid = "0";
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString(LEAD_ID, leadId);
                                bundle.putString(UCID, ucid);
                                navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                                    true);
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
            RelativeLayout relativeLayout =
                (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
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
            LinearLayout listLayout =
                (LinearLayout) convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(
                row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first =
                (TextView) convertView.findViewById(R.id.tv_table_first);
            tv_table_first.setText(getDevice(row).data[column + 1]);

            return convertView;
        }

        private View getBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table, parent, false);
            }
            LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.item_table);
            listLayout.setBackgroundResource(
                row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            ((TextView) convertView.findViewById(android.R.id.text1)).setText(
                getDevice(row).data[column]);
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

        @Override public int getWidth(int column) {
            int widthSize = 0;
            if (column == -1) {
                widthSize = getResources().getDimensionPixelSize(R.dimen._90sdp);
            }
            for (int i = 0; i < column; i++) {
                widthSize = getResources().getDimensionPixelSize(R.dimen._135sdp);
            }
            return widthSize;
        }

        @Override public int getHeight(int row) {
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

        @Override public int getItemViewType(int row, int column) {
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

        @Override public int getViewTypeCount() {
            return 5;
        }
    }

    public String prefix(int input) {
        if (Integer.toString(input).length() == 1) {
            return "0" + input;
        }
        return input + "";
    }

    private void isDataPresent() {
        if (branchCode != null) {
            getReport();
        }
    }

    //EMPLOYEE CODE
    private HashMap getRespBody(String empCode) {
        HashMap inputMap = new HashMap<>();
        inputMap.put("ReportTypeSystemName", widgetSystemName);
        inputMap.put("EmployeeCode", empCode);
        inputMap.put("BusinessChannel", "-1");
        inputMap.put("BranchCode", branchCode);
        inputMap.put("Period_From", CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd"));
        inputMap.put("Period_To", CRMAppUtil.getDateStr(toDate, "yyyy-MM-dd"));
        inputMap.put("IsExportToExcel", false);
        inputMap.put("InputTotalCount", 0);
        inputMap.put("PageSize", REPORT_PAGE_SIZE);
        inputMap.put("Page", "0");
        inputMap.put("CalculatedPage", "0");
        inputMap.put("TotalCount", "0");
        return inputMap;
    }
}
