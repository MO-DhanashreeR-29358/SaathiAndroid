package com.mosl.tabapp.dashboard;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.CommonRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.navigation.NavigationConstants;
import com.mosl.tabapp.util.SpinnerAdapterFactory;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.constants.MOSLConstants.COL_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.LEAD;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.MESSAGE;
import static com.mosl.tabapp.constants.MOSLConstants.ROW_FILTER;
import static com.mosl.tabapp.constants.MOSLConstants.SELF_KEY;
import static com.mosl.tabapp.constants.MOSLConstants.SOME_ERROR_OCCURED_PLEASE_RETRY;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TYPESOFLEAD;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.PERIOD_FILTER;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.PRODUCT_FILTER;
import static com.mosl.tabapp.dashboard.leads.LeadsFragment.SOURCE_FILTER;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/14/2017.
 */

public class ClientsLeadsFragmentOld extends CommonFragment implements View.OnClickListener {
    private HorizontalBarChart mChart;
    private TextView tvTotal;
    private TextView tv;
    private LinearLayout llShowMore;
    private Spinner spFilter1, spFilter2, spFilter3;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Map addtionalParameterMap = new HashMap<>();

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View leadsView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(leadsView);
        hideAppTitle();
        return leadsView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_lead;
    }

    private void setUpView(View leadsView) {
        spFilter1 = leadsView.findViewById(R.id.spFilter1);
        spFilter2 = leadsView.findViewById(R.id.spFilter2);
        spFilter3 = leadsView.findViewById(R.id.spFilter3);

        mChart = leadsView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        llShowMore = leadsView.findViewById(R.id.llShowMore);
        tvTotal = leadsView.findViewById(R.id.tvTotal);
        TextView tvShowMore = leadsView.findViewById(R.id.tvShowMore);
        tvShowMore.setPaintFlags(tvShowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv = leadsView.findViewById(R.id.tv);

        llShowMore.setOnClickListener(this);

        Legend l = mChart.getLegend();
        l.setTextSize(getResources().getDimension(R.dimen.chart_text_size));
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);

        if (dashboardModel != null) {
            DashboardGroupModel groupModel =
                    dashboardModel.getGroupModel(DashboardSystemModel.getLeadSystemName());
            WidgetModel model;
            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                if (widgetList.size() == 1) {
                    model = widgetList.get(0);
                } else {
                    model = widgetList.get(1);
                }
                widgetSystemName = model.getSystemName();

                //set filter data
                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 2) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());
                    spFilter1.setSelection(this.getArguments().getInt(PERIOD_FILTER));

                    ArrayAdapter<FilterRowData> adapter2 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter2.setAdapter(adapter2);
                    AdapterUtils.setSpinnerData(adapter2, spFilter2, filterList.get(1).getFilterData());
                    spFilter2.setSelection(this.getArguments().getInt(PRODUCT_FILTER));

                    spFilter2.setVisibility(View.VISIBLE);
                    getSpinnerSelection();

                }
                if (filterList != null && filterList.size() == 3) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());
                    spFilter1.setSelection(this.getArguments().getInt(PERIOD_FILTER));

                    ArrayAdapter<FilterRowData> adapter2 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter2.setAdapter(adapter2);
                    AdapterUtils.setSpinnerData(adapter2, spFilter2, filterList.get(1).getFilterData());
                    spFilter2.setSelection(this.getArguments().getInt(PRODUCT_FILTER));

                    ArrayAdapter<FilterRowData> adapter3 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter3.setAdapter(adapter3);
                    AdapterUtils.setSpinnerData(adapter3, spFilter3, filterList.get(2).getFilterData());
                    spFilter3.setSelection(this.getArguments().getInt(SOURCE_FILTER));

                    spFilter2.setVisibility(View.VISIBLE);
                    spFilter3.setVisibility(View.VISIBLE);
                    getSpinnerSelection();
                } else if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
                    spFilter1.setAdapter(adapter1);
                    AdapterUtils.setSpinnerData(adapter1, spFilter1, filterList.get(0).getFilterData());
                    spFilter1.setSelection(this.getArguments().getInt(PERIOD_FILTER));
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

    private void renderLeadStackedBarChart(Map<String, Object> responseJsonObj) {
        tv.setVisibility(View.VISIBLE);
        llShowMore.setVisibility(View.VISIBLE);

        ArrayList<String> columnsYAxis = new ArrayList<String>();
        String total = "";
        String[] lableString = new String[]{};

        final ArrayList<ArrayList<String>> yAxisListFirst =
                new ArrayList<>();

        List<Map<String, Object>> arrayDetail =
                (List<Map<String, Object>>) responseJsonObj.get(
                        "Table");
        if (arrayDetail.size() != 0) {
            for (int y = 2; y < arrayDetail.size(); y++) {
                Map<String, Object> objDetail =
                        (Map<String, Object>) arrayDetail.get(y);
                String stages =
                        objDetail.get("stage").toString().equals("")
                                ? "0"
                                : objDetail.get("stage").toString();
                if (stages.equalsIgnoreCase("Total")) {
                    total =
                            (objDetail.get("Total") == null || objDetail
                                    .get("Total")
                                    .toString()
                                    .equals("")) ? "0"
                                    : objDetail.get("Total").toString();
                }
            }
            try {
                if (responseJsonObj.get("Table1") != null) {
                    List<Map<String, Object>> arrayDetailTotal =
                            (List<Map<String, Object>>) responseJsonObj.get(
                                    "Table1");
                    Map<String, Object> jobjFKey =
                            arrayDetailTotal.get(0);
                    for (Object keySetE : jobjFKey.keySet()) {
                        if (keySetE.toString()
                                .equalsIgnoreCase("id")
                                | keySetE.toString()
                                .trim()
                                .equalsIgnoreCase("Total")) {
                        } else {
                            columnsYAxis.add(keySetE.toString());
                        }
                    }
                    for (Map<String, Object> jobj : arrayDetailTotal) {

                        final ArrayList<String> yAxisList =
                                new ArrayList<String>();

                        for (Object keySetE : columnsYAxis) {
                            if (keySetE.toString()
                                    .equalsIgnoreCase("id")
                                    || keySetE.toString()
                                    .equalsIgnoreCase("Total")) {
                            } else {
                                yAxisList.add(
                                        jobj.get(keySetE).toString());
                            }
                        }
                        yAxisListFirst.add(yAxisList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (yAxisListFirst != null
                    && yAxisListFirst.size() > 0) {
                lableString = new String[]{
                        yAxisListFirst.get(0).get(0)
                        , yAxisListFirst.get(1).get(0)
                };
                yAxisListFirst.get(0).remove(0);
                yAxisListFirst.get(1).remove(0);
                tvTotal.setText(total);
                stackedBarData(columnsYAxis, yAxisListFirst,
                        lableString);
            }
        }
    }

    private void renderClientLead(UiModel<Map<String, Object>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;

        if (error != null) {
            showOnFailurePopup(error);
        }

        Map<String, Object> data = uiModel.data;

        if (data != null) {
            try {
                renderLeadStackedBarChart(data);
            } catch (Exception e) {
                alertDialog(requireContext(), 0, getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            Map<String, String> additionalMap = getAddtionalParameterMap();

            CommonRequest request = new CommonRequest(
                    businessProfileId,
                    additionalMap,
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
                        String status = response.get(STATUS).toString();
                        if (status.equalsIgnoreCase("true")) {
                            return responseJsonObj;
                        } else {
                            String message = response.get(MESSAGE).toString();
                            throw new ServiceFailureException(message, message);
                        }
                    })
                    .compose(applyUiModel())
                    .subscribe(this::renderClientLead);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void stackedBarData(final ArrayList<String> xAxisValue,
                                final ArrayList<ArrayList<String>> yAxisFirst, final String[] lableString) {
        xAxisValue.remove(0);

        Collections.reverse(xAxisValue);
        try {
            Collections.reverse(yAxisFirst.get(0));
            Collections.reverse(yAxisFirst.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        xAxis.setDrawAxisLine(false);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValue));

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < yAxisFirst.get(0).size(); i++) {
            try {
                float val1 = Float.parseFloat(yAxisFirst.get(0).get(i));
                float val2 = Float.parseFloat(yAxisFirst.get(1).get(i));
                yVals1.add(new BarEntry(i + 0.5f, new float[]{val1, val2}));
            } catch (java.lang.NumberFormatException e) {
                e.printStackTrace();
            }
        }

        BarDataSet set1;

        ArrayList<Integer> colors = new ArrayList<Integer>();
        int[] VORDIPLOM_COLORS = {
                Color.rgb(38, 127, 183), Color.rgb(255, 154, 0)
        };
        for (int c : VORDIPLOM_COLORS)
            colors.add(c);
        set1 = new BarDataSet(yVals1, "");
        set1.setColors(colors);
        set1.setStackLabels(lableString);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new CRMValueFormatter());
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(13f);
        data.setBarWidth(0.6f);

        mChart.setData(data);
        mChart.getLegend().setEnabled(false);
        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                if (e.getY() == 0.0f) return;
                mChart.highlightValues(null);
                addtionalParameterMap.clear();
                addtionalParameterMap.put(COL_FILTER, xAxisValue.get((int) (e.getX())));
                addtionalParameterMap.put(ROW_FILTER, lableString[h.getStackIndex()]);
                addtionalParameterMap.putAll(getAddtionalParameterMap());
                Bundle args = new Bundle();
                args.putString(SYSTEM_NAME, widgetSystemName);
                args.putString(LABEL, LEAD);
                args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                if (lableString[h.getStackIndex()].equalsIgnoreCase("Assigned")) {
                    if (!String.valueOf(Float.parseFloat(yAxisFirst.get(1).get((int) (e.getX()))))
                            .equalsIgnoreCase("0.0")) {
                        navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD,
                                args, true, false);
                    }
                } else {
                    navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                            true, false);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llShowMore) {
            Bundle args = new Bundle();
          ClientsLeadShowFragmentOld clientsFragment =
              ClientsLeadShowFragmentOld.newInstance();
            args.putSerializable(DASHBOARD_MODEL, dashboardModel);
            args.putSerializable(MAP, (Serializable) getAddtionalParameterMap());
            args.putString(SYSTEM_NAME, widgetSystemName);
            args.putString(TYPESOFLEAD, LEAD);
            clientsFragment.setArguments(args);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.llReplaceClientFragment, clientsFragment)
                    .commit();
        }
    }
}
