package com.mosl.tabapp.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.CommonRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.tablefixheader.TableFixHeaders;
import com.mosl.tabapp.tablefixheader.adapters.BaseTableAdapter;
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.common.CRMAppUtil.getRefreshColumnName;
import static com.mosl.tabapp.constants.MOSLConstants.BUSINESS_SNAPSHOT_TITLE;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SELF_KEY;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 6/30/2017.
 */

public class BusinessSnapshotFragment extends CommonFragment {
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Spinner spFilter1, spFilter2, spFilter3;
    private TableFixHeaders table;
    private TextView tvNochart;
    private Map addtionalParameterMap = new HashMap<>();
    private LinkedHashMap lMap;
    private ArrayList<HashMap<String, String>> hierarchyMapList;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View businessSnapshotView = super.onCreateView(inflater, container, savedInstanceState);
        hideAppTitle();
        setUpView(businessSnapshotView);
        return businessSnapshotView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_business_snapshot;
    }


    private void setUpView(View businessSnapshotView) {

        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
        table = businessSnapshotView.findViewById(R.id.table);
        tvNochart = businessSnapshotView.findViewById(R.id.tvNochart);
        spFilter1 = businessSnapshotView.findViewById(R.id.spFilter1);
        spFilter2 = businessSnapshotView.findViewById(R.id.spFilter2);
        spFilter3 = businessSnapshotView.findViewById(R.id.spFilter3);

        if (dashboardModel != null) {
            DashboardGroupModel groupModel =
                    dashboardModel.getGroupModel(DashboardSystemModel.getBusinessSnapshotSystemName());
            WidgetModel model;
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                model = widgetList.get(0);
                widgetSystemName = model.getSystemName();

                //set filter data
                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 2) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());

                    ArrayAdapter<FilterRowData> adapter2 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter2.setAdapter(adapter2);
                    AdapterUtils.setSpinnerData(adapter2, spFilter2, filterList.get(1).getFilterData());

                    spFilter2.setVisibility(View.VISIBLE);
                    getSpinnerSelection();
                }
                if (filterList != null && filterList.size() == 3) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());

                    ArrayAdapter<FilterRowData> adapter2 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter2.setAdapter(adapter2);
                    AdapterUtils.setSpinnerData(adapter2, spFilter2, filterList.get(1).getFilterData());

                    ArrayAdapter<FilterRowData> adapter3 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter3.setAdapter(adapter3);
                    AdapterUtils.setSpinnerData(adapter3, spFilter3, filterList.get(2).getFilterData());

                    spFilter2.setVisibility(View.VISIBLE);
                    spFilter3.setVisibility(View.VISIBLE);
                    getSpinnerSelection();
                } else if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());
                    getSpinnerSelection();
                }
            }
        }
    }

    private void getSpinnerSelection() {
        disposable.add(
                Observable.merge(itemSelections(spFilter1), itemSelections(spFilter2), itemSelections(spFilter3))
                        .filter(InvalidSelectionPredicate)
                        .skip(1)
                        .subscribe(xyz -> webServiceCall()));

    }

    private FilterRowData getFilter1() {
        return (FilterRowData) spFilter1.getSelectedItem();
    }

    private FilterRowData getFilter2() {
        return (FilterRowData) spFilter2.getSelectedItem();
    }

    private FilterRowData getFilter3() {
        return (FilterRowData) spFilter3.getSelectedItem();
    }

    private Map<String, String> getAddtionalParameterMap() {

        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {

            if (getFilter1() != null) {
                addtionalParameterMap.put(getFilter1().getKeyValue(), getFilter1().getValue());
            }
            if (getFilter2() != null) {
                addtionalParameterMap.put(getFilter2().getKeyValue(), getFilter2().getValue());
            }
            if (getFilter3() != null) {
                addtionalParameterMap.put(getFilter3().getKeyValue(), getFilter3().getValue());
            }
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void webServiceCall() {
        ////////////////////////////////////////////
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            CommonRequest request = new CommonRequest(
                    businessProfileId,
                    getAddtionalParameterMap(),
                    widgetSystemName,
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
                        List<Map<String, Object>> tableResponseList =
                                (List<Map<String, Object>>) responseJsonObj.get(
                                        "Table");
                        String status = response.get(STATUS).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return tableResponseList;
                        } else {
                            String message = response.get(MESSAGE).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::renderBusinessSnapshot);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderBusinessSnapshot(UiModel<List<Map<String, Object>>> uiModel) {
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
                alertDialog(requireContext(), 0,
                        requireContext().getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void renderTable(List<Map<String, Object>> tableList) {
        hierarchyMapList = new ArrayList<>();
        SecondClickTableAdapter secondClickTableAdapter = new SecondClickTableAdapter(new ArrayList<>());
        table.setAdapter(secondClickTableAdapter);
        ArrayList<String> columns = new ArrayList<>();
        ArrayList<String> columnsHeader = new ArrayList<>();
        if (tableList.size() != 0) {
            for (int y = 0; y < tableList.size(); y++) {
                Map<String, Object> objDetail = tableList.get(y);
                if (columns.size() == 0) {
                    for (Object columnName : objDetail.keySet()) {
                        if (!columnName.toString().equalsIgnoreCase("DisplayOrder")) {
                            columns.add(columnName.toString());
                            if (!columnName.toString().equalsIgnoreCase("flg_empcode")) {
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
                    if (column.equalsIgnoreCase("flg_empcode")) {
                        map.put("flg_empcode", objDetail.get("flg_empcode"));
                    } else {
                        String columnData = (objDetail.get(column) == null
                                || objDetail.get(column).toString() == null) ? ""
                                : objDetail.get(column).toString();

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

                        hm.put(CRMAppUtil.getRefreshColumnName(column), columnData.trim());
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
        } else {
            table.setVisibility(View.GONE);
            tvNochart.setVisibility(View.VISIBLE);
        }
    }

    ///SDTable
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
        private String[] headers = {};
        ArrayList<HashMap<String, String>> hashArray;
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
                        Set<String> stringSet = hashArray.get(row).keySet();
                        String columnName = stringSet.toArray()[column].toString();
                        addtionalParameterMap.clear();
                        addtionalParameterMap.put(COL_FILTER, columnName);
                        addtionalParameterMap.put(ROW_FILTER,
                                hashArray.get(row).values().toArray()[0].toString());
                        addtionalParameterMap.putAll(getAddtionalParameterMap());
                        if (hierarchyMapList.size() >= 1) {
                            addtionalParameterMap.put("flt_Hierarchy_empCode",
                                    hierarchyMapList.get(row).get("flg_empcode"));
                        }
                        Bundle args = new Bundle();
                        args.putString(SYSTEM_NAME, widgetSystemName);
                        args.putString(LABEL, BUSINESS_SNAPSHOT_TITLE);
                        args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                        lMap = (LinkedHashMap) hashArray.get(row);
                        String columnData = lMap.get(columnName).toString();
                        if (columnData != null) {
                            if (!columnData.trim().equalsIgnoreCase("0")) {
                                navigator.openPage(
                                        NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
                                        args,
                                        true, false);
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


        //click on symbol header
        private View getFirstHeader(View convertView, ViewGroup parent) {
            inflater = LayoutInflater.from(requireContext());
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
