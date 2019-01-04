package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.common.LockedPieChart;
import com.mosl.tabapp.model.TargetAchievementModel;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.model.dashboard.WidgetType;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by raju on 2/20/2017.
 */

public class TargetAchievementTeamFragment extends MOSLCommonFragment
    implements View.OnClickListener {
    static DashboardFragment parent;
    private View targetView;
    private TextView txtRightValue, txtRightStr, txtCenterValue, txtCenterStr, txtLeftStr,
        txtLeftValue, tvUniqueLeadsFirst, tvUniqueLeadsSecond, tvUniqueLeadsThird;
    private LockedPieChart pieChartFirst, pieChartSecond, pieChartThird;
    private ArrayList<String> xdataTargetArrFirst = new ArrayList<>();
    private ArrayList<String> xdataTargetArrSecond = new ArrayList<>();
    private ArrayList<String> xdataTargetArrThird = new ArrayList<>();
    private Typeface tf;
    private ImageView ivView, ivRightArrowFirst, ivLeftArrowFirst, ivRightArrowSecond,
        ivLeftArrowSecond, ivRightArrowThird, ivLeftArrowThird;
    private LinearLayout llFirst, llSecond, llThird, llTopFirst;
    private List<DashboardGroupModel> widgetModelList;
    private List<WidgetModel> subWidgetModelList;
    static Context mContext;
    private RecyclerView horizontalRecyclerView;
    private HorizontalAdapter horizontalAdapter;
    private int[] VORDIPLOM_COLORS = {
        Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
        Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46), Color.rgb(255, 21, 0), Color.rgb(38, 127, 183),
        Color.rgb(0, 177, 207),
        Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
        Color.rgb(254, 46, 46)
    };
    private RecyclerView verticalRecyclerViewFirst;
    private RecyclerView verticalRecyclerViewSecond;
    private RecyclerView verticalRecyclerViewThird;
    private VerticalAdapterFirst verticalAdapterFirst;
    private VerticalAdapterSecond verticalAdapterSecond;
    private VerticalAdapterThird verticalAdapterThird;
    private ArrayList<TargetAchievementModel> targetAchievementModelsFirst;
    private ArrayList<TargetAchievementModel> targetAchievementModelsSecond;
    private ArrayList<TargetAchievementModel> targetAchievementModelsThird;
    private TextView tvErrorFirst, tvErrorSecond, tvErrorThird;
    private Spinner spTargetVsAchivementFilter;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private String leftStr, rightStr, centerStr, widgetName;
    private Map addtionalParameterMap = new HashMap<>();
    private LinearLayout linearLayoutRight, linearLayoutCenter, linearLayoutLeft;
    private List<WidgetType> widgetTypeList = new ArrayList<>();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        targetView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_target_achievement_team);
        setUpView(targetView);

        return targetView;
    }

    public static TargetAchievementTeamFragment newInstance(Context context) {
        TargetAchievementTeamFragment fragment = new TargetAchievementTeamFragment();
        return fragment;
    }

    public void setUpView(View targetView) {
        widgetModelList = new ArrayList<>();
        try {
            dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
            subWidgetModelList =
                dashboardModel.getGroupModel(DashboardSystemModel.getTargetAchivementSystemName())
                    .getWidgetList();
            if (subWidgetModelList.size() < 3) {
                subWidgetModelList.add(new WidgetModel());
                subWidgetModelList.add(new WidgetModel());
            } else if (subWidgetModelList.size() < 4) {
                subWidgetModelList.add(new WidgetModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        horizontalRecyclerView =
            (RecyclerView) targetView.findViewById(R.id.horizontalRecyclerView);
        llFirst = (LinearLayout) targetView.findViewById(R.id.llFirst);
        llTopFirst = (LinearLayout) targetView.findViewById(R.id.llTopFirst);
        llSecond = (LinearLayout) targetView.findViewById(R.id.llSecond);
        llThird = (LinearLayout) targetView.findViewById(R.id.llThird);
        linearLayoutLeft = (LinearLayout) targetView.findViewById(R.id.linearLayoutLeft);
        linearLayoutCenter = (LinearLayout) targetView.findViewById(R.id.linearLayoutCenter);
        linearLayoutRight = (LinearLayout) targetView.findViewById(R.id.linearLayoutRight);

        ivView = (ImageView) targetView.findViewById(R.id.ivView);
        ivRightArrowFirst = (ImageView) targetView.findViewById(R.id.ivRightArrowFirst);
        ivLeftArrowFirst = (ImageView) targetView.findViewById(R.id.ivLeftArrowFirst);
        ivRightArrowSecond = (ImageView) targetView.findViewById(R.id.ivRightArrowSecond);
        ivLeftArrowSecond = (ImageView) targetView.findViewById(R.id.ivLeftArrowSecond);
        ivRightArrowThird = (ImageView) targetView.findViewById(R.id.ivRightArrowThird);
        ivLeftArrowThird = (ImageView) targetView.findViewById(R.id.ivLeftArrowThird);
        ivLeftArrowFirst.setOnClickListener(this);
        ivRightArrowFirst.setOnClickListener(this);
        ivRightArrowSecond.setOnClickListener(this);
        ivLeftArrowSecond.setOnClickListener(this);
        ivRightArrowThird.setOnClickListener(this);
        ivLeftArrowThird.setOnClickListener(this);
        linearLayoutRight.setOnClickListener(this);
        linearLayoutCenter.setOnClickListener(this);
        linearLayoutLeft.setOnClickListener(this);

        txtRightValue = (TextView) targetView.findViewById(R.id.txtRightValue);
        txtRightStr = (TextView) targetView.findViewById(R.id.txtRightStr);
        txtCenterValue = (TextView) targetView.findViewById(R.id.txtCenterValue);
        txtCenterStr = (TextView) targetView.findViewById(R.id.txtCenterStr);
        txtLeftStr = (TextView) targetView.findViewById(R.id.txtLeftStr);
        txtLeftValue = (TextView) targetView.findViewById(R.id.txtLeftValue);
        tvUniqueLeadsFirst = (TextView) targetView.findViewById(R.id.tvUniqueLeadsFirst);
        tvErrorFirst = (TextView) targetView.findViewById(R.id.tvErrorFirst);
        tvErrorSecond = (TextView) targetView.findViewById(R.id.tvErrorSecond);
        tvErrorThird = (TextView) targetView.findViewById(R.id.tvErrorThird);
        verticalRecyclerViewFirst =
            (RecyclerView) targetView.findViewById(R.id.verticalRecyclerViewFirst);
        verticalRecyclerViewSecond =
            (RecyclerView) targetView.findViewById(R.id.verticalRecyclerViewSecond);
        verticalRecyclerViewThird =
            (RecyclerView) targetView.findViewById(R.id.verticalRecyclerViewThird);
        spTargetVsAchivementFilter =
            (Spinner) targetView.findViewById(R.id.spTargetVsAchivementFilter);

        tvUniqueLeadsSecond = (TextView) targetView.findViewById(R.id.tvUniqueLeadsSecond);
        tvUniqueLeadsThird = (TextView) targetView.findViewById(R.id.tvUniqueLeadsThird);

        pieChartFirst = (LockedPieChart) targetView.findViewById(R.id.pieChartFirst);
        pieChartSecond = (LockedPieChart) targetView.findViewById(R.id.pieChartSecond);
        pieChartThird = (LockedPieChart) targetView.findViewById(R.id.pieChartThird);

        //Stopping rotation
        pieChartFirst.setRotationEnabled(false);
        pieChartSecond.setRotationEnabled(false);
        pieChartThird.setRotationEnabled(false);
        //Stopping rotation end

        DashboardGroupModel groupModel =
            dashboardModel.getGroupModel(DashboardSystemModel.getTargetAchivementSystemName());
        //set filter data
        List<WidgetFilterModel> filterList = groupModel.getGroupFilterList();
        if (filterList != null && filterList.size() == 1) {
            ArrayAdapter<FilterRowData> adapter1 =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                    filterList.get(0).getFilterData());
            adapter1.setDropDownViewResource(R.layout.mdropdown);
            spTargetVsAchivementFilter.setAdapter(adapter1);
        }
        spTargetVsAchivementFilter.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                    int position, long id) {
                    webServiceCallFirst();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });

        horizontalAdapter = new HorizontalAdapter(subWidgetModelList);
        LinearLayoutManager horizontalLayoutManagaer =
            new LinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();
    }

    private void populatePieChartFirst(ArrayList<TargetAchievementModel> targetAchievementModels) {
        Description des = pieChartFirst.getDescription();
        des.setEnabled(false);
        pieChartFirst.setUsePercentValues(true);
        pieChartFirst.setExtraOffsets(5, 10, 5, 5);
        pieChartFirst.setDragDecelerationFrictionCoef(0.85f);
        pieChartFirst.getLegend().setEnabled(false);
        // enable hole and configure
        pieChartFirst.setDrawHoleEnabled(true);
        pieChartFirst.setTransparentCircleColor(Color.WHITE);
        pieChartFirst.setTransparentCircleAlpha(100);
        //pieChartFirst.setHoleColor(R.color.counter_text_bg);
        pieChartFirst.setHoleRadius(70f);
        pieChartFirst.setTransparentCircleRadius(30f);

        pieChartFirst.setDrawCenterText(true);
        pieChartFirst.setDrawSliceText(false);

        pieChartFirst.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChartFirst.setRotationEnabled(true);
        // totalClientPieChart.setOnChartValueSelectedListener(this);

        setDataFirst(targetAchievementModels);

        pieChartFirst.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataFirst(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                targetAchievementModels.get(i).getLabel(), i));

        ArrayList<String> xVals1 = new ArrayList<String>();

        for (int i = 0; i < xdataTargetArrFirst.size(); i++)
            xVals1.add(xdataTargetArrFirst.get(i));
        // add a lot of colors

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Client");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //  data.setValueFormatter(new PercentFormatter());

        data.setDrawValues(false);     // for text hidden
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        pieChartFirst.setData(data);

        // undo all highlights
        pieChartFirst.highlightValues(null);

        pieChartFirst.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                addtionalParameterMap.clear();
                pieChartDetail(e.getX());
                Log.d("Highlited", String.valueOf(h.getX()));
                Log.d("xValue", String.valueOf((int) (e.getX())));
                addtionalParameterMap.put(KEY,
                    targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                    targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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

        pieChartFirst.invalidate();
    }

    private void populatePieChartSecond(ArrayList<TargetAchievementModel> targetAchievementModels) {
        Description des = pieChartSecond.getDescription();
        des.setEnabled(false);
        pieChartSecond.setUsePercentValues(true);
        pieChartSecond.setExtraOffsets(5, 10, 5, 5);
        pieChartSecond.setDragDecelerationFrictionCoef(0.85f);
        pieChartSecond.getLegend().setEnabled(false);
        // enable hole and configure
        pieChartSecond.setDrawHoleEnabled(true);
        pieChartSecond.setTransparentCircleColor(Color.WHITE);
        pieChartSecond.setTransparentCircleAlpha(100);
        //pieChartFirst.setHoleColor(R.color.counter_text_bg);
        pieChartSecond.setHoleRadius(70f);
        pieChartSecond.setTransparentCircleRadius(30f);

        pieChartSecond.setDrawCenterText(true);
        pieChartSecond.setDrawSliceText(false);

        pieChartSecond.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChartSecond.setRotationEnabled(true);
        // totalClientPieChart.setOnChartValueSelectedListener(this);

        setDataSecond(targetAchievementModels);

        pieChartSecond.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataSecond(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                targetAchievementModels.get(i).getLabel(), i));

        ArrayList<String> xVals1 = new ArrayList<String>();

        for (int i = 0; i < xdataTargetArrSecond.size(); i++)
            xVals1.add(xdataTargetArrSecond.get(i));
        // add a lot of colors

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Client");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //  data.setValueFormatter(new PercentFormatter());

        data.setDrawValues(false);     // for text hidden
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        pieChartSecond.setData(data);

        // undo all highlights
        pieChartSecond.highlightValues(null);

        pieChartSecond.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                addtionalParameterMap.clear();
                pieChartDetail(e.getX());
                Log.d("Highlited", String.valueOf(h.getX()));
                Log.d("xValue", String.valueOf((int) (e.getX())));
                addtionalParameterMap.put(KEY,
                    targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                    targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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

        pieChartSecond.invalidate();
    }

    private void populatePieChartThird(
        final ArrayList<TargetAchievementModel> targetAchievementModels) {
        Description des = pieChartThird.getDescription();
        des.setEnabled(false);
        pieChartThird.setUsePercentValues(true);
        pieChartThird.setExtraOffsets(5, 10, 5, 5);
        pieChartThird.setDragDecelerationFrictionCoef(0.85f);
        pieChartThird.getLegend().setEnabled(false);
        // enable hole and configure
        pieChartThird.setDrawHoleEnabled(true);
        pieChartThird.setTransparentCircleColor(Color.WHITE);
        pieChartThird.setTransparentCircleAlpha(100);
        //pieChartFirst.setHoleColor(R.color.counter_text_bg);
        pieChartThird.setHoleRadius(70f);
        pieChartThird.setTransparentCircleRadius(30f);

        pieChartThird.setDrawCenterText(true);
        pieChartThird.setDrawSliceText(false);

        pieChartThird.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChartThird.setRotationEnabled(true);
        // totalClientPieChart.setOnChartValueSelectedListener(this);

        setDataThird(targetAchievementModels);

        pieChartThird.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataThird(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                targetAchievementModels.get(i).getLabel(), i));

        ArrayList<String> xVals1 = new ArrayList<String>();

        for (int i = 0; i < xdataTargetArrThird.size(); i++)
            xVals1.add(xdataTargetArrThird.get(i));
        // add a lot of colors

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Client");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        //  data.setValueFormatter(new PercentFormatter());

        data.setDrawValues(false);     // for text hidden
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        pieChartThird.setData(data);

        // undo all highlights
        pieChartThird.highlightValues(null);

        pieChartThird.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                addtionalParameterMap.clear();
                pieChartDetail(e.getX());
                Log.d("Highlited", String.valueOf(h.getX()));
                Log.d("xValue", String.valueOf((int) (e.getX())));
                addtionalParameterMap.put(KEY,
                    targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                    targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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

        pieChartThird.invalidate();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivLeftArrowFirst) {
           /* llSecond.setVisibility(View.GONE);
            llFirst.setVisibility(View.GONE);
            llThird.setVisibility(View.VISIBLE);*/
        }
        if (id == R.id.ivRightArrowFirst) {
            llThird.setVisibility(View.GONE);
            llFirst.setVisibility(View.GONE);
            llSecond.setVisibility(View.VISIBLE);
        }
        if (id == R.id.ivRightArrowSecond) {
            llFirst.setVisibility(View.GONE);
            llSecond.setVisibility(View.GONE);
            llThird.setVisibility(View.VISIBLE);
        }
        if (id == R.id.ivLeftArrowSecond) {
            llThird.setVisibility(View.GONE);
            llFirst.setVisibility(View.VISIBLE);
            llSecond.setVisibility(View.GONE);
        }
        if (id == R.id.ivRightArrowThird) {
          /*  llSecond.setVisibility(View.GONE);
            llFirst.setVisibility(View.VISIBLE);
            llThird.setVisibility(View.GONE);*/
        }
        if (id == R.id.ivLeftArrowThird) {
            llFirst.setVisibility(View.GONE);
            llSecond.setVisibility(View.VISIBLE);
            llThird.setVisibility(View.GONE);
        }

        if (id == R.id.linearLayoutCenter) {
            // addtionalParameterMap.put(WIDGETTYPE, widgetName.concat("_" + achievedKey.toLowerCase()));
            addtionalParameterMap.clear();
            addtionalParameterMap.put(WIDGETTYPE, centerStr);
            try {
                if (Double.parseDouble(txtCenterValue.getText().toString().trim()) != 0) {
                    secondClickTarget(addtionalParameterMap);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.linearLayoutRight) {
            // addtionalParameterMap.put(WIDGETTYPE, uniqueStr);
            addtionalParameterMap.clear();
            addtionalParameterMap.put(WIDGETTYPE, rightStr);
            try {
                if (Double.parseDouble(txtRightValue.getText().toString().trim()) != 0) {
                    secondClickTarget(addtionalParameterMap);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.linearLayoutLeft) {
            if (!leftStr.equalsIgnoreCase("Target")) {
                //  addtionalParameterMap.put(WIDGETTYPE, widgetName.concat("_" + targetKey.toLowerCase()));
                addtionalParameterMap.clear();
                addtionalParameterMap.put(WIDGETTYPE, leftStr);
                try {
                    if (Double.parseDouble(txtLeftValue.getText().toString().trim()) != 0) {
                        secondClickTarget(addtionalParameterMap);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void secondClickTarget(Map addtionalParameterMap) {
        addtionalParameterMap.putAll(getAddtionalParameterMap());
        Bundle args = new Bundle();
        args.putString(SYSTEM_NAME, widgetSystemName);
        args.putString(LABEL, TEAM_TARGET_TITLE + "-" + widgetName);
        args.putSerializable(MAP, (Serializable) addtionalParameterMap);
        navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
    }

    private Map getAddtionalParameterMap() {

        Map addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
            if (filter1 != null) {
                addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());
            }
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
           /* if (dashboardModel.getName().toLowerCase().endsWith("self")) {
                addtionalParameterMap.put(SELF_KEY, "SELF");
            }*/

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private List<TargetAchievementModel> getTargetAchievementModel(JSONArray arr) {
        if (arr == null || arr.length() == 0) return null;

        List<TargetAchievementModel> list = new ArrayList<>();

        for (int ctr = 0; ctr < arr.length(); ctr++) {
            try {
                JSONObject jsonObject = arr.getJSONObject(ctr);
                if (jsonObject == null) return null;

                String value =
                    (jsonObject.isNull("value") || jsonObject.getString("value").equals("")) ? "0"
                        : jsonObject.getString("value");
                String label =
                    (jsonObject.isNull("label") || jsonObject.getString("label").equals("")) ? "0"
                        : jsonObject.getString("label");
                String formatted =
                    (jsonObject.isNull("formatted") || jsonObject.getString("formatted").equals(""))
                        ? "0" : jsonObject.getString("formatted");
                String widgetType =
                    (jsonObject.isNull("WidgetType") || jsonObject.getString("WidgetType")
                        .equals("")) ? "0" : jsonObject.getString("WidgetType");

                //  int formattedInt = new Double(value).intValue();

                list.add(new TargetAchievementModel(label, formatted, value,
                    new WidgetType(widgetType, null)));
            } catch (Exception ex) {

            }
        }
        return list;
    }

    private List<TargetAchievementModel> getDataByWidgetType(
        Map<String, List<TargetAchievementModel>> dataMap, WidgetType widgetType) {
        Set<String> keys = dataMap.keySet();

        for (String tableName : keys) {
            List<TargetAchievementModel> list = dataMap.get(tableName);
            if (list == null) continue;

            for (TargetAchievementModel model : list) {
                if (model.getWidgetType().getWidgetTypes().equals(widgetType.getWidgetTypes())) {
                    return list;
                }
            }
        }
        return null;
    }

    private void webServiceCallFirst() {
        llSecond.setVisibility(View.GONE);
        llFirst.setVisibility(View.VISIBLE);
        llThird.setVisibility(View.GONE);
        targetAchievementModelsFirst = new ArrayList<>();
        xdataTargetArrFirst.clear();
        targetAchievementModelsSecond = new ArrayList<>();
        xdataTargetArrSecond.clear();
        targetAchievementModelsThird = new ArrayList<>();
        xdataTargetArrThird.clear();
        tvUniqueLeadsFirst.setText("");
        tvUniqueLeadsSecond.setText("");
        tvUniqueLeadsThird.setText("");
        tvErrorFirst.setVisibility(View.GONE);
        tvErrorSecond.setVisibility(View.GONE);
        tvErrorThird.setVisibility(View.GONE);
        populatePieChartFirst(targetAchievementModelsFirst);
        populatePieChartSecond(targetAchievementModelsSecond);
        populatePieChartThird(targetAchievementModelsThird);
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", widgetSystemName);
            inputMap.put("AddtionalParameters", getAddtionalParameterMap());

            //dashboard for
            inputMap.put("BusinessProfileId", dashboardModel.getBusinessProfileIdForDashboard());
            inputMap.put(PRODUCT_ID, dashboardModel.getProductId());
            inputMap.put(EMPLOYEE_CODE, dashboardModel.getEmployeeCode());

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap,
                    new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            //after success code goes here
                            try {
                                if (statusCode == 200) {
                                    String status = response.getString(STATUS);
                                    int j = 0;
                                    if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase(
                                        "true")) {
                                        hideProgress();
                                   /* "value" : 0,
                                            "label" : Unique Meeting,
                                    "formatted" : 0.00%*/
                                        targetAchievementModelsSecond.clear();
                                        targetAchievementModelsFirst.clear();
                                        targetAchievementModelsThird.clear();
                                        JSONObject responseJsonObj =
                                            response.getJSONObject("Response");
                                        if (!responseJsonObj.isNull("Table3")) {
                                            JSONArray tableArray =
                                                responseJsonObj.getJSONArray("Table3");
                                            int arrayLength = 0;
                                            if (tableArray.length() == 5) {
                                                arrayLength = 3;
                                            } else {
                                                arrayLength = tableArray.length();
                                            }
                                            if (tableArray.length() != 0 && tableArray != null) {
                                                ivView.setVisibility(View.VISIBLE);
                                                llTopFirst.setVisibility(View.VISIBLE);
                                                for (int i = 0; i < arrayLength; i++) {
                                                    if (arrayLength == 3) {
                                                        linearLayoutLeft.setVisibility(
                                                            View.VISIBLE);
                                                        linearLayoutRight.setVisibility(
                                                            View.VISIBLE);
                                                        linearLayoutCenter.setVisibility(
                                                            View.VISIBLE);
                                                        JSONObject getJsonObject =
                                                            tableArray.getJSONObject(i);
                                                        String displayText =
                                                            (getJsonObject.isNull("DisplayText")
                                                                || getJsonObject.getString(
                                                                "DisplayText").equals("")) ? ""
                                                                : getJsonObject.getString(
                                                                    "DisplayText");
                                                        String value =
                                                            (getJsonObject.isNull("Value")
                                                                || getJsonObject.getString("Value")
                                                                .equals("")) ? "0"
                                                                : getJsonObject.getString("Value");
                                                        int ord = getJsonObject.isNull("Ord") ? 0
                                                            : getJsonObject.getInt("Ord");
                                                        if (ord == 1) {
                                                            leftStr = displayText;
                                                            if (!displayText.equalsIgnoreCase(
                                                                "Target")) {
                                                                txtLeftStr.setText(displayText);
                                                            } else {
                                                                FilterRowData filter1 =
                                                                    (FilterRowData) spTargetVsAchivementFilter
                                                                        .getSelectedItem();
                                                                if (filter1.getValue()
                                                                    .equalsIgnoreCase("DAY")
                                                                    || filter1.getValue()
                                                                    .equalsIgnoreCase("WTD")) {
                                                                    txtLeftStr.setText(
                                                                        "MTD " + displayText);
                                                                } else {
                                                                    txtLeftStr.setText(displayText);
                                                                }
                                                            }
                                                            txtLeftValue.setText(value);
                                                        } else if (ord == 2) {
                                                            centerStr = displayText;
                                                            txtCenterStr.setText(displayText);
                                                            txtCenterValue.setText(value);
                                                        } else if (ord == 3) {
                                                            rightStr = displayText;
                                                            txtRightStr.setText(displayText);
                                                            txtRightValue.setText(value);
                                                        }
                                                    } else if (tableArray.length() == 2) {
                                                        linearLayoutRight.setVisibility(View.GONE);
                                                        linearLayoutCenter.setVisibility(
                                                            View.VISIBLE);
                                                        linearLayoutLeft.setVisibility(
                                                            View.VISIBLE);
                                                        JSONObject getJsonObject =
                                                            tableArray.getJSONObject(i);
                                                        String displayText =
                                                            (getJsonObject.isNull("DisplayText")
                                                                || getJsonObject.getString(
                                                                "DisplayText").equals("")) ? ""
                                                                : getJsonObject.getString(
                                                                    "DisplayText");
                                                        String value =
                                                            (getJsonObject.isNull("Value")
                                                                || getJsonObject.getString("Value")
                                                                .equals("")) ? ""
                                                                : getJsonObject.getString("Value");
                                                        int ord = getJsonObject.isNull("Ord") ? 0
                                                            : getJsonObject.getInt("Ord");
                                                        if (ord == 1) {
                                                            leftStr = displayText;
                                                            if (!displayText.equalsIgnoreCase(
                                                                "Target")) {
                                                                txtLeftStr.setText(displayText);
                                                            } else {
                                                                FilterRowData filter1 =
                                                                    (FilterRowData) spTargetVsAchivementFilter
                                                                        .getSelectedItem();
                                                                if (filter1.getValue()
                                                                    .equalsIgnoreCase("DAY")
                                                                    || filter1.getValue()
                                                                    .equalsIgnoreCase("WTD")) {
                                                                    txtLeftStr.setText(
                                                                        "MTD " + displayText);
                                                                } else {
                                                                    txtLeftStr.setText(displayText);
                                                                }
                                                            }
                                                            txtLeftValue.setText(value);
                                                        } else if (ord == 2) {
                                                            centerStr = displayText;
                                                            txtCenterStr.setText(displayText);
                                                            txtCenterValue.setText(value);
                                                        }
                                                    } else if (tableArray.length() == 1) {
                                                        linearLayoutRight.setVisibility(View.GONE);
                                                        linearLayoutCenter.setVisibility(View.GONE);
                                                        linearLayoutLeft.setVisibility(
                                                            View.VISIBLE);
                                                        JSONObject getJsonObject =
                                                            tableArray.getJSONObject(i);
                                                        String displayText =
                                                            (getJsonObject.isNull("DisplayText")
                                                                || getJsonObject.getString(
                                                                "DisplayText").equals("")) ? ""
                                                                : getJsonObject.getString(
                                                                    "DisplayText");
                                                        String value =
                                                            (getJsonObject.isNull("Value")
                                                                || getJsonObject.getString("Value")
                                                                .equals("")) ? ""
                                                                : getJsonObject.getString("Value");
                                                        int ord = getJsonObject.isNull("Ord") ? 0
                                                            : getJsonObject.getInt("Ord");
                                                        if (ord == 1) {
                                                            leftStr = displayText;
                                                            if (!displayText.equalsIgnoreCase(
                                                                "Target")) {
                                                                txtLeftStr.setText(displayText);
                                                            } else {
                                                                FilterRowData filter1 =
                                                                    (FilterRowData) spTargetVsAchivementFilter
                                                                        .getSelectedItem();
                                                                if (filter1.getValue()
                                                                    .equalsIgnoreCase("DAY")
                                                                    || filter1.getValue()
                                                                    .equalsIgnoreCase("WTD")) {
                                                                    txtLeftStr.setText(
                                                                        "MTD " + displayText);
                                                                } else {
                                                                    txtLeftStr.setText(displayText);
                                                                }
                                                            }
                                                            txtLeftValue.setText(value);
                                                        } /*else if (ord == 2) {
                                                        centerStr = displayText;
                                                        txtCenterStr.setText(displayText);
                                                        txtCenterValue.setText(value);
                                                    }*/
                                                    }
                                                }
                                            }
                                        }

                                   /* if (!responseJsonObj.isNull("Table4")) {
                                        JSONArray tableArrayTargetWidget = responseJsonObj.getJSONArray("Table4");
                                        if (tableArrayTargetWidget.length() != 0 && tableArrayTargetWidget != null) {
                                            llTopFirst.setVisibility(View.VISIBLE);
                                            ivView.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < tableArrayTargetWidget.length(); i++) {
                                                JSONObject getJsonObject = tableArrayTargetWidget.getJSONObject(0);
                                                String target = (getJsonObject.isNull("Target") || getJsonObject.getString("Target").equals("")) ? "0" : getJsonObject.getString("Target");
                                                String achieved = (getJsonObject.isNull("Achieved") || getJsonObject.getString("Achieved").equals("")) ? "0" : getJsonObject.getString("Achieved");
                                                String unique = (getJsonObject.isNull("Unique") || getJsonObject.getString("Unique").equals("")) ? "0" : getJsonObject.getString("Unique");
                                                targetStr = target;
                                                widgetName = achieved;
                                                uniqueStr = unique;
                                            }
                                        }
                                    }*/

                                        Map<String, List<TargetAchievementModel>> dataMap =
                                            new HashMap();
                                        JSONArray tableDataArray =
                                            responseJsonObj.getJSONArray("Table");
                                        JSONArray tableDataArray1 =
                                            responseJsonObj.getJSONArray("Table1");
                                        JSONArray tableDataArray2 =
                                            responseJsonObj.getJSONArray("Table2");

                                        dataMap.put("Table",
                                            getTargetAchievementModel(tableDataArray));
                                        dataMap.put("Table1",
                                            getTargetAchievementModel(tableDataArray1));
                                        dataMap.put("Table2",
                                            getTargetAchievementModel(tableDataArray2));

                                        //Render Table
                                        List<TargetAchievementModel> tableDataFirst = null;
                                        if (!widgetTypeList.isEmpty()) {
                                            tableDataFirst =
                                                getDataByWidgetType(dataMap, widgetTypeList.get(0));
                                        }

                                        if (tableDataFirst == null || tableDataFirst.isEmpty()) {
                                            tvErrorFirst.setVisibility(View.VISIBLE);
                                            tvUniqueLeadsFirst.setText(
                                                widgetTypeList.get(0).getTitle());
                                            verticalAdapterFirst = new VerticalAdapterFirst(
                                                targetAchievementModelsFirst);
                                            verticalRecyclerViewFirst.setLayoutManager(
                                                new GridLayoutManager(getMainActivity(), 3));
                                            verticalRecyclerViewFirst.setAdapter(
                                                verticalAdapterFirst);
                                            verticalAdapterFirst.notifyDataSetChanged();
                                            populatePieChartFirst(targetAchievementModelsFirst);
                                        } else {
                                            for (TargetAchievementModel model : tableDataFirst) {
                                                xdataTargetArrFirst.add(model.getLabel());
                                                targetAchievementModelsFirst.add(model);
                                                tvUniqueLeadsFirst.setText(
                                                    widgetTypeList.get(0).getTitle());
                                                verticalAdapterFirst = new VerticalAdapterFirst(
                                                    targetAchievementModelsFirst);
                                                verticalRecyclerViewFirst.setLayoutManager(
                                                    new GridLayoutManager(getMainActivity(), 3));
                                                verticalRecyclerViewFirst.setAdapter(
                                                    verticalAdapterFirst);
                                                verticalAdapterFirst.notifyDataSetChanged();
                                                populatePieChartFirst(targetAchievementModelsFirst);
                                            }
                                        }

                                        //List<TargetAchievementModel> tableDataSecond = getDataByWidgetType(dataMap, widgetTypeList.get(1));
                                        List<TargetAchievementModel> tableDataSecond = null;
                                        if (!widgetTypeList.isEmpty()) {
                                            tableDataSecond =
                                                getDataByWidgetType(dataMap, widgetTypeList.get(1));
                                        }

                                        if (tableDataSecond == null || tableDataSecond.isEmpty()) {
                                            tvErrorSecond.setVisibility(View.VISIBLE);
                                            tvUniqueLeadsSecond.setText(
                                                widgetTypeList.get(1).getTitle());
                                            verticalAdapterSecond = new VerticalAdapterSecond(
                                                targetAchievementModelsSecond);
                                            verticalRecyclerViewSecond.setLayoutManager(
                                                new GridLayoutManager(getMainActivity(), 3));
                                            verticalRecyclerViewSecond.setAdapter(
                                                verticalAdapterSecond);
                                            verticalAdapterSecond.notifyDataSetChanged();
                                            populatePieChartSecond(targetAchievementModelsSecond);
                                        } else {
                                            for (TargetAchievementModel model : tableDataSecond) {
                                                xdataTargetArrSecond.add(model.getLabel());
                                                targetAchievementModelsSecond.add(model);
                                                tvUniqueLeadsSecond.setText(
                                                    widgetTypeList.get(1).getTitle());
                                                verticalAdapterSecond = new VerticalAdapterSecond(
                                                    targetAchievementModelsSecond);
                                                verticalRecyclerViewSecond.setLayoutManager(
                                                    new GridLayoutManager(getMainActivity(), 3));
                                                verticalRecyclerViewSecond.setAdapter(
                                                    verticalAdapterSecond);
                                                verticalAdapterSecond.notifyDataSetChanged();
                                                populatePieChartSecond(
                                                    targetAchievementModelsSecond);
                                            }
                                        }

                                        //List<TargetAchievementModel> tableDataThird = getDataByWidgetType(dataMap, widgetTypeList.get(2));
                                        List<TargetAchievementModel> tableDataThird = null;
                                        if (!widgetTypeList.isEmpty()) {
                                            tableDataThird =
                                                getDataByWidgetType(dataMap, widgetTypeList.get(2));
                                        }

                                        if (tableDataThird == null || tableDataThird.isEmpty()) {
                                            tvErrorThird.setVisibility(View.VISIBLE);
                                            tvUniqueLeadsThird.setText(
                                                widgetTypeList.get(2).getTitle());
                                            verticalAdapterThird = new VerticalAdapterThird(
                                                targetAchievementModelsThird);
                                            verticalRecyclerViewThird.setLayoutManager(
                                                new GridLayoutManager(getMainActivity(), 3));
                                            verticalRecyclerViewThird.setAdapter(
                                                verticalAdapterThird);
                                            verticalAdapterThird.notifyDataSetChanged();
                                            populatePieChartThird(targetAchievementModelsThird);
                                        } else {
                                            for (TargetAchievementModel model : tableDataThird) {
                                                xdataTargetArrThird.add(model.getLabel());
                                                targetAchievementModelsThird.add(model);
                                                tvUniqueLeadsThird.setText(
                                                    widgetTypeList.get(2).getTitle());
                                                verticalAdapterThird = new VerticalAdapterThird(
                                                    targetAchievementModelsThird);
                                                verticalRecyclerViewThird.setLayoutManager(
                                                    new GridLayoutManager(getMainActivity(), 3));
                                                verticalRecyclerViewThird.setAdapter(
                                                    verticalAdapterThird);
                                                verticalAdapterThird.notifyDataSetChanged();
                                                populatePieChartThird(targetAchievementModelsThird);
                                            }
                                        }
                                    } else {
                                        hideProgress();
                                        tvErrorFirst.setVisibility(View.VISIBLE);
                                        tvUniqueLeadsFirst.setText("");
                                        verticalAdapterFirst =
                                            new VerticalAdapterFirst(targetAchievementModelsFirst);
                                        verticalRecyclerViewFirst.setLayoutManager(
                                            new GridLayoutManager(getMainActivity(), 3));
                                        verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
                                        verticalAdapterFirst.notifyDataSetChanged();
                                        populatePieChartFirst(targetAchievementModelsFirst);

                                        tvErrorSecond.setVisibility(View.VISIBLE);
                                        tvUniqueLeadsSecond.setText("");
                                        verticalAdapterSecond = new VerticalAdapterSecond(
                                            targetAchievementModelsSecond);
                                        verticalRecyclerViewSecond.setLayoutManager(
                                            new GridLayoutManager(getMainActivity(), 3));
                                        verticalRecyclerViewSecond.setAdapter(
                                            verticalAdapterSecond);
                                        verticalAdapterSecond.notifyDataSetChanged();

                                        tvErrorThird.setVisibility(View.VISIBLE);
                                        tvUniqueLeadsThird.setText("");
                                        verticalAdapterThird =
                                            new VerticalAdapterThird(targetAchievementModelsThird);
                                        verticalRecyclerViewThird.setLayoutManager(
                                            new GridLayoutManager(getMainActivity(), 3));
                                        verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
                                        verticalAdapterThird.notifyDataSetChanged();
                                        populatePieChartThird(targetAchievementModelsThird);

                                        populatePieChartSecond(targetAchievementModelsSecond);
                                        String errorMsg =
                                            CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }
                                } else {
                                    hideProgress();
                                    tvErrorFirst.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsFirst.setText("");
                                    verticalAdapterFirst =
                                        new VerticalAdapterFirst(targetAchievementModelsFirst);
                                    verticalRecyclerViewFirst.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
                                    verticalAdapterFirst.notifyDataSetChanged();
                                    populatePieChartFirst(targetAchievementModelsFirst);

                                    tvErrorSecond.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsSecond.setText("");
                                    verticalAdapterSecond =
                                        new VerticalAdapterSecond(targetAchievementModelsSecond);
                                    verticalRecyclerViewSecond.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
                                    verticalAdapterSecond.notifyDataSetChanged();
                                    populatePieChartSecond(targetAchievementModelsSecond);

                                    tvErrorThird.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsThird.setText("");
                                    verticalAdapterThird =
                                        new VerticalAdapterThird(targetAchievementModelsThird);
                                    verticalRecyclerViewThird.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
                                    verticalAdapterThird.notifyDataSetChanged();
                                    populatePieChartThird(targetAchievementModelsThird);

                                    //                                customProgress.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                hideProgress();
                                tvErrorFirst.setVisibility(View.VISIBLE);
                                tvUniqueLeadsFirst.setText("");
                                verticalAdapterFirst =
                                    new VerticalAdapterFirst(targetAchievementModelsFirst);
                                verticalRecyclerViewFirst.setLayoutManager(
                                    new GridLayoutManager(getMainActivity(), 3));
                                verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
                                verticalAdapterFirst.notifyDataSetChanged();
                                populatePieChartFirst(targetAchievementModelsFirst);

                                tvErrorSecond.setVisibility(View.VISIBLE);
                                tvUniqueLeadsSecond.setText("");
                                verticalAdapterSecond =
                                    new VerticalAdapterSecond(targetAchievementModelsSecond);
                                verticalRecyclerViewSecond.setLayoutManager(
                                    new GridLayoutManager(getMainActivity(), 3));
                                verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
                                verticalAdapterSecond.notifyDataSetChanged();
                                populatePieChartSecond(targetAchievementModelsSecond);

                                tvErrorThird.setVisibility(View.VISIBLE);
                                tvUniqueLeadsThird.setText("");
                                verticalAdapterThird =
                                    new VerticalAdapterThird(targetAchievementModelsThird);
                                verticalRecyclerViewThird.setLayoutManager(
                                    new GridLayoutManager(getMainActivity(), 3));
                                verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
                                verticalAdapterThird.notifyDataSetChanged();
                                populatePieChartThird(targetAchievementModelsThird);
                                //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            //                        customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            //                        customProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(int statusCode,
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
                                    tvErrorFirst.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsFirst.setText("");
                                    verticalAdapterFirst =
                                        new VerticalAdapterFirst(targetAchievementModelsFirst);
                                    verticalRecyclerViewFirst.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
                                    verticalAdapterFirst.notifyDataSetChanged();
                                    populatePieChartFirst(targetAchievementModelsFirst);

                                    tvErrorSecond.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsSecond.setText("");
                                    verticalAdapterSecond =
                                        new VerticalAdapterSecond(targetAchievementModelsSecond);
                                    verticalRecyclerViewSecond.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
                                    verticalAdapterSecond.notifyDataSetChanged();
                                    populatePieChartSecond(targetAchievementModelsSecond);

                                    tvErrorThird.setVisibility(View.VISIBLE);
                                    tvUniqueLeadsThird.setText("");
                                    verticalAdapterThird =
                                        new VerticalAdapterThird(targetAchievementModelsThird);
                                    verticalRecyclerViewThird.setLayoutManager(
                                        new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
                                    verticalAdapterThird.notifyDataSetChanged();
                                    populatePieChartThird(targetAchievementModelsThird);
                                    String error =
                                        CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                    if (CRMAppUtil.isBlank(error)) {
                                        error = SOME_SERVER_ERROR_OCCURRED;
                                    }
                                    // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                    return;
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                MSFDialog.alertDialog(getMainActivity(), 0,
                                    getResources().getString(R.string.motilal_oswal),
                                    SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                            JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            //                        customProgress.setVisibility(View.GONE);
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            hideProgress();
                        }

                        @Override
                        public void onFailure(int statusCode,
                            cz.msebera.android.httpclient.Header[] headers, String responseString,
                            Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            //                        customProgress.setVisibility(View.GONE);
                            MSFDialog.alertDialog(getMainActivity(), 0,
                                getResources().getString(R.string.motilal_oswal),
                                SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            hideProgress();
                        }
                    });
            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0,
                    getResources().getString(R.string.motilal_oswal),
                    ERROR_LOADING_DATA_FOR + widgetSystemName, "OK", true, null);
            }
        } else {
            //            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                null);
        }
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<WidgetModel> widgetModelList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvWidgetName;
            public ImageView imageView;
            public RelativeLayout llWidgetList;
            public CardView card_view;

            public MyViewHolder(View view) {
                super(view);
                tvWidgetName = (TextView) view.findViewById(R.id.tvWidgetName);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                llWidgetList = (RelativeLayout) view.findViewById(R.id.llWidgetList);
                card_view = (CardView) view.findViewById(R.id.card_view);
            }
        }

        public HorizontalAdapter(List<WidgetModel> widgetModelList) {
            this.widgetModelList = widgetModelList;
        }

        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target_achievement_list, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        public void selectCategory(String name) {
            for (WidgetModel model : widgetModelList) {
                if (name.equals(model.getName())) {
                    model.setSelected(true);
                } else {
                    model.setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder,
            final int position) {
            final WidgetModel widgetModel = widgetModelList.get(position);
            if (widgetModel.isSelected()) {
                subWidgetModelList = new ArrayList<>();
                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.white));
                setImage(widgetModel.getIconUrl2().replaceAll(" ", "%20"), holder.imageView);
                holder.tvWidgetName.setText(widgetModel.getName());
                holder.card_view.setCardBackgroundColor(
                    getResources().getColor(R.color.secondaryBackground));
                widgetSystemName = widgetModel.getSystemName();
                webServiceCallFirst();
                widgetName = widgetModel.getName();
                //  uniqueStr = widgetName.concat(UNIQUE);
                widgetTypeList = widgetModel.getWidgetTypeList();
            } else {
                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.txt_dselect));
                setImage(widgetModel.getIconUrl1().replaceAll(" ", "%20"), holder.imageView);
                holder.tvWidgetName.setText(widgetModel.getName());
                holder.card_view.setCardBackgroundColor(getResources().getColor(R.color.white));
            }
            holder.llWidgetList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!widgetModel.getName().isEmpty()) {
                        int post = 0;
                        //   selectCategory(widgetModel.getName());
                        for (WidgetModel model : widgetModelList) {
                            if (model.isSelected()) {
                                post = model.getPreviousSelectedPosition();
                            }
                        }
                        for (WidgetModel model : widgetModelList) {
                            if (widgetModel.getName().equals(model.getName())) {
                                model.setSelected(true);
                                model.setPreviousSelectedPosition(position);
                            } else {
                                model.setSelected(false);
                            }
                        }
                        notifyItemChanged(position);
                        notifyItemChanged(post);
                        txtCenterValue.setText("");
                        txtRightValue.setText("");
                        txtCenterValue.setText("");
                        tvUniqueLeadsFirst.setText("");
                        llTopFirst.setVisibility(View.GONE);
                        ivView.setVisibility(View.GONE);
                        tvUniqueLeadsSecond.setText("");
                        tvUniqueLeadsThird.setText("");
                    }
                }
            });
        }

        public void setImage(String imageUrl, final ImageView imageView) {

            Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .placeholder(R.drawable.ic_white)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        //circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
        }

        @Override
        public int getItemCount() {
            return widgetModelList.size();
        }
    }

    public class VerticalAdapterFirst
        extends RecyclerView.Adapter<VerticalAdapterFirst.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvPercentage, tvLabel;
            public View imageView;

            public MyViewHolder(View view) {
                super(view);
                tvPercentage = (TextView) view.findViewById(R.id.tvPercentage);
                tvLabel = (TextView) view.findViewById(R.id.tvLabel);
                imageView = (View) view.findViewById(R.id.view);
            }
        }

        public VerticalAdapterFirst(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @Override
        public VerticalAdapterFirst.MyViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new VerticalAdapterFirst.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final VerticalAdapterFirst.MyViewHolder holder, int position) {
            try {
                final TargetAchievementModel widgetModel = targetAchievementModelList.get(position);
                holder.tvLabel.setText(widgetModel.getLabel());
                holder.tvPercentage.setText(widgetModel.getForrmated());
                holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[position]);
            } catch (IndexOutOfBoundsException iob) {
                //
                holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[3]);
            }
        }

        @Override
        public int getItemCount() {
            return targetAchievementModelsFirst.size();
        }
    }

    public class VerticalAdapterSecond
        extends RecyclerView.Adapter<VerticalAdapterSecond.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvPercentage, tvLabel;
            public View imageView;

            public MyViewHolder(View view) {
                super(view);
                tvPercentage = (TextView) view.findViewById(R.id.tvPercentage);
                tvLabel = (TextView) view.findViewById(R.id.tvLabel);
                imageView = (View) view.findViewById(R.id.view);
            }
        }

        public VerticalAdapterSecond(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @Override
        public VerticalAdapterSecond.MyViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new VerticalAdapterSecond.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final VerticalAdapterSecond.MyViewHolder holder,
            int position) {
            final TargetAchievementModel widgetModel = targetAchievementModelList.get(position);
            holder.tvLabel.setText(widgetModel.getLabel());
            holder.tvPercentage.setText(widgetModel.getForrmated());
            holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[position]);
        }

        @Override
        public int getItemCount() {
            return targetAchievementModelsSecond.size();
        }
    }

    public class VerticalAdapterThird
        extends RecyclerView.Adapter<VerticalAdapterThird.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tvPercentage, tvLabel;
            public View imageView;

            public MyViewHolder(View view) {
                super(view);
                tvPercentage = (TextView) view.findViewById(R.id.tvPercentage);
                tvLabel = (TextView) view.findViewById(R.id.tvLabel);
                imageView = (View) view.findViewById(R.id.view);
            }
        }

        public VerticalAdapterThird(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @Override
        public VerticalAdapterThird.MyViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new VerticalAdapterThird.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final VerticalAdapterThird.MyViewHolder holder, int position) {
            final TargetAchievementModel widgetModel = targetAchievementModelList.get(position);
            holder.tvLabel.setText(widgetModel.getLabel());
            holder.tvPercentage.setText(widgetModel.getForrmated());
            holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[position]);
        }

        @Override
        public int getItemCount() {
            return targetAchievementModelsThird.size();
        }
    }
}
