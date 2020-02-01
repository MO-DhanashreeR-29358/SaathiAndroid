package com.mosl.tabapp.bankmeetingreport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.aubankmeetingreport.MeetingReportRequest;
import com.mosl.saathi.api.model.aubankmeetingreport.MeetingRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.databinding.FragmentBankMeetingReportBinding;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;

import org.jetbrains.annotations.NotNull;

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

import static androidx.databinding.DataBindingUtil.inflate;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.constants.MOSLConstants.DATE_FORMAT_OF_DD_MMM_YYYY;
import static com.mosl.tabapp.constants.MOSLConstants.INVALID_DATE_RANGE;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class BankMeetingReportFragmentNew extends CommonFragment {

    final public static String TAG = BankMeetingReportFragmentNew.class.getSimpleName();
    private FragmentBankMeetingReportBinding binding;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(
                inflater, R.layout.fragment_bank_meeting_report, container, false
        );
        setUpView();
        return binding.getRoot();
    }

    private void setUpView() {
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");

        binding.tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY));
        binding.rlEndDate.setOnClickListener(
                v -> selectDate()
        );
        binding.tvEndDate.setText(CRMAppUtil.getDateStr(toDate, DATE_FORMAT_OF_DD_MMM_YYYY));
        binding.rlStartDate.setOnClickListener(
                v -> selectDate()
        );
        binding.ivEmailAll.setOnClickListener(
                v -> sendEmail()
        );
        getReport();
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

        dpToDate.setMaxDate(System.currentTimeMillis() - 1000);
        dpFromDate.setMaxDate(System.currentTimeMillis() - 1000);
        ImageView close = dialog.findViewById(R.id.ibtnClose);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        try {
            if (mainFromDate != null) {
                String[] dateSplit = mainFromDate.split("-");
                dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1,
                        Integer.parseInt(dateSplit[2]),
                        (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
                                year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth));
            } else {
                dpFromDate.init(today.get(Calendar.YEAR), Calendar.APRIL, today.getFirstDayOfWeek(),
                        (view, year, monthOfYear, dayOfMonth) -> mainFromDate =
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
                            //long dayy = today.getActualMaximum(Calendar.DATE);
                            mainToDate =
                                    year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                        });
            }
        } catch (Exception e) {
            Timber.d(e, TAG);
        }
        close.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
            toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
            if (fromDat.after(toDate)) {
                alertDialog(requireContext(), 0, "Motilal Oswal", INVALID_DATE_RANGE,
                        "OK", true, null);
            } else {
                if (mainFromDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    fromPostingDate =
                            CRMAppUtil.getDateStr(fromDat, DATE_FORMAT_OF_DD_MMM_YYYY);
                    binding.tvStartDate.setText(fromPostingDate);
                }
                if (mainToDate != null) {
                    toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                    toPostingDate = CRMAppUtil.getDateStr(toDate, DATE_FORMAT_OF_DD_MMM_YYYY);
                    binding.tvEndDate.setText(toPostingDate);
                }
                getReport();
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

    private void sendEmail() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            MeetingRequest
                    request =
                    new MeetingRequest(0,
                            0,
                            0,
                            true,
                            CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd"),
                            CRMAppUtil.getDateStr(toDate, "yyyy-MM-dd"),
                            0);
            MeetingReportRequest reportRequest = new MeetingReportRequest();
            reportRequest.request = request;
            Disposable subscribe = getSaathiApi().postBankMeetingReport(reportRequest)
                    .map(responseS -> {
                        Gson mGson = new Gson();
                        Type type = new TypeToken<HashMap<String, Object>>() {
                        }.getType();
                        HashMap<String, Object> response = mGson.fromJson(responseS, type);
                        String status = Objects.requireNonNull(response.get(STATUS)).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return response;
                        } else {
                            String message = Objects.requireNonNull(response.get(MESSAGE)).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::renderEmail);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderEmail(UiModel<HashMap<String, Object>> uiModel) {
        toggleProgress(uiModel.inProgress);
        Throwable error = uiModel.error;
        if (error != null) {
            Timber.d("throwable %s", error.getMessage());
            showOnFailurePopup(error);
        }

        HashMap<String, Object> data = uiModel.data;
        if (data != null) {
            alertDialog(requireContext(), 0, "Motilal Oswal", "Email Send Successfully",
                    "OK", true, null);
        }
    }

    private void getReport() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            MeetingRequest
                    request =
                    new MeetingRequest(0,
                            Integer.parseInt(MOSLConstants.REPORT_PAGE_SIZE),
                            0,
                            false,
                            CRMAppUtil.getDateStr(fromDat, "yyyy-MM-dd"),
                            CRMAppUtil.getDateStr(toDate, "yyyy-MM-dd"),
                            0);
            MeetingReportRequest reportRequest = new MeetingReportRequest();
            reportRequest.request = request;
            Disposable subscribe = getSaathiApi().postBankMeetingReport(reportRequest)
                    .map(responseS -> {
                        Gson mGson = new Gson();
                        Type type = new TypeToken<HashMap<String, Object>>() {
                        }.getType();
                        HashMap<String, Object> response = mGson.fromJson(responseS, type);
                        String status = Objects.requireNonNull(response.get(STATUS)).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return response;
                        } else {
                            String message = Objects.requireNonNull(response.get(MESSAGE)).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::renderReports);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderReports(UiModel<HashMap<String, Object>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            Timber.d("throwable %s", error.getMessage());
            showOnFailurePopup(error);
        }

        HashMap<String, Object> data = uiModel.data;
        if (data != null) {
            renderTable(data);
        }
    }

    @SuppressLint("DefaultLocale")
    private void renderTable(HashMap<String, Object> response) {
        hideProgress();
        MeetingReportTableAdapter meetingReportTableAdapter =
                new MeetingReportTableAdapter(getActivity(),
                        new ArrayList<>());
        binding.tblReport.setAdapter(meetingReportTableAdapter);
        Map<String, Object> responseJSONObject = (Map<String, Object>) response.get("Response");
        List<Map<String, Object>> arrayDetail =
                (List<Map<String, Object>>) responseJSONObject.get("Data");
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> columnsHeader = new ArrayList<String>();

        if (Objects.requireNonNull(arrayDetail).size() != 0) {
            for (int y = 0; y < arrayDetail.size(); y++) {
                Map<String, Object> objDetail = arrayDetail.get(y);

                if (columns.size() == 0) {
                    for (Object columnName : objDetail.keySet()) {
                        columns.add(columnName.toString());
                        if (columnName.toString().equalsIgnoreCase("lead_id")) {
                            columnName = columnName.toString().toUpperCase();
                        }
                        columnsHeader.add(getRefreshColumnName(columnName.toString()));
                    }
                    String[] stockArr = new String[columnsHeader.size()];
                    stockArr = columnsHeader.toArray(stockArr);
                    meetingReportTableAdapter.setHeaders(
                            stockArr);
                }
                LinkedHashMap<String, String> hm =
                        new LinkedHashMap<>();
                for (String column : columns) {
                    String columnData =
                            objDetail.get(column) == null
                                    ? "" : Objects.requireNonNull(objDetail.get(column)).toString();

                    if (objDetail.get(column) instanceof Number) {
                        String strValue =
                                Objects.requireNonNull(objDetail.get(column)).toString().trim();

                        if (strValue.contains(".")
                                && !strValue.endsWith(".0")
                                && !strValue.endsWith(".00")) {
                            columnData = String.format("%.2f",
                                    Double.parseDouble(
                                            Objects.requireNonNull(objDetail.get(column)).toString()));
                        } else {
                            columnData = String.format("%.0f",
                                    Double.parseDouble(
                                            Objects.requireNonNull(objDetail.get(column)).toString()));
                        }
                    }
                    if (column.equalsIgnoreCase("lead_id")) {
                        column = column.toUpperCase();
                    }
                    hm.put(getRefreshColumnName(column), columnData.trim());
                }
                meetingReportTableAdapter.add(hm);
            }
            meetingReportTableAdapter.notifyDataSetChanged();
            //dataFoundFlag = true;
            binding.tblReport.setVisibility(View.VISIBLE);
            binding.tvNoRecords.setVisibility(View.GONE);
        } else {
            //dataFoundFlag = false;
            binding.tblReport.setVisibility(View.GONE);
            binding.tvNoRecords.setVisibility(View.VISIBLE);
        }
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

    public class MeetingReportTableAdapter extends BaseTableAdapter {
        private final CommonHeader[] commonHeaders;
        ArrayList<HashMap<String, String>> hashArray;
        private String[] headers = {};
        private LayoutInflater inflater = null;

        private MeetingReportTableAdapter(Context context,
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
            return commonHeaders[0].list.size();
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
            inflater = LayoutInflater.from(getActivity());
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header_first, parent, false);
            }
            RelativeLayout relativeLayout =
                    convertView.findViewById(R.id.relativeLayout);
            TextView head = convertView.findViewById(android.R.id.text1);
            try {
                head.setText(headers[0]);
            } catch (Exception e) {
                binding.tvNoRecords.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }

            return convertView;
        }

        private View getHeader(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_header, parent, false);
            }
            TextView head = convertView.findViewById(android.R.id.text1);
            head.setText(headers[column]);
            return convertView;
        }

        private View getFirstBody(int row, int column, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_table_first, parent, false);
            }
            LinearLayout listLayout = convertView.findViewById(R.id.item_table_first);
            listLayout.setBackgroundResource(
                    row % 2 == 0 ? R.color.alternateBackground : R.color.white);
            final TextView tv_table_first = convertView.findViewById(R.id.tv_table_first);
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
