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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 4/22/2017.
 */

public class TeamLeadsFragment extends MOSLCommonFragment implements MOSLConstants, View.OnClickListener {
    private View leadsView;
    static Context mContext;
    private HorizontalBarChart mChart;
    private TextView tvStartDate, tvEndDate;
    private String mainFromDate, mainToDate, toPostingDate, fromPostingDate;
    private Date fromDat, toDate;
    private Dialog dialog;
    private Calendar today;
    private DatePicker dpFromDate, dpToDate;
    private ImageView close;
    private Button btnSubmit;
    private TextView tvDate, tvTotal, tv;
    private LinearLayout llShowMore;
    private Spinner spLeads;
    private RelativeLayout rlDate;
    private DashboardModel dashboardModel;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadsView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead);
        setUpView(leadsView);
        hideAppTitle(getClass().toString());
        return leadsView;
    }

    public static TeamLeadsFragment newInstance(Context context) {
        TeamLeadsFragment fragment = new TeamLeadsFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadsView) {
        rlDate = (RelativeLayout) leadsView.findViewById(R.id.rlDate);
        spLeads = (Spinner) leadsView.findViewById(R.id.spLeads);
        mChart = (HorizontalBarChart) leadsView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);

        llShowMore = (LinearLayout) leadsView.findViewById(R.id.llShowMore);
        tvDate = (TextView) leadsView.findViewById(R.id.tvDate);
        tvStartDate = (TextView) leadsView.findViewById(R.id.tvStartDate);
        tvTotal = (TextView) leadsView.findViewById(R.id.tvTotal);
        tv = (TextView) leadsView.findViewById(R.id.tv);
        // tvEndDate = (TextView) leadsView.findViewById(R.id.tvEndDate);
        mainFromDate = CRMAppUtil.getFirstDateOfCurrentMonth("yyyy-MM-dd");
        fromDat = CRMAppUtil.getDate(mainFromDate, "yyyy-MM-dd");
        fromPostingDate = CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy");
        mainToDate = CRMAppUtil.getDate(new Date(), "yyyy-MM-dd");
        toDate = CRMAppUtil.getDate(mainToDate, "yyyy-MM-dd");
        toPostingDate = CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy");
        tvStartDate.setText(CRMAppUtil.getDateStr(fromDat, "dd-MM-yyyy"));
        rlDate.setOnClickListener(this);
        /*tvEndDate.setText(CRMAppUtil.getDateStr(toDate, "dd-MM-yyyy"));
        tvEndDate.setOnClickListener(this);*/
        llShowMore.setOnClickListener(this);
        // tvDate.setText("As on 31 December,2016");

        // draw bars behind lines
       /* mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE,
CombinedChart.DrawOrder.SCATTER
        });*/

        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);


        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        //  mChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(false);
        mChart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = mChart.getAxisLeft();
        //  leftAxis.setValueFormatter(new MyAxisValueFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        dashboardModel = (DashboardModel) this.getArguments().getSerializable(TEAM_LEAD);
        List<WidgetModel> actionableWidgetList = dashboardModel.getGroupModel(TEAM_LEAD).getWidgetList();
        for (WidgetModel model : actionableWidgetList) {
            webServiceCall(model.getSystemName());
        }

        //  stackedBarData();
    }

    private void webServiceCall(String systemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", systemName);

            //dashboard for
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    tv.setVisibility(View.VISIBLE);
                                    llShowMore.setVisibility(View.VISIBLE);
                                    hideProgress();
                                    JSONObject responseJsonObj = response.getJSONObject("Response");
                                    JSONArray arrayDetail = responseJsonObj.getJSONArray("Table");
                                    ArrayList<String> columnsYAxis = new ArrayList<String>();
                                    int total = 0;
                                    // ArrayList<String> columnsXAxis = new ArrayList<String>();
                                    final ArrayList<String> xAxisList = new ArrayList<String>();
                                    final ArrayList<String> yAxisListFirst = new ArrayList<String>();
                                    final ArrayList<String> yAxisListsecond = new ArrayList<String>();

                                    if (arrayDetail.length() != 0) {
                                        for (int y = 2; y < arrayDetail.length(); y++) {
                                            JSONObject objDetail = (JSONObject) arrayDetail.get(y);
                                            String Total = objDetail.getString("Total").equals("") ? "0" : objDetail.getString("Total");
                                            try {
                                                total += Integer.parseInt(Total);
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        JSONObject objDetailFirst = (JSONObject) arrayDetail.get(1);
                                        if (columnsYAxis.size() == 0) {
                                            Iterator iter = objDetailFirst.keys();
                                            while (iter.hasNext()) {
                                                String columnName = iter.next().toString();
                                                columnsYAxis.add(columnName);
                                            }
                                            columnsYAxis.remove("id");
                                            columnsYAxis.remove("Total");
                                            columnsYAxis.remove("stage");
                                            for (String column : columnsYAxis) {
                                                String columnData = objDetailFirst.getString(column).equals("") ? "" : objDetailFirst.getString(column);
                                                xAxisList.add(columnData);
                                            }

                                        }

                                        JSONObject objDetailYFirst = (JSONObject) arrayDetail.get(2);
                                        JSONObject objDetailYSecond = (JSONObject) arrayDetail.get(5);

                                        if (columnsYAxis.size() == 0) {
                                            Iterator iter = objDetailYFirst.keys();
                                            while (iter.hasNext()) {
                                                String columnName = iter.next().toString();
                                                if (!columnName.equalsIgnoreCase("Id")) {
                                                    columnsYAxis.add(columnName);
                                                }
                                            }
                                        }
                                        columnsYAxis.remove("id");
                                        columnsYAxis.remove("Total");
                                        columnsYAxis.remove("stage");
                                        for (String column : columnsYAxis) {
                                            String columnData = objDetailYFirst.getString(column).equals("") ? "0" : objDetailYFirst.getString(column);
                                            yAxisListFirst.add(columnData);
                                        }
                                        for (String column : columnsYAxis) {
                                            String columnData = objDetailYSecond.getString(column).equals("") ? "0" : objDetailYSecond.getString(column);
                                            yAxisListsecond.add(columnData);
                                        }
                                        tvTotal.setText(String.valueOf(total));
                                        stackedBarData(xAxisList, yAxisListFirst, yAxisListsecond);
                                    } else {
                                        hideProgress();
                                        String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                     //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                  //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            hideProgress();
                          //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
//                        customProgress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        try {
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
                              //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
//                        customProgress.setVisibility(View.GONE);
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                    }

                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR+systemName, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }


    private void stackedBarData(final ArrayList<String> xAxisValue, final ArrayList<String> yAxisFirst, final ArrayList<String> yAxisSecond) {
        Collections.reverse(xAxisValue);
        Collections.reverse(yAxisFirst);
        Collections.reverse(yAxisSecond);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setCenterAxisLabels(true);
        //remove axis line
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                try {
                    return xAxisValue.get((int) value % xAxisValue.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < xAxisValue.size(); i++) {
            float val1 = Float.parseFloat(yAxisFirst.get(i));

            float val2 = Float.parseFloat(yAxisSecond.get(i));


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
              /*  Bundle args = new Bundle();
                Log.d("index", Float.toString(sliceIndex + 0.5f));
                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);*/

            }

            @Override
            public void onNothingSelected() {

            }

        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rlDate) {
            selectDate();
        }
       /* if (id == R.id.tvEndDate) {
            selectDate();
        }*/
        if (id == R.id.llShowMore) {
            Bundle args = new Bundle();
            TeamLeadShowFragment clientsFragment = TeamLeadShowFragment.newInstance(getContext());
            args.putSerializable(TEAM_LEAD, dashboardModel);
            clientsFragment.setArguments(args);
         //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceTeamFragment, clientsFragment).commit();
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
