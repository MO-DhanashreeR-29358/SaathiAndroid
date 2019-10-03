package com.mosl.tabapp.pendingApproval;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.pendingforapproval.ApprovalFieldItem;
import com.mosl.saathi.api.model.pendingforapproval.ApprovalListRequest;
import com.mosl.saathi.api.model.pendingforapproval.ApprovalUserItem;
import com.mosl.saathi.api.model.pendingforapproval.ApprovelRequest;
import com.mosl.saathi.api.model.pendingforapproval.MultiUpdateApprovalRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.RowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.ui.MSFDialog;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.RowData.getSpinnerRowDataById;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class PendingForApprovalFragment extends CommonFragment
    implements View.OnClickListener, MOSLConstants {

    private View pendingForApprovalView;
    private Spinner spProfileChange, spSelectEmployee;
    private TableFixHeaders table;
    private PendingApprovalTableAdapter pendingApprovalTableAdapter;
    private TextView tvNochart, tvStartDate, tvEndDate;
    private LinkedHashMap lMap;
    private String clickedOn = "";
    private int click = 1;
    private boolean isAllChecked = false;
    private boolean isSingleAllChecked = false;
    private CheckBox cbClientName;
    private ArrayList<String> selectedRowList = new ArrayList<>();
    //will be used to hold the clientid of selected rows by clicking checkboxes
    private Map<Integer, Boolean> selectedRowMap = new HashMap<Integer, Boolean>();
    private String employeeCodeStr, profileCodeStr;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate, brFromDate, brToDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private LinearLayout llButton;
    private AdapterView.OnItemSelectedListener selectEmployeeListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    employeeCodeStr = ((RowData) spSelectEmployee.getSelectedItem()).getKey();
                    isDataPresent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    public static PendingForApprovalFragment newInstance(Context context) {
        PendingForApprovalFragment fragment = new PendingForApprovalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View pendingForApprovalView) {

        spProfileChange = pendingForApprovalView.findViewById(R.id.spProfileChange);
        spSelectEmployee = pendingForApprovalView.findViewById(R.id.spSelectEmployee);
        Button btnApprove = pendingForApprovalView.findViewById(R.id.btnApprove);
        Button btnReject = pendingForApprovalView.findViewById(R.id.btnReject);
        table = pendingForApprovalView.findViewById(R.id.table);

        tvStartDate = pendingForApprovalView.findViewById(R.id.tvStartDate);
        tvEndDate = pendingForApprovalView.findViewById(R.id.tvEndDate);
        LinearLayout llStartDate =
            pendingForApprovalView.findViewById(R.id.llStartDate);
        LinearLayout llEndDate = pendingForApprovalView.findViewById(R.id.llEndDate);
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
        tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY));
        llStartDate.setOnClickListener(this);
        tvEndDate.setText(CRMAppUtil.getDateStr(toDate, DATE_FORMAT_OF_DD_MMM_YYYY));
        llEndDate.setOnClickListener(this);
        btnApprove.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        tvNochart = pendingForApprovalView.findViewById(R.id.tvNochart);
        llButton = pendingForApprovalView.findViewById(R.id.llButton);
    }

    private AdapterView.OnItemSelectedListener selectProfileListener =
        new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    profileCodeStr = ((RowData) spProfileChange.getSelectedItem()).getKey();
                    isDataPresent();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        pendingForApprovalView = super.onCreateView(inflater, container, savedInstanceState);
		container.post(() -> setAppTitle("Pending For Approvals"));
        setUpView(pendingForApprovalView);
        setUserVisibleHint(true);
        return pendingForApprovalView;
    }

    @Override protected int getLayout() {
        return R.layout.frag_pending_for_approval;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnApprove:

                if (getSelectedRowCount() > 0 || cbClientName.isChecked()) {
                    MSFDialog.alertDialog(requireContext(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Are you sure, you want to approve selected request?", "Yes", "No", true,
                        (action, data) -> {
                            if (action == MSFDialog.Action.OK) {
                                sendUpdateAppoovalWebService("2");
                            }
                        });
                } else {
                    MSFDialog.alertDialog(requireContext(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Please select at least one record", "OK", true, null);
                }
                break;
            case R.id.btnReject:

                if (getSelectedRowCount() > 0 || cbClientName.isChecked()) {
                    MSFDialog.alertDialog(requireContext(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Are you sure, you want to reject selected request?", "Yes", "No", true,
                        (action, data) -> {
                            if (action == MSFDialog.Action.OK) {
                                sendUpdateAppoovalWebService("3");
                            }
                        });
                } else {
                    MSFDialog.alertDialog(requireContext(), 0,
                        getResources().getString(R.string.motilal_oswal),
                        "Please select at least one record", "OK", true, null);
                }
                break;
            case R.id.llStartDate:
                selectDate();
                break;
            case R.id.llEndDate:
                selectDate();
                break;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && pendingForApprovalView != null) {
                getApprovelReqestUserList();
                getApprovelReqestFieldList(
                    Objects.requireNonNull(this.getArguments()).getString(ENTITY_FIELD));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectDate() {
        today = Calendar.getInstance();
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow())
            .setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.date_picker_from_to);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        DatePicker dpFromDate = dialog.findViewById(R.id.datePickerFromDate);
        DatePicker dpToDate = dialog.findViewById(R.id.datePickerToDate);
        ImageView close = dialog.findViewById(R.id.ibtnClose);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);

        dpToDate.setMaxDate(System.currentTimeMillis() - 1000);
        dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);

        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                Integer.parseInt(dateSplit[2]),
                (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
                    year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.getFirstDayOfWeek(), (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
                    year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
        }

        if (mainFromDate != null) {
            String[] dateSplit = mainToDate.split("-");
            dpToDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                Integer.parseInt(dateSplit[2]),
                (view, year, monthOfYear, dayOfMonth) -> mainToDate =
                    year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
        } else {
            dpToDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH), (view, year, monthOfYear, dayOfMonth) -> {

                    today.set(year, monthOfYear, dayOfMonth);
                    long dayy = today.getActualMaximum(Calendar.DATE);
                    mainToDate =
                        year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                });
        }

        close.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
            toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
            if (fromDat.after(toDate)) {
                MSFDialog.alertDialog(requireContext(), 0,
                    getResources().getString(R.string.motilal_oswal), INVALID_DATE_RANGE, "OK",
                    true, null);
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
                RowData subEmployeeData = (RowData) spSelectEmployee.getSelectedItem();
                RowData subProfileData = (RowData) spProfileChange.getSelectedItem();
                webServiceCall(subEmployeeData.getKey(), subProfileData.getKey());
                dialog.dismiss();
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

    private void renderApprovalField(UiModel<List<ApprovalFieldItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;

        if (error != null) {
            showOnFailurePopup(error);
        }

        List<ApprovalFieldItem> dataList = uiModel.data;
        if (dataList != null) {
            ArrayList<RowData> arrProfile = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                ApprovalFieldItem item = dataList.get(i);
                String lookUpId = stringOrEmpty(item.entityField);
                String lookUpName = stringOrEmpty(item.referenceName);
                arrProfile.add(new RowData(i, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapProfile =
                new ArrayAdapter<>(requireContext(), R.layout.simple_spinner_item, arrProfile);
            arrAdapProfile.setDropDownViewResource(R.layout.mdropdown);
            spProfileChange.setAdapter(arrAdapProfile);
            spProfileChange.setOnItemSelectedListener(selectProfileListener);
            arrAdapProfile.notifyDataSetChanged();

            String entityField =
                Objects.requireNonNull(this.getArguments()).getString(ENTITY_FIELD);

            RowData entityFieldID = getSpinnerRowDataById(entityField, arrAdapProfile);
            if (entityFieldID != null) {
                spProfileChange.setSelection(entityFieldID.getPosition());
            }
        }
    }

    private void getApprovelReqestFieldList(final String entityField) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            ApprovelRequest request =
				new ApprovelRequest(Objects.requireNonNull(userManager.getUser()).employeeCode);
            Disposable subscribe = getSaathiApi().getApprovalFieldList(request)
                .compose(applyUiModel())
                .subscribe(this::renderApprovalField);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderApprovalUser(UiModel<List<ApprovalUserItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;

        if (error != null) {
            showOnFailurePopup(error);
        }

        List<ApprovalUserItem> dataList = uiModel.data;
        if (dataList != null) {
            ArrayList<RowData> arrEmployee = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                ApprovalUserItem item = dataList.get(i);
                String lookUpId = stringOrEmpty(item.updatedBy);
                String lookUpName = stringOrEmpty(item.name);
                arrEmployee.add(new RowData(i, lookUpId, lookUpName));
            }
            ArrayAdapter<RowData> arrAdapEmployee = new ArrayAdapter<>(requireContext(),
                R.layout.simple_spinner_item, arrEmployee);
            arrAdapEmployee.setDropDownViewResource(R.layout.mdropdown);
            spSelectEmployee.setAdapter(arrAdapEmployee);
            spSelectEmployee.setOnItemSelectedListener(
                selectEmployeeListener);
            arrAdapEmployee.notifyDataSetChanged();
        }
    }

    private void getApprovelReqestUserList() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            ApprovelRequest request =
				new ApprovelRequest(Objects.requireNonNull(userManager.getUser()).employeeCode);
            Disposable subscribe = getSaathiApi().getApprovalUserList(request)
                .compose(applyUiModel())
                .subscribe(this::renderApprovalUser);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderApprovalRequest(UiModel<JSONArray> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.GONE);
            showOnFailurePopup(error);
        }

        JSONArray data = uiModel.data;
        if (data != null) {
            try {
                renderTable(data);
            } catch (Exception e) {
                alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void renderTable(JSONArray arrayDetail) throws JSONException {
        pendingApprovalTableAdapter =
            new PendingApprovalTableAdapter(requireActivity(),
                new ArrayList<HashMap<String, String>>());
        table.setAdapter(pendingApprovalTableAdapter);
        selectedRowList.clear();
        selectedRowMap.clear();
        isAllChecked = false;
        isSingleAllChecked = false;

        String Status = "";
        if (arrayDetail.length() != 0) {
            for (int y = 0; y < arrayDetail.length(); y++) {
                JSONObject objDetail =
                    (JSONObject) arrayDetail.get(y);
                String approvalRequestId =
                    (objDetail.isNull("ApprovalRequestId")
                        || objDetail.getString("ApprovalRequestId")
                        .equals("")) ? "0"
                        : objDetail.getString("ApprovalRequestId");
                String employeeCode = (objDetail.isNull("UpdatedBy")
                    || objDetail.getString("UpdatedBy").equals(""))
                    ? "0" : objDetail.getString("UpdatedBy");
                String leadId = (objDetail.isNull("LeadID")
                    || objDetail.getString("LeadID").equals(""))
                    ? "0" : objDetail.getString("LeadID");
                String FieldName =
                    (objDetail.isNull("ReferenceName") || objDetail.getString("ReferenceName")
                        .equals("")) ? ""
                        : objDetail.getString("ReferenceName");
                String empName =
                    (objDetail.isNull("EmployeeName") || objDetail.getString("EmployeeName")
                        .equals("")) ? ""
                        : objDetail.getString("EmployeeName");
                String subOption =
                    (objDetail.isNull("ApprovalFieldValue")
                        || objDetail.getString("ApprovalFieldValue")
                        .equals("")) ? ""
                        : objDetail.getString("ApprovalFieldValue");
                String ucid = (objDetail.isNull("UCID")
                    || objDetail.getString("UCID").equals("")) ? "0"
                    : objDetail.getString("UCID");
                String clientName = (objDetail.isNull("ClientName")
                    || objDetail.getString("ClientName").equals(""))
                    ? "" : objDetail.getString("ClientName");
                String clientCode = (objDetail.isNull("ClientCode")
                    || objDetail.getString("ClientCode").equals(""))
                    ? "0" : objDetail.getString("ClientCode");
                String moduleName = (objDetail.isNull("Entity")
                    || objDetail.getString("Entity").equals(""))
                    ? "0" : objDetail.getString("Entity");
                String FinalStatus =
                    (objDetail.isNull("FinalStatus")
                        || objDetail.getString("FinalStatus")
                        .equals("")) ? "0"
                        : objDetail.getString("FinalStatus");
                String requestDate =
                    (objDetail.isNull("UpdationDate")
                        || objDetail.getString("UpdationDate")
                        .equals("")) ? ""
                        : objDetail.getString("UpdationDate");
                String approvedDate =
                    (objDetail.isNull("Approver2StatusUpdateDate")
                        || objDetail.getString(
                        "Approver2StatusUpdateDate").equals(""))
                        ? "" : objDetail.getString(
                        "Approver2StatusUpdateDate");
                if (FinalStatus.equalsIgnoreCase("1")) {
                    Status = "Pending";
                } else if (FinalStatus.equalsIgnoreCase("2")) {
                    Status = "Approve";
                } else if (FinalStatus.equalsIgnoreCase("3")) {
                    Status = "Reject";
                }

                requestDate =
                    CRMAppUtil.getDateFromString(requestDate,
                        DATE_FORMAT_TO_SERVER_MEETING,
                        CAL_DATE_FORMAT);
                approvedDate =
                    CRMAppUtil.getDateFromString(approvedDate,
                        DATE_FORMAT_TO_SERVER_MEETING,
                        CAL_DATE_FORMAT);

                LinkedHashMap<String, String> hm =
                    new LinkedHashMap<>();
                hm.put(EMPLOYEE_CODE_APP, employeeCode);
                hm.put(EMP_NAME, empName);
                hm.put(LEAD_ID_APP, leadId);
                hm.put(CLIENT_CODE, clientCode);
                hm.put(UCID, ucid);
                hm.put(NEW_CLIENT_NAME, clientName);
                hm.put(MODULE_NAME, moduleName);
                hm.put(FIELD_NAME, FieldName);
                hm.put(SUB_OPTION, subOption);
                hm.put(STATUS, Status);
                hm.put(REQUEST_DATE, requestDate);
                hm.put(APPROVED_DATE, approvedDate);
                hm.put(APPROVAL_REQUEST_ID, approvalRequestId);

                pendingApprovalTableAdapter.add(hm);
            }

            pendingApprovalTableAdapter.notifyDataSetChanged();
            table.setVisibility(View.VISIBLE);
            tvNochart.setVisibility(View.GONE);
            llButton.setVisibility(View.VISIBLE);
        } else {
            table.setVisibility(View.GONE);
            llButton.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
        }
    }

    private void webServiceCall(String emoloyeeStr, String profileStr) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String updatedStartDate = tvStartDate.getText().toString() + " 00:00:00";
            String updatedEndDate = tvEndDate.getText().toString() + " 00:00:00";

            ApprovalListRequest request =
                new ApprovalListRequest(emoloyeeStr, "1", REPORT_PAGE_SIZE,
					Objects.requireNonNull(userManager.getUser()).employeeCode, "0",
					updatedStartDate, updatedEndDate,
                    profileStr);
            Disposable subscribe = getSaathiApi().getApprovalRequestList(request)
                .map(responseS -> {
                    JSONObject response = new JSONObject(responseS);
                    boolean status = response.getBoolean(STATUS);
                    if (status) {
                        return response.getJSONObject(RESPONSE).getJSONArray(DATA);
                    } else {
                        String message = response.get(MESSAGE).toString();
                        throw new ServiceFailureException(message, message);
                    }
                })
                .compose(applyUiModel())
                .subscribe(this::renderApprovalRequest);
        } else {
            noInternetConnectionDialog();
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

    private void renderMultiUpdate(UiModel<String> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        String data = uiModel.data;

        if (data != null) {
            alertDialog(requireContext(), 0,
                getResources().getString(R.string.motilal_oswal),
                data, "OK", true, (action, data1) -> {
                    if (action == MSFDialog.Action.OK) {
                        webServiceCall(employeeCodeStr,
                            profileCodeStr);
                    }
                });
        }
    }

    private void sendUpdateAppoovalWebService(String status) {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            StringBuilder approvalRequestId = new StringBuilder();
            for (String approvalId : selectedRowList) {
                if (approvalRequestId.toString().equals("")) {
                    approvalRequestId = new StringBuilder(approvalId);
                } else {
                    approvalRequestId.append(",").append(approvalId);
                }
            }

            MultiUpdateApprovalRequest request =
                new MultiUpdateApprovalRequest(status, approvalRequestId.toString());
            Disposable subscribe = getSaathiApi().postMultiUpdateApproval(request)
                .compose(applyUiModel())
                .subscribe(this::renderMultiUpdate);
        } else {
            noInternetConnectionDialog();
        }
    }



    private class Item {
        private final String[] data;

        private Item(String employeeCode, String empName, String leadID, String clientCode,
            String ucid, String clientName,
            String moduleName, String fieldName, String subOption, String status,
            String requestDate, String approvedDate, String appovalRequestId) {
            data = new String[] {
                employeeCode,
                empName,
                leadID,
                clientCode,
                ucid,
                clientName,
                moduleName,
                fieldName,
                subOption,
                status,
                requestDate,
                approvedDate,
                appovalRequestId
            };
        }
    }

    private int getSelectedRowCount() {
        return selectedRowList.size();
    }

    public class PendingApprovalTableAdapter extends BaseTableAdapter {
        private final CommonHeader[] commonHeaders;

        private final String[] headers = {
            EMPLOYEE_CODE_APP,
            EMP_NAME,
            LEAD_ID_APP,
            CLIENT_CODE,
            UCID,
            NEW_CLIENT_NAME,
            MODULE_NAME,
            FIELD_NAME,
            SUB_OPTION,
            STATUS,
            REQUEST_DATE,
            APPROVED_DATE,
            APPROVAL_REQUEST_ID
        };

        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        private PendingApprovalTableAdapter(Context context,
            ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new CommonHeader[] { new CommonHeader("asd") };
            this.hashArray = hashArray;


        }

        @Override
        public int getRowCount() {
            return commonHeaders[0].list.size();
        }


        public void add(HashMap<String, String> row) {
            hashArray.add(row);

            commonHeaders[0].list.add(new Item(
                row.get(EMPLOYEE_CODE_APP),
                row.get(EMP_NAME),
                row.get(LEAD_ID_APP),
                row.get(CLIENT_CODE),
                row.get(UCID),
                row.get(NEW_CLIENT_NAME),
                row.get(MODULE_NAME),
                row.get(FIELD_NAME),
                row.get(SUB_OPTION),
                row.get(STATUS),
                row.get(REQUEST_DATE),
                row.get(APPROVED_DATE),
                row.get(APPROVAL_REQUEST_ID)

            ));
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

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
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

        private View getFirstHeader(final int row, int column, View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(requireContext());
            if (convertView == null) {
                convertView =
                    inflater.inflate(R.layout.item_table_header_first_pending, parent, false);
            }

            TextView head = (convertView.findViewById(android.R.id.text1));

            if (clickedOn.equals(headers[column + 1]) && click > 1) {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
            } else {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
            }
            head.setText(headers[0]);

            cbClientName = convertView.findViewById(R.id.cbClientName);
            if (isAllChecked) {
                cbClientName.setChecked(true);
            } else {
                cbClientName.setChecked(false);
            }
            cbClientName.setOnClickListener(view -> {
                isAllChecked = ((CheckBox) view).isChecked();
                for (int ctr = 0; ctr < getRowCount(); ctr++) {
                    String approvalRequestId = getDevice(ctr - 1).data[12];
                    selectedRowMap.put(ctr, ((CheckBox) view).isChecked());

                    if (cbClientName.isChecked()) {
                        selectedRowList.add(approvalRequestId);
                        isSingleAllChecked = false;
                    } else {
                        selectedRowList.remove(approvalRequestId);
                    }
                }
                notifyDataSetChanged();
            });

            if (selectedRowList.isEmpty() || isSingleAllChecked) {
                cbClientName.setChecked(false);
            }
            if (getSelectedRowCount() > 0) {

            } else {
                head.setOnClickListener(v -> {

                    TextView temp = (TextView) v;
                    clickedOn = temp.getText().toString();
                    if (click == 1) {
                        sortByAMCName(click);
                        click++;
                    } else if (click > 1) {
                        sortByAMCName(click);
                        click = 1;
                    }
                });
            }
            return convertView;
        }

        private void sortByAMCName(Integer sortVal) {
            String sortCol = EMPLOYEE_CODE_APP;
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
            ArrayList<HashMap<String, String>> tempHashArray =
                (ArrayList<HashMap<String, String>>) hashArray.clone();
            hashArray.clear();

            for (int i = 0; i < out.size(); i++) {
                String sortedValue = out.get(i);
                for (int j = 0; j < tempHashArray.size(); j++) {
                    LinkedHashMap sortLinkedMap = (LinkedHashMap) tempHashArray.get(j);
                    String sortColVal = (String) sortLinkedMap.get(sortCol);

                    if (sortColVal.equals(sortedValue)) {
                        pendingApprovalTableAdapter.add(sortLinkedMap);
                        tempHashArray.remove(sortLinkedMap);
                    }
                }
            }
            pendingApprovalTableAdapter.notifyDataSetChanged();
        }

        //click on header other than symbol
        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = ((TextView) convertView.findViewById(android.R.id.text1));
            if (clickedOn.equals(headers[column]) && click > 1) {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_button, 0);
            } else {
                head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_button, 0);
            }

            head.setText(headers[column]);

            if (getSelectedRowCount() > 0) {

            } else {
                head.setOnClickListener(v -> {
                    String sortCol = ((TextView) v).getText().toString();

                    boolean isNumber = sortCol.equals(REJECTED);

                    ArrayList<String> outStr = new ArrayList<String>();
                    ArrayList<Double> outDbl = new ArrayList<Double>();

                    for (int j = 0; j < hashArray.size(); j++) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) hashArray.get(j);
                        String val = (String) linkedHashMap.get(sortCol);
                        if (isNumber) {
                            outDbl.add(Double.valueOf(Objects.requireNonNull(val)));
                        } else {
                            outStr.add(val);
                        }
                    }

                    if (isNumber) {
                        TextView temp = (TextView) v;
                        clickedOn = temp.getText().toString();
                        if (click == 1) {
                            Collections.sort(outDbl, new Comparator<Double>() {
                                public int compare(Double c1, Double c2) {
                                    return c1.compareTo(c2);
                                }
                            });
                            click++;
                        } else if (click > 1) {
                            Collections.reverse(outDbl);
                            click = 1;
                        }

                        commonHeaders[0].list.clear();
                        ArrayList<HashMap<String, String>> tempHashArray =
                            (ArrayList<HashMap<String, String>>) hashArray.clone();
                        hashArray.clear();

                        for (int i = 0; i < outDbl.size(); i++) {
                            Double sortedValue = outDbl.get(i);
                            for (int j = 0; j < tempHashArray.size(); j++) {
                                LinkedHashMap sortLinkedMap =
                                    (LinkedHashMap) tempHashArray.get(j);
                                String sortColVal = (String) sortLinkedMap.get(sortCol);
                                double sortColValue = Double.parseDouble(
                                    Objects.requireNonNull(sortColVal));
                                int retval = Double.compare(sortedValue, sortColValue);
                                if (retval == 0) {
                                    pendingApprovalTableAdapter.add(sortLinkedMap);
                                    tempHashArray.remove(sortLinkedMap);
                                }
                            }
                        }
                    } else {
                        TextView temp = (TextView) v;
                        clickedOn = temp.getText().toString();
                        if (click == 1) {
                            Collections.sort(outStr, new Comparator<String>() {
                                public int compare(String c1, String c2) {
                                    return c1.compareTo(c2);
                                }
                            });
                            click++;
                        } else if (click > 1) {
                            Collections.reverse(outStr);
                            click = 1;
                        }

                        commonHeaders[0].list.clear();
                        ArrayList<HashMap<String, String>> tempHashArray =
                            (ArrayList<HashMap<String, String>>) hashArray.clone();
                        hashArray.clear();

                        for (int i = 0; i < outStr.size(); i++) {
                            String sortedValue = outStr.get(i);
                            for (int j = 0; j < tempHashArray.size(); j++) {
                                LinkedHashMap sortLinkedMap =
                                    (LinkedHashMap) tempHashArray.get(j);
                                String sortColVal = (String) sortLinkedMap.get(sortCol);
                                if (sortedValue.equals(sortColVal)) {
                                    pendingApprovalTableAdapter.add(sortLinkedMap);
                                    tempHashArray.remove(sortLinkedMap);
                                }
                            }
                        }
                    }
                    pendingApprovalTableAdapter.notifyDataSetChanged();
                });
            }
            return convertView;
        }

        private View getFirstBody(final int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first_pending, parent, false);
            }
            LinearLayout listLayout =
                convertView.findViewById(R.id.item_table_first);

            listLayout.setBackgroundResource(
                row % 2 == 0 ? R.color.alternateBackground : R.color.white);

            ((TextView) convertView.findViewById(R.id.tv_table_first)).setText(
                getDevice(row).data[column + 1]);//row= 1 to ... & col=-1
            CheckBox cbClientNameBody = convertView.findViewById(R.id.cbClientNameBody);
            Boolean isChecked = selectedRowMap.get(row);
            if (isChecked == null) {
                isChecked = false;
            }
            final String approvalRequestId = getDevice(row).data[column + 13];

            if (isChecked) {
                cbClientNameBody.setChecked(true);
            } else {
                selectedRowList.remove(approvalRequestId);
                cbClientNameBody.setChecked(false);
            }

            cbClientNameBody.setOnClickListener(v -> {

                boolean isChecked1 = ((CheckBox) v).isChecked();
                selectedRowMap.put(row, isChecked1);

                if (isChecked1) {
                    selectedRowList.add(approvalRequestId);
                } else {
                    selectedRowList.remove(approvalRequestId);
                    isSingleAllChecked = true;
                }
                notifyDataSetChanged();
            });

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
                widthSize = getResources().getDimensionPixelSize(R.dimen._100sdp);
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
                return false;
            }
            return false;
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

    private void isDataPresent() {
        if (employeeCodeStr != null && profileCodeStr != null) {
            webServiceCall(employeeCodeStr, profileCodeStr);
        }
    }
}
