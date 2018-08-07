package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.ui.MOSLCommonFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by raju on 3/1/2017.
 */

public class PerformanceHierarchyFragment extends MOSLCommonFragment {
    private View performanceView;
    static Context mContext;
    protected HorizontalBarChart mChart;
    private final int itemcount = 8;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        performanceView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_performane_hierarchy);
        setUpView(performanceView);
        return performanceView;
    }

    public static PerformanceHierarchyFragment newInstanse(Context context) {
        PerformanceHierarchyFragment fragment = new PerformanceHierarchyFragment();
        mContext = context;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View performanceView) {

        mChart = (HorizontalBarChart) performanceView.findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        //remove axis line
        xAxis.setDrawAxisLine(true);
        final ArrayList<String> mActivities = new ArrayList<String>();
        mActivities.add("mumbai");
        mActivities.add("pune");
        mActivities.add("banglore");
        mActivities.add("jaipur");
        mActivities.add("belapur");
        mActivities.add("delhi");
        mActivities.add("manipur");
        mActivities.add("bihar");
        mActivities.add("gujrat");
        mActivities.add("maharashtra");
        mActivities.add("kolhapur");
        mActivities.add("goa");
        Collections.reverse(mActivities);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                try {
                    return mActivities.get((int) value % mActivities.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setEnabled(false);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        setData();
        mChart.setFitBars(true);
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        l.setEnabled(false);
    }

    private void setData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < itemcount; i++) {
            float val1 = (float) (Math.random() * 150) / 2;

            float val2 = (float) (Math.random() * 150) / 2;


            yVals1.add(new BarEntry(i + 0.5f, val1));
        }

        BarDataSet set1;

        ArrayList<Integer> colors = new ArrayList<Integer>();
        int[] VORDIPLOM_COLORS = {
                Color.rgb(38, 127, 183), Color.rgb(255, 154, 0)
        };
        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        //  colors.add(ColorTemplate.getHoloBlue());
        set1 = new BarDataSet(yVals1, "");
        set1.setColors(Color.rgb(255, 154, 0));
        set1.setStackLabels(new String[]{"Self", "Assigned", "Marriages"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new CRMValueFormatter());
        data.setValueTextColor(Color.BLACK);
        // width bar chart
        data.setBarWidth(0.3f);

        mChart.setData(data);
        mChart.invalidate();
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                pieChartDetail(e.getX());
            }

            private void pieChartDetail(float sliceIndex) {
                Bundle args = new Bundle();
                Log.d("index", Float.toString(sliceIndex + 0.5f));
                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);

            }

            @Override
            public void onNothingSelected() {

            }

        });
    }
}
