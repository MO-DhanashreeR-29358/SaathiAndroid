package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raju on 2/16/2017.
 */

public class TargetAchievementFragment extends MOSLCommonFragment implements View.OnClickListener {

    private View targetView;
    private TextView txtRightValue, txtRightStr, txtCenterValue, txtCenterStr, txtLeftStr, txtLeftValue, tvUniqueLeads, tvError;
    private LockedPieChart chartTarget;
    private ArrayList<String> xdataTargetArr = new ArrayList<>();
    private ArrayList<String> yDataTargetArr = new ArrayList<>();
    private ArrayList<HashMap<String, String>> dataArr = new ArrayList<>();
    private Typeface tf;
    private List<WidgetModel> widgetModelList;
    static Context mContext;
    private RecyclerView horizontalRecyclerView;
    private RecyclerView verticalRecyclerView;
    private HorizontalAdapter horizontalAdapter;
    private VerticalAdapter verticalAdapter;
    private ArrayList<TargetAchievementModel> targetAchievementModels;
    private LinearLayout NewDataLinearLayout, llTop;
    private int[] VORDIPLOM_COLORS = {
            Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
            Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46), Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
            Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46)
    };
    private Spinner spTargetVsAchivementFilter;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private String leftStr, rightStr, centerStr;
    private Map addtionalParameterMap = new HashMap<>();
    private Map NewaddtionalParameterMap = new HashMap<>();
    private LinearLayout linearLayoutRight, linearLayoutCenter, linearLayoutLeft;
    private List<WidgetType> widgetTypeList = new ArrayList<>();
    private String widgetName;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        targetView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_target_achievement);
        setUpView(targetView);
        return targetView;
    }

    public static TargetAchievementFragment newInstance(Context context) {
        TargetAchievementFragment fragment = new TargetAchievementFragment();
        mContext = context;
        return fragment;
    }

    public void setUpView(View targetView) {
        widgetModelList = new ArrayList<>();
        try {
            dashboardModel = (DashboardModel) this.getArguments().getSerializable(DASHBOARD_MODEL);
            widgetModelList = dashboardModel.getGroupModel(DashboardSystemModel.getTargetAchivementSystemName()).getWidgetList();
            if(widgetModelList.size() <3){
                widgetModelList.add(new WidgetModel());
                widgetModelList.add(new WidgetModel());
            }else if(widgetModelList.size() <4){
                widgetModelList.add(new WidgetModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtRightStr = (TextView) targetView.findViewById(R.id.txtRightStr);
        txtRightValue = (TextView) targetView.findViewById(R.id.txtRightValue);
        tvError = (TextView) targetView.findViewById(R.id.tvError);
        txtCenterValue = (TextView) targetView.findViewById(R.id.txtCenterValue);
        txtCenterStr = (TextView) targetView.findViewById(R.id.txtCenterStr);
        txtLeftValue = (TextView) targetView.findViewById(R.id.txtLeftValue);
        txtLeftStr = (TextView) targetView.findViewById(R.id.txtLeftStr);
        tvUniqueLeads = (TextView) targetView.findViewById(R.id.tvUniqueLeads);
        horizontalRecyclerView = (RecyclerView) targetView.findViewById(R.id.horizontalRecyclerView);
        verticalRecyclerView = (RecyclerView) targetView.findViewById(R.id.verticalRecyclerView);
        chartTarget = (LockedPieChart) targetView.findViewById(R.id.chartTarget);
        chartTarget.setRotationEnabled(false);
        chartTarget.setEntryLabelTextSize(getResources().getDimension(R.dimen.chart_text_size));
        llTop = (LinearLayout) targetView.findViewById(R.id.llTop);

        linearLayoutLeft = (LinearLayout) targetView.findViewById(R.id.linearLayoutLeft);
        linearLayoutCenter = (LinearLayout) targetView.findViewById(R.id.linearLayoutCenter);
        linearLayoutRight = (LinearLayout) targetView.findViewById(R.id.linearLayoutRight);
        spTargetVsAchivementFilter = (Spinner) targetView.findViewById(R.id.spTargetVsAchivementFilter);
        DashboardGroupModel groupModel = dashboardModel.getGroupModel(DashboardSystemModel.getTargetAchivementSystemName());
        //set filter data
        List<WidgetFilterModel> filterList = groupModel.getGroupFilterList();
        if (filterList != null && filterList.size() == 1) {
            ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
            adapter1.setDropDownViewResource(R.layout.mdropdown);
            spTargetVsAchivementFilter.setAdapter(adapter1);
        }
        spTargetVsAchivementFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                webServiceCall();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        horizontalAdapter = new HorizontalAdapter(widgetModelList);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getMainActivity(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();


        linearLayoutLeft.setOnClickListener(this);
        linearLayoutCenter.setOnClickListener(this);
        linearLayoutRight.setOnClickListener(this);
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
            addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
           /* if (dashboardModel.getName().toLowerCase().endsWith("self")) {
                addtionalParameterMap.put(SELF_KEY, "SELF");
            }*/


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void populateTotalClientPieChartBroking(ArrayList<TargetAchievementModel> targetAchievementModels) {
        Description des = chartTarget.getDescription();
        des.setEnabled(false);
        chartTarget.setUsePercentValues(true);
        chartTarget.setExtraOffsets(5, 10, 5, 5);
        chartTarget.setDragDecelerationFrictionCoef(0.85f);
        chartTarget.getLegend().setEnabled(false);
        // enable hole and configure
        chartTarget.setDrawHoleEnabled(true);
        chartTarget.setTransparentCircleColor(Color.WHITE);
        chartTarget.setTransparentCircleAlpha(100);
        //chartTarget.setHoleColor(R.color.counter_text_bg);
        chartTarget.setHoleRadius(70f);
        chartTarget.setTransparentCircleRadius(30f);

        chartTarget.setDrawCenterText(true);
        chartTarget.setDrawSliceText(false);


        // enable rotation of the chart by touch

        // totalClientPieChart.setOnChartValueSelectedListener(this);

        setDataBroking(3, 100, targetAchievementModels);

        chartTarget.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataBroking(int count, float range, final ArrayList<TargetAchievementModel> targetAchievementModels) {

        float mult = range;

        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++) {
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()), targetAchievementModels.get(i).getLabel(), i));
        }
       /* yVals1.add(new PieEntry(Float.valueOf("0.83"), targetAchievementModels.get(0).getLabel(), 0));
        yVals1.add(new PieEntry(Float.valueOf("0.83"), targetAchievementModels.get(1).getLabel(), 1));*/

        ArrayList<String> xVals1 = new ArrayList<String>();

        for (int i = 0; i < xdataTargetArr.size(); i++)
            xVals1.add(xdataTargetArr.get(i));
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
        chartTarget.setData(data);

        // undo all highlights
        chartTarget.highlightValues(null);

        chartTarget.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                addtionalParameterMap.clear();
                pieChartDetail(e.getX());
                Log.d("Highlited", String.valueOf(h.getX()));
                Log.d("xValue", String.valueOf((int) (e.getX())));
                addtionalParameterMap.put(KEY, targetAchievementModels.get((int) h.getX()).getLabel());
                // addtionalParameterMap.put(WIDGETTYPE, targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                //  secondClickTarget(addtionalParameterMap);
                addtionalParameterMap.putAll(getAddtionalParameterMap());
                Bundle args = new Bundle();
                args.putString(SYSTEM_NAME, widgetSystemName);
                args.putString(LABEL, TEAM_TARGET_TITLE + "-" + widgetName);
                args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);

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


        chartTarget.invalidate();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.linearLayoutCenter) {
            NewaddtionalParameterMap.clear();
            NewaddtionalParameterMap.put(WIDGETTYPE, centerStr);
            try {
                if (Double.parseDouble(txtCenterValue.getText().toString().trim()) != 0) {
                    secondClickTarget(NewaddtionalParameterMap);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.linearLayoutRight) {
            NewaddtionalParameterMap.clear();
            NewaddtionalParameterMap.put(WIDGETTYPE, rightStr);
            try {
                if (Double.parseDouble(txtRightValue.getText().toString().trim()) != 0) {
                    secondClickTarget(NewaddtionalParameterMap);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } else if (id == R.id.linearLayoutLeft) {
            if (!leftStr.equalsIgnoreCase("Target")) {
                NewaddtionalParameterMap.clear();
                NewaddtionalParameterMap.put(WIDGETTYPE, leftStr.toLowerCase());
                try {
                    if (Double.parseDouble(txtLeftValue.getText().toString().trim()) != 0) {
                        secondClickTarget(NewaddtionalParameterMap);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
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
        public void onBindViewHolder(final HorizontalAdapter.MyViewHolder holder, final int position) {
            final WidgetModel widgetModel = widgetModelList.get(position);
            if (widgetModel.isSelected()) {
                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.white));
                setImage(widgetModel.getIconUrl2().replaceAll(" ", "%20"), holder.imageView);
                holder.tvWidgetName.setText(widgetModel.getName());
                holder.card_view.setCardBackgroundColor(getResources().getColor(R.color.secondaryBackground));
                widgetSystemName = widgetModel.getSystemName();
                widgetTypeList = widgetModel.getWidgetTypeList();
                webServiceCall();
                widgetName = widgetModel.getName();
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

                        txtRightValue.setText("");
                        txtCenterValue.setText("");
                        txtLeftValue.setText("");
                        tvUniqueLeads.setText("");
                        llTop.setVisibility(View.GONE);
                        tvError.setVisibility(View.GONE);
                    }
                }
            });

        }

        public void setImage(String imageUrl, final ImageView imageView) {
            Glide.with(context).load(imageUrl)
                    //remove Chache         
                    .asBitmap().placeholder(R.drawable.ic_white).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(imageView) {
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


    private void webServiceCall() {
        targetAchievementModels = new ArrayList<>();
        yDataTargetArr.clear();
        xdataTargetArr.clear();
        dataArr.clear();
        tvUniqueLeads.setText("");
        populateTotalClientPieChartBroking(targetAchievementModels);
        tvError.setVisibility(View.GONE);
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
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                int j = 0;
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    hideProgress();
                                   /* "value" : 0,
                                            "label" : Unique Meeting,
                                    "formatted" : 0.00%*/
                                    targetAchievementModels.clear();
                                    JSONObject responseJsonObj = response.getJSONObject("Response");
                                    if (!responseJsonObj.isNull("Table1")) {
                                        JSONArray arrayDetail = responseJsonObj.getJSONArray("Table1");
                                        if (arrayDetail.length() != 0 && arrayDetail != null) {
                                            for (int i = 0; i < arrayDetail.length(); i++) {
                                                JSONObject jsonObject = arrayDetail.getJSONObject(i);
                                                String value = (jsonObject.isNull("value") || jsonObject.get("value").equals("")) ? "0" : jsonObject.getString("value");
                                                String label = (jsonObject.isNull("label") || jsonObject.getString("label").equals("")) ? "0" : jsonObject.getString("label");
                                                String formatted = (jsonObject.isNull("formatted") || jsonObject.getString("formatted").equals("")) ? "0" : jsonObject.getString("formatted");
                                                try {
                                                    j = new Double(value).intValue();
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                                yDataTargetArr.add(i, String.valueOf(j));
                                                xdataTargetArr.add(i, label);
                                                targetAchievementModels.add(new TargetAchievementModel(label, formatted, value, null));
                                            }
                                            verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                            verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                            verticalRecyclerView.setAdapter(verticalAdapter);
                                            verticalAdapter.notifyDataSetChanged();
                                            populateTotalClientPieChartBroking(targetAchievementModels);

                                        } else {
                                            tvError.setVisibility(View.VISIBLE);
                                            verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                            verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                            verticalRecyclerView.setAdapter(verticalAdapter);
                                            verticalAdapter.notifyDataSetChanged();
                                            populateTotalClientPieChartBroking(targetAchievementModels);
                                        }
                                    } else {
                                        tvError.setVisibility(View.VISIBLE);
                                        verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                        verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                        verticalRecyclerView.setAdapter(verticalAdapter);
                                        verticalAdapter.notifyDataSetChanged();
                                        populateTotalClientPieChartBroking(targetAchievementModels);
                                    }
                                    if (!responseJsonObj.isNull("Table")) {
                                        JSONArray tableArray = responseJsonObj.getJSONArray("Table");
                                        int arrayLength = 0;
                                        if (tableArray.length() == 5) {
                                            arrayLength = 3;
                                        } else {
                                            arrayLength = tableArray.length();
                                        }
                                        if (tableArray.length() != 0 && tableArray != null) {
                                            llTop.setVisibility(View.VISIBLE);
                                            for (int i = 0; i < arrayLength; i++) {
                                                if (arrayLength == 3) {
                                                    linearLayoutLeft.setVisibility(View.VISIBLE);
                                                    linearLayoutRight.setVisibility(View.VISIBLE);
                                                    linearLayoutCenter.setVisibility(View.VISIBLE);
                                                    JSONObject getJsonObject = tableArray.getJSONObject(i);
                                                    String displayText = (getJsonObject.isNull("DisplayText") || getJsonObject.getString("DisplayText").equals("")) ? "" : getJsonObject.getString("DisplayText");
                                                    String value = (getJsonObject.isNull("Value") || getJsonObject.getString("Value").equals("")) ? "0" : getJsonObject.getString("Value");
                                                    int ord = getJsonObject.isNull("Ord") ? 0 : getJsonObject.getInt("Ord");
                                                    if (ord == 1) {
                                                        leftStr = displayText;
                                                        if (!displayText.equalsIgnoreCase("Target")) {
                                                            txtLeftStr.setText(displayText);
                                                        } else {
                                                            FilterRowData filter1 = (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                                            if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue().equalsIgnoreCase("WTD")) {
                                                                txtLeftStr.setText("MTD " + displayText);
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
                                                    linearLayoutLeft.setVisibility(View.VISIBLE);
                                                    linearLayoutCenter.setVisibility(View.VISIBLE);
                                                    JSONObject getJsonObject = tableArray.getJSONObject(i);
                                                    String displayText = (getJsonObject.isNull("DisplayText") || getJsonObject.getString("DisplayText").equals("")) ? "" : getJsonObject.getString("DisplayText");
                                                    String value = (getJsonObject.isNull("Value") || getJsonObject.getString("Value").equals("")) ? "" : getJsonObject.getString("Value");
                                                    int ord = getJsonObject.isNull("Ord") ? 0 : getJsonObject.getInt("Ord");
                                                    if (ord == 1) {
                                                        leftStr = displayText;
                                                        if (!displayText.equalsIgnoreCase("Target")) {
                                                            txtLeftStr.setText(displayText);
                                                        } else {
                                                            FilterRowData filter1 = (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                                            if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue().equalsIgnoreCase("WTD")) {
                                                                txtLeftStr.setText("MTD " + displayText);
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
                                                    linearLayoutLeft.setVisibility(View.VISIBLE);
                                                    linearLayoutRight.setVisibility(View.GONE);
                                                    linearLayoutCenter.setVisibility(View.GONE);
                                                    JSONObject getJsonObject = tableArray.getJSONObject(i);
                                                    String displayText = (getJsonObject.isNull("DisplayText") || getJsonObject.getString("DisplayText").equals("")) ? "" : getJsonObject.getString("DisplayText");
                                                    String value = (getJsonObject.isNull("Value") || getJsonObject.getString("Value").equals("")) ? "" : getJsonObject.getString("Value");
                                                    int ord = getJsonObject.isNull("Ord") ? 0 : getJsonObject.getInt("Ord");
                                                    if (ord == 1) {
                                                        leftStr = displayText;
                                                        if (!displayText.equalsIgnoreCase("Target")) {
                                                            txtLeftStr.setText(displayText);
                                                        } else {
                                                            FilterRowData filter1 = (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                                            if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue().equalsIgnoreCase("WTD")) {
                                                                txtLeftStr.setText("MTD " + displayText);
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
                                                /*JSONArray jsonArrayKeys = getJsonObject.names();
                                                leftStr = jsonArrayKeys.getString(0);
                                                centerStr = jsonArrayKeys.getString(1);
                                                txtLeftStr.setText(leftStr);
                                                txtCenterStr.setText(centerStr);
                                                int target = getJsonObject.isNull(leftStr) ? 0 : getJsonObject.getInt(leftStr);
                                                int achieved = getJsonObject.isNull(centerStr) ? 0 : getJsonObject.getInt(centerStr);
                                                txtLeftValue.setText(target + "");
                                                txtCenterValue.setText(achieved + "");
                                                if (getJsonObject.has("Unique")) {
                                                    int unique = getJsonObject.isNull("Unique") ? 0 : getJsonObject.getInt("Unique");
                                                    txtRightValue.setText(unique + "");
                                                    linearLayoutUnique.setVisibility(View.VISIBLE);
                                                } else {
                                                    linearLayoutUnique.setVisibility(View.GONE);
                                                }*/
                                                try {
                                                    tvUniqueLeads.setText(widgetTypeList.get(0).getTitle());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    tvUniqueLeads.setText("");
                                                }
                                            }
                                        } else {
                                            tvUniqueLeads.setText("");
                                        }
                                    } else {
                                        tvUniqueLeads.setText("");
                                    }


                                } else {
                                    tvError.setVisibility(View.VISIBLE);
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    tvUniqueLeads.setText("");
                                    verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                    verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                    verticalRecyclerView.setAdapter(verticalAdapter);
                                    verticalAdapter.notifyDataSetChanged();
                                    populateTotalClientPieChartBroking(targetAchievementModels);
                                }

                            } else {
                                tvError.setVisibility(View.VISIBLE);
                                hideProgress();
                                tvUniqueLeads.setText("");
                                verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                verticalRecyclerView.setAdapter(verticalAdapter);
                                verticalAdapter.notifyDataSetChanged();
                                populateTotalClientPieChartBroking(targetAchievementModels);
//                                customProgress.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            tvError.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                            hideProgress();
                            //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            tvUniqueLeads.setText("");
                            verticalAdapter = new VerticalAdapter(targetAchievementModels);
                            verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                            verticalRecyclerView.setAdapter(verticalAdapter);
                            verticalAdapter.notifyDataSetChanged();
                            populateTotalClientPieChartBroking(targetAchievementModels);
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
                                tvError.setVisibility(View.VISIBLE);
                                tvUniqueLeads.setText("");
                                verticalAdapter = new VerticalAdapter(targetAchievementModels);
                                verticalRecyclerView.setLayoutManager(new GridLayoutManager(getMainActivity(), 3));
                                verticalRecyclerView.setAdapter(verticalAdapter);
                                verticalAdapter.notifyDataSetChanged();
                                populateTotalClientPieChartBroking(targetAchievementModels);

                                String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(errorMsg)) {
                                    //     MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                } else {
                                    //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }
                                return;
                            }
                        } catch (Exception ex) {
                            //eat it
                            ex.printStackTrace();
                            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), ERROR_LOADING_DATA_FOR + widgetSystemName, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }


    public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

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


        public VerticalAdapter(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }


        @Override
        public VerticalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new VerticalAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final TargetAchievementModel widgetModel = targetAchievementModelList.get(position);
            holder.tvLabel.setText(widgetModel.getLabel());
            holder.tvPercentage.setText(widgetModel.getForrmated());
            holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[position]);

        }


        @Override
        public int getItemCount() {
            return targetAchievementModels.size();
        }
    }

    public static void smoothScrollToUserSelectedPosition(final RecyclerView scrollView, int destination) {

        final RecyclerView.Adapter adapter = scrollView.getAdapter();
        int totalCount = adapter.getItemCount();
        if (destination > 0 && destination < totalCount - 1) {
            //AutoScrool
            scrollView.smoothScrollToPosition(destination);
        }


    }

}
