package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.mosl.tabapp.common.FilterRowData.getSpinnerFilterDataByValue;

//import com.squareup.moshi.Moshi;

//import com.squareup.moshi.Moshi;

/**
 * Created by raju on 2/14/2017.
 */

public class ClientsLeadsFragment extends MOSLCommonFragment implements View.OnClickListener {
    private View leadsView;
    static Context mContext;
    private HorizontalBarChart mChart;
    private TextView tvTotal, tv, tvShowMore;
    private LinearLayout llShowMore;
    private Spinner spFilter1, spFilter2, spFilter3;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private Map addtionalParameterMap = new HashMap<>();
    private String filterFirst = "", filterSecond = "", filterThird = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        leadsView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_lead);
        setUpView(leadsView);
        hideAppTitle(getClass().toString());
        return leadsView;
    }

    public static ClientsLeadsFragment newInstance(Context context) {
        ClientsLeadsFragment fragment = new ClientsLeadsFragment();
        Bundle args = new Bundle();
        mContext = context;
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpView(View leadsView) {
        spFilter1 = (Spinner) leadsView.findViewById(R.id.spFilter1);
        spFilter1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterFirst = ((FilterRowData) spFilter1.getSelectedItem()).getValue();
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spFilter2 = (Spinner) leadsView.findViewById(R.id.spFilter2);
        spFilter2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterSecond = ((FilterRowData) spFilter2.getSelectedItem()).getValue();
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spFilter3 = (Spinner) leadsView.findViewById(R.id.spFilter3);
        spFilter3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filterThird = ((FilterRowData) spFilter3.getSelectedItem()).getValue();
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        try {
            filterFirst = this.getArguments().getString(FILTERFIRST);
            filterSecond = this.getArguments().getString(FILTERSECOND);
            filterThird = this.getArguments().getString(FILTERTHIRD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mChart = (HorizontalBarChart) leadsView.findViewById(R.id.chart1);
        mChart.getDescription().setEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setHighlightFullBarEnabled(false);


        llShowMore = (LinearLayout) leadsView.findViewById(R.id.llShowMore);
        tvTotal = (TextView) leadsView.findViewById(R.id.tvTotal);
        tvShowMore = (TextView) leadsView.findViewById(R.id.tvShowMore);
        tvShowMore.setPaintFlags(tvShowMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv = (TextView) leadsView.findViewById(R.id.tv);

        llShowMore.setOnClickListener(this);

        Legend l = mChart.getLegend();
        l.setTextSize(getResources().getDimension(R.dimen.chart_text_size));
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
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
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
        dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);

        if (dashboardModel != null) {
            DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getLeadSystemName());
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
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);

                    FilterRowData productDataFirst = getSpinnerFilterDataByValue(filterFirst, adapter1);
                    if (productDataFirst != null) {
                        spFilter1.setSelection(productDataFirst.getPosition());
                    }

                    ArrayAdapter<FilterRowData> adapter2 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(1).getFilterData());
                    adapter2.setDropDownViewResource(R.layout.mdropdown);
                    spFilter2.setAdapter(adapter2);
                    spFilter2.setVisibility(View.VISIBLE);

                    FilterRowData productDataSecond = getSpinnerFilterDataByValue(filterSecond, adapter2);
                    if (productDataSecond != null) {
                        spFilter2.setSelection(productDataSecond.getPosition());
                    }
                }
                if (filterList != null && filterList.size() == 3) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);

                    FilterRowData productDataFirst = getSpinnerFilterDataByValue(filterFirst, adapter1);
                    if (productDataFirst != null) {
                        spFilter1.setSelection(productDataFirst.getPosition());
                    }

                    ArrayAdapter<FilterRowData> adapter2 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(1).getFilterData());
                    adapter2.setDropDownViewResource(R.layout.mdropdown);
                    spFilter2.setAdapter(adapter2);

                    FilterRowData productDataSecond = getSpinnerFilterDataByValue(filterSecond, adapter2);
                    if (productDataSecond != null) {
                        spFilter2.setSelection(productDataSecond.getPosition());
                    }

                    ArrayAdapter<FilterRowData> adapter3 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(2).getFilterData());
                    adapter3.setDropDownViewResource(R.layout.mdropdown);
                    spFilter3.setAdapter(adapter3);

                    FilterRowData productDataThird = getSpinnerFilterDataByValue(filterThird, adapter3);
                    if (productDataThird != null) {
                        spFilter3.setSelection(productDataThird.getPosition());
                    }
                    spFilter2.setVisibility(View.VISIBLE);
                    spFilter3.setVisibility(View.VISIBLE);
                } else if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    spFilter1.setAdapter(adapter1);

                    FilterRowData productDataFirst = getSpinnerFilterDataByValue(filterFirst, adapter1);
                    if (productDataFirst != null) {
                        spFilter1.setSelection(productDataFirst.getPosition());
                    }
                }
            }
        }

        //  stackedBarData();
    }

    private Map getAddtionalParameterMap() {

        Map addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spFilter1.getSelectedItem();
            FilterRowData filter2 = (FilterRowData) spFilter2.getSelectedItem();
            FilterRowData filter3 = (FilterRowData) spFilter3.getSelectedItem();

            if (filter1 != null) {
                addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());

            }
            if (filter2 != null) {
                addtionalParameterMap.put(filter2.getKeyValue(), filter2.getValue());
                filterSecond = filter2.getValue();
            }
            if (filter3 != null) {
                addtionalParameterMap.put(filter3.getKeyValue(), filter3.getValue());
                filterThird = filter3.getValue();
            }
           /* if(dashboardModel.getName().toLowerCase().endsWith("self")){
                addtionalParameterMap.put(SELF_KEY,"SELF");
            }*/
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void webServiceCall() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());


            //for dashboard as: EmployeeCode  TODO
            //inputMap.put("EmployeeCode", EmployeeCode);


            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        try {
                            JSONObject errorResponse = new JSONObject(responseString);
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseS) {
                        //super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {

                            Gson mGson = new Gson();
                            Type type = new TypeToken<HashMap<String, Object>>() {
                            }.getType();
                            HashMap<String, Object> response = mGson.fromJson(responseS, type);
                            //org.json.JSONObject response =new JSONObject(responseS);
                            if (statusCode == 200) {
                                //  String status = response.getString(STATUS);
                                String status = response.get(STATUS).toString();
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    tv.setVisibility(View.VISIBLE);
                                    llShowMore.setVisibility(View.VISIBLE);
                                    hideProgress();

                                    ArrayList<String> columnsYAxis = new ArrayList<String>();
                                    //  ArrayList<String> columnsYAxis = null;
                                    String total = "";
                                    String[] lableString = new String[]{};

                                    final ArrayList<ArrayList<String>> yAxisListFirst = new ArrayList<>();
                                    StringMap responseJsonObj = (StringMap) response.get("Response");
                                    List<StringMap> arrayDetail = (List<StringMap>) responseJsonObj.get("Table");
                                    if (arrayDetail.size() != 0) {
                                        for (int y = 2; y < arrayDetail.size(); y++) {
                                            StringMap objDetail = (StringMap) arrayDetail.get(y);
                                            // String stages = objDetail.getString("stage").equals("") ? "0" : objDetail.getString("stage");
                                            String stages = objDetail.get("stage").toString().equals("") ? "0" : objDetail.get("stage").toString();
                                            if (stages.equalsIgnoreCase("Total")) {
                                                //     String Total = (objDetail.isJsonNull("Total") || objDetail.get("Total").getAsString().equals("")) ? "0" : objDetail.get("Total").getAsString();

                                                String Total = (objDetail.get("Total").toString().equals("")) ? "0" : objDetail.get("Total").toString();
                                                total = Total;
                                            }
                                        }
                                        try {
                                            if (responseJsonObj.get("Table1") != null) {
                                                List<StringMap> arrayDetailTotal = (List<StringMap>) responseJsonObj.get("Table1");
//region gettiong colums
                                                StringMap jobjFKey = arrayDetailTotal.get(0);
                                                for (Object keySetE : jobjFKey.keySet()) {
                                                    if (keySetE.toString().equalsIgnoreCase("id") | keySetE.toString().trim().equalsIgnoreCase("Total")) {
                                                    } else {
                                                        columnsYAxis.add(keySetE.toString());

                                                    }
                                                }
                                                //endregion
                                                //region new gson Impl
                                                for (StringMap jobj : arrayDetailTotal) {

                                                    final ArrayList<String> yAxisList = new ArrayList<String>();

                                                    for (Object keySetE : columnsYAxis) {
                                                        if (keySetE.toString().equalsIgnoreCase("id") || keySetE.toString().equalsIgnoreCase("Total")) {
                                                        } else {
                                                            yAxisList.add(jobj.get(keySetE).toString());
                                                        }

                                                    }
                                                    // yAxisList.remove(0);
                                                    yAxisListFirst.add(yAxisList);
                                                }
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if (yAxisListFirst != null && yAxisListFirst.size() > 0) {
                                            // lableString = new String[]{yAxisListFirst.get(0).get(0), yAxisListFirst.get(1).get(0)};
                                            lableString = new String[]{yAxisListFirst.get(0).get(0)
                                                    , yAxisListFirst.get(1).get(0)
                                            };
                                            yAxisListFirst.get(0).remove(0);
                                            yAxisListFirst.get(1).remove(0);
                                            tvTotal.setText(total);
                                            stackedBarData(columnsYAxis, yAxisListFirst, lableString);
                                        }
                                    } else {
                                        hideProgress();
                                        //  String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(new JSONObject(responseS));
                                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }

                                } else {
                                    hideProgress();
                                    //     String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(new JSONObject(responseS));
                                    MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }

                            } else {
                                hideProgress();
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (
                                ClassCastException ce)

                        {
                            ce.printStackTrace();
                            hideProgress();
                        } catch (
                                Exception e)

                        {
                            e.printStackTrace();
                            hideProgress();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR + widgetSystemName, "OK", true, null);
                        }
                    }


                });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR + widgetSystemName, "OK", true, null);
            }
        } else

        {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }


    private void stackedBarData(final ArrayList<String> xAxisValue, final ArrayList<ArrayList<String>> yAxisFirst, final String[] lableString) {
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
        //remove axis line
        xAxis.setDrawAxisLine(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                try {
                    //sometime value is coming as negative
                    if (value < 0) {
                        return "";
                    } else {
                        return xAxisValue.get((int) value % xAxisValue.size());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

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

        //  colors.add(ColorTemplate.getHoloBlue());
        set1 = new BarDataSet(yVals1, "");
        set1.setColors(colors);
        set1.setStackLabels(lableString);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        //  set1.setFormSize(getResources().getDimension(R.dimen.chart_text_size));
        // set1.setValueTextSize(getResources().getDimension(R.dimen.chart_text_size));
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new CRMValueFormatter());
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(13f);
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
                if (e.getY() == 0.0f) return;
                mChart.highlightValues(null);
                pieChartDetail(e.getX());
                Log.d("Highlited", String.valueOf(h.getStackIndex()));
                Log.d("xValue", xAxisValue.get((int) (e.getX())));
                addtionalParameterMap.put(COL_FILTER, xAxisValue.get((int) (e.getX())));
                addtionalParameterMap.put(ROW_FILTER, lableString[h.getStackIndex()]);
                addtionalParameterMap.putAll(getAddtionalParameterMap());
                Bundle args = new Bundle();
                args.putString(SYSTEM_NAME, widgetSystemName);
                args.putString(LABEL, LEAD);
                // args.putString(LABEL, LEAD+"-"+lableString[h.getStackIndex()]+"("+Float.parseFloat(yAxisFirst.get(1).get((int) (e.getX()))));
                args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                if (lableString[h.getStackIndex()].equalsIgnoreCase("Assigned")) {
                    if (!String.valueOf(Float.parseFloat(yAxisFirst.get(1).get((int) (e.getX())))).equalsIgnoreCase("0.0"))
                        navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                } else {
                    navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                }


            }

            private void pieChartDetail(float sliceIndex) {
               /* Bundle args = new Bundle();
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
        if (id == R.id.llShowMore) {
            Bundle args = new Bundle();
            ClientsLeadShowFragment clientsFragment = ClientsLeadShowFragment.newInstance(getContext());
            args.putSerializable(DASHBOARD_MODEL, dashboardModel);
            args.putSerializable(MAP, (Serializable) getAddtionalParameterMap());
            args.putString(SYSTEM_NAME, widgetSystemName);
            args.putString(FILTERFIRST, filterFirst);
            args.putString(FILTERSECOND, filterSecond);
            args.putString(FILTERTHIRD, filterThird);
            clientsFragment.setArguments(args);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceClientFragment, clientsFragment).commit();
        }
    }


}
