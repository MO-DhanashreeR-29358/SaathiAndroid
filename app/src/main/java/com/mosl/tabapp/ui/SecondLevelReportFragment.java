package com.mosl.tabapp.ui;

/**
 * Created by Sanket Gurav on 10/28/2017.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.reports.report.LeadReportRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import io.reactivex.disposables.Disposable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import timber.log.Timber;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD_ID;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.UCID;

public class SecondLevelReportFragment extends CommonFragment implements View.OnClickListener {
    private View reportView;
    private TableFixHeaders table;
    private SecondLevelReportFragment.SecondClickTableAdapter secondClickTableAdapter;
    private TextView tvNochart, txtLables;
    private boolean dataFoundFlag;
    private String widgetSystemName = "";
    private String leadId = "0", ucid = "0";
    public static final String STRING_MAP = "map";
    public static final String STRING_TITLE = "title";
    private Map<String, String> mMap;

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        reportView = super.onCreateView(inflater, container, savedInstanceState);
        mMap = (Map<String, String>) getArguments().getSerializable(STRING_MAP);
        widgetSystemName = getArguments().getString(STRING_TITLE);
        setUpView(reportView);
        if (mMap != null) {
            hideAppTitle();
            setUserVisibleHint(true);
            getReport();
        } else {
            dataFoundFlag = false;
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
            //Todo Warning
        }
        return reportView;
    }

    @Override protected int getLayout() {
        return R.layout.frag_second_level_report;
    }

    private void setUpView(View reportView) {
        tvNochart = (TextView) reportView.findViewById(R.id.tvNochart);
        txtLables = (TextView) reportView.findViewById(R.id.txtLables);
        table = (TableFixHeaders) reportView.findViewById(R.id.table);

        txtLables.setText(widgetSystemName);
    }

    private void getReport() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            LeadReportRequest
                request =
                new LeadReportRequest(0, mMap.get("Period_From"), false,
                    mMap.get("EmployeeCode"),
                    mMap.get("PageSize"), 0, 0, 0, mMap.get("ReportTypeSystemName"), "-1",
                    mMap.get("Period_To"), mMap.get("BranchCode"));
            Disposable subscribe = getSaathiApi().postLeadReport(request)
                .map(responseS -> {
                    Gson mGson = new Gson();
                    Type type = new TypeToken<HashMap<String, Object>>() {
                    }.getType();
                    HashMap<String, Object> response = mGson.fromJson(responseS, type);
                    String status = response.get(STATUS).toString();
                    if (status.equalsIgnoreCase("true")) {
                        return response;
                    } else {
                        String message = response.get(MESSAGE).toString();
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

    private void renderTable(HashMap<String, Object> response) {
        hideProgress();
        secondClickTableAdapter =
            new SecondClickTableAdapter(getActivity(), new ArrayList<HashMap<String, String>>());
        table.setAdapter(secondClickTableAdapter);
        Map<String, Object> responseJSONObject = (Map<String, Object>) response.get("Response");
        List<Map<String, Object>> arrayDetail =
            (List<Map<String, Object>>) responseJSONObject.get("Data");
        ArrayList<String> columns = new ArrayList<String>();
        ArrayList<String> columnsHeader = new ArrayList<String>();

        if (arrayDetail.size() != 0) {
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
                    secondClickTableAdapter.setHeaders(
                        stockArr);
                }
                LinkedHashMap<String, String> hm =
                    new LinkedHashMap<>();
                for (String column : columns) {
                    String columnData =
                        (objDetail.get(column) == null || objDetail.get(column).toString() == null)
                            ? "" : objDetail.get(column).toString();

                    if (objDetail.get(column) instanceof Number) {
                        String strValue = objDetail.get(column).toString().trim();

                        if (strValue.indexOf(".") > -1
                            && !strValue.endsWith(".0")
                            && !strValue.endsWith(".00")) {
                            columnData = String.format("%.2f",
                                Double.parseDouble(objDetail.get(column).toString()));
                        } else {
                            columnData = String.format("%.0f",
                                Double.parseDouble(objDetail.get(column).toString()));
                        }
                    }
                    if (column.equalsIgnoreCase("lead_id")) {
                        column = column.toUpperCase();
                    }
                    hm.put(getRefreshColumnName(column), columnData.trim());
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
    }

    ///SDTable
    private class CommonHeader {
        private final String name;
        private final List<SecondLevelReportFragment.Item> list;

        CommonHeader(String name) {
            this.name = name;
            list = new ArrayList<SecondLevelReportFragment.Item>();
        }

        public int size() {
            return list.size();
        }

        public SecondLevelReportFragment.Item get(int i) {
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
    }

    public class SecondClickTableAdapter extends BaseTableAdapter {
        private final SecondLevelReportFragment.CommonHeader commonHeaders[];
        private String headers[] = {};
        private final float density;
        HashMap hashMap;
        ArrayList<HashMap<String, String>> hashArray;
        private LayoutInflater inflater = null;

        public SecondClickTableAdapter(Context context,
            ArrayList<HashMap<String, String>> hashArray) {
            commonHeaders = new SecondLevelReportFragment.CommonHeader[] {
                new SecondLevelReportFragment.CommonHeader("asd")
            };
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

            commonHeaders[0].list.add(new SecondLevelReportFragment.Item(rowData));
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
                            if (!isBlank(getValue(row, "EMPLOYEE CODE"))) {
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
                                //navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                                //    true);
                            }
                        }
                    });

                    break;
                case 3:
                    view = getBody(row, column, convertView, parent);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View view) {
                            if (!isBlank(getValue(row, "EMPLOYEE CODE"))) {
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
                                //navigateTo(NAV_TO_LEAD_VIEW_MAIN, bundle, getFragmentManager(),
                                //    true);
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

        private SecondLevelReportFragment.CommonHeader getFamily(int row) {
            int family = 0;
            while (row >= 0) {
                row -= commonHeaders[family].size();
            }
            return commonHeaders[family];
        }

        private SecondLevelReportFragment.Item getDevice(int row) {
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
}
