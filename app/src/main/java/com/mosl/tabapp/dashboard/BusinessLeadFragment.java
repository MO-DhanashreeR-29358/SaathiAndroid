package com.mosl.tabapp.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.ui.MOSLCommonFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by raju on 2/20/2017.
 */

public class BusinessLeadFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View leadTeamView;
    static Context mContext;
    private HorizontalBarChart mChart;
    private final int itemcount = 8;
    private TextView tvStartDate, tvEndDate;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate, dpToDate;
    private ImageView close;
    private Button btnSubmit;
    private TextView tvDate;
    private LinearLayout llShowMore;
    private Spinner spLeads;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadTeamView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead_team);
        setUpView(leadTeamView);
        hideAppTitle(getClass().toString());
        return leadTeamView;
    }

    public static BusinessLeadFragment newInstance(Context context) {
        BusinessLeadFragment fragment = new BusinessLeadFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadTeamView) {
        spLeads = (Spinner) leadTeamView.findViewById(R.id.spLeads);
        mChart = (HorizontalBarChart) leadTeamView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawValueAboveBar(false);

        llShowMore = (LinearLayout) leadTeamView.findViewById(R.id.llShowMore);
        tvDate = (TextView) leadTeamView.findViewById(R.id.tvDate);
        tvStartDate = (TextView) leadTeamView.findViewById(R.id.tvStartDate);
        // tvEndDate = (TextView) leadTeamView.findViewById(R.id.tvEndDate);
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
        tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy"));
        tvStartDate.setOnClickListener(this);
       /* tvEndDate.setText(CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy"));
        tvEndDate.setOnClickListener(this);*/
        llShowMore.setOnClickListener(this);
        tvDate.setText("As on 31 December,2016");


        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis leftAxis = mChart.getAxisLeft();
        //  leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        //remove axis line
        xAxis.setDrawAxisLine(false);
        final ArrayList<String> mActivities = new ArrayList<String>();
        mActivities.add("C11");
        mActivities.add("C10");
        mActivities.add("C9");
        mActivities.add("C8");
        mActivities.add("C7");
        mActivities.add("C6");
        mActivities.add("C5");
        mActivities.add("C4");
        mActivities.add("C3");
        mActivities.add("C2");
        mActivities.add("C1");
        mActivities.add("C0");
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
        stackedBarData();
    }


    private void stackedBarData() {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < itemcount; i++) {
            float val1 = (float) (Math.random() * 150) / 2;

            float val2 = (float) (Math.random() * 150) / 2;


            yVals1.add(new BarEntry(i + 0.5f, new float[]{val1, val2}));
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
        set1.setColors(colors);
        set1.setStackLabels(new String[]{"Self", "Assigned", "Marriages"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new CRMValueFormatter());
        data.setValueTextColor(Color.WHITE);
        // width bar chart
        data.setBarWidth(0.6f);

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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tvStartDate) {
            selectDate();
        }
       /* if (id == R.id.tvEndDate) {
            selectDate();
        }*/
        if (id == R.id.llShowMore) {
            BusinessLeadShowFragment leadShowFragment = BusinessLeadShowFragment.newInstance(getContext());
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBusinessSnapshotFragment, leadShowFragment).commit();
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
        // dpToDate = (DatePicker) dialog.findViewById(R.id.datePickerToDate);
        close = (ImageView) dialog.findViewById(R.id.ibtnClose);
        btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);


        if (mainFromDate != null) {
            String[] dateSplit = mainFromDate.split("-");
            dpFromDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        } else {
            dpFromDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.getFirstDayOfWeek(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mainFromDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);
                }
            });
        }

       /* if (mainFromDate != null) {
            String[] dateSplit = mainToDate.split("-");
            dpToDate.init(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);

                }
            });
        } else {
            dpToDate.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    today.set(year, monthOfYear, dayOfMonth);
                    long dayy = today.getActualMaximum(Calendar.DATE);
                    mainToDate = year + "-" + prefix(monthOfYear + 1) + "-" + prefix(dayOfMonth);


                }
            });
        }*/

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                if (mainFromDate != null) {
                    fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                    fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                    tvStartDate.setText(fromPostingDate);
                }
                dialog.dismiss();
               /* if (fromDat.after(toDate)) {
                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), "Invalid date range ", "OK", true, null);
                } else {
                    if (mainFromDate != null) {
                        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
                        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
                        tvStartDate.setText(fromPostingDate);
                    }
                    if (mainToDate != null) {
                        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
                        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
                        tvEndDate.setText(toPostingDate);
                    }

                    dialog.dismiss();
                }*/


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
}
