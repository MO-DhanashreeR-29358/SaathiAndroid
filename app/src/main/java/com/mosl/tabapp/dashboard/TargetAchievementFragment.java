package com.mosl.tabapp.dashboard;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
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
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.saathi.api.model.dashboard.target.Table1Item;
import com.mosl.saathi.api.model.dashboard.target.TableItem;
import com.mosl.saathi.api.model.dashboard.target.TargetAchievementItem;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.common.LockedPieChart;
import com.mosl.tabapp.core.CommonFragment;
import com.mosl.tabapp.model.TargetAchievementModel;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.model.dashboard.WidgetType;
import com.mosl.tabapp.navigation.NavigationConstants;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.disposables.Disposable;

import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.KEY;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.SELF_KEY;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TEAM_TARGET_TITLE;
import static com.mosl.tabapp.constants.MOSLConstants.WIDGETTYPE;

/**
 * Created by raju on 2/16/2017.
 */

public class TargetAchievementFragment extends CommonFragment implements View.OnClickListener {

    private TextView txtRightValue, txtRightStr, txtCenterValue, txtCenterStr, txtLeftStr,
            txtLeftValue, tvUniqueLeads, tvError;
    private LockedPieChart chartTarget;
    private RecyclerView verticalRecyclerView;
    private VerticalAdapter verticalAdapter;
    private ArrayList<TargetAchievementModel> targetAchievementModels;
    private LinearLayout llTop;
    private int[] VORDIPLOM_COLORS = {
            Color.rgb(255, 21, 0), Color.rgb(38, 127, 183), Color.rgb(0, 177, 207),
            Color.rgb(22, 225, 143), Color.rgb(64, 0, 255), Color.rgb(255, 255, 0),
            Color.rgb(254, 46, 46), Color.rgb(255, 21, 0), Color.rgb(38, 127, 183),
            Color.rgb(0, 177, 207),
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View targetView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(targetView);
        return targetView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_target_achievement;
    }

    private void secondClickTarget(Map addtionalParameterMap) {
        addtionalParameterMap.putAll(getAddtionalParameterMap());
        Bundle args = new Bundle();
        args.putString(SYSTEM_NAME, widgetSystemName);
        args.putString(LABEL, TEAM_TARGET_TITLE + "-" + widgetName);
        args.putSerializable(MAP, (Serializable) addtionalParameterMap);
        navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args, true,
                false);
    }

    public void setUpView(View targetView) {
        List<WidgetModel> widgetModelList = new ArrayList<>();
        try {
            dashboardModel = (DashboardModel) Objects.requireNonNull(this.getArguments()).
                    getSerializable(DASHBOARD_MODEL);
            widgetModelList =
                    Objects.requireNonNull(dashboardModel).
                            getGroupModel(DashboardSystemModel.getTargetAchivementSystemName())
                            .getWidgetList();
            if (widgetModelList.size() < 3) {
                widgetModelList.add(new WidgetModel());
                widgetModelList.add(new WidgetModel());
            } else if (widgetModelList.size() < 4) {
                widgetModelList.add(new WidgetModel());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtRightStr = targetView.findViewById(R.id.txtRightStr);
        txtRightValue = targetView.findViewById(R.id.txtRightValue);
        tvError = targetView.findViewById(R.id.tvError);
        txtCenterValue = targetView.findViewById(R.id.txtCenterValue);
        txtCenterStr = targetView.findViewById(R.id.txtCenterStr);
        txtLeftValue = targetView.findViewById(R.id.txtLeftValue);
        txtLeftStr = targetView.findViewById(R.id.txtLeftStr);
        tvUniqueLeads = targetView.findViewById(R.id.tvUniqueLeads);
        RecyclerView horizontalRecyclerView = targetView.findViewById(R.id.horizontalRecyclerView);
        verticalRecyclerView = targetView.findViewById(R.id.verticalRecyclerView);
        chartTarget = targetView.findViewById(R.id.chartTarget);
        chartTarget.setRotationEnabled(false);
        chartTarget.setEntryLabelTextSize(getResources().getDimension(R.dimen.chart_text_size));
        llTop = targetView.findViewById(R.id.llTop);

        linearLayoutLeft = targetView.findViewById(R.id.linearLayoutLeft);
        linearLayoutCenter = targetView.findViewById(R.id.linearLayoutCenter);
        linearLayoutRight = targetView.findViewById(R.id.linearLayoutRight);
        spTargetVsAchivementFilter =
                targetView.findViewById(R.id.spTargetVsAchivementFilter);
        DashboardGroupModel groupModel =
                dashboardModel.getGroupModel(DashboardSystemModel.getTargetAchivementSystemName());
        //set filter data
        List<WidgetFilterModel> filterList = groupModel.getGroupFilterList();
        if (filterList != null && filterList.size() == 1) {
            ArrayAdapter<FilterRowData> adapter1 =
                    new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item,
                            filterList.get(0).getFilterData());
            adapter1.setDropDownViewResource(R.layout.mdropdown);
            spTargetVsAchivementFilter.setAdapter(adapter1);
        }
        spTargetVsAchivementFilter.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                               int position, long id) {
                        webServiceCall();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }
                });
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(widgetModelList);
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();

        linearLayoutLeft.setOnClickListener(this);
        linearLayoutCenter.setOnClickListener(this);
        linearLayoutRight.setOnClickListener(this);
    }

    private Map<String, String> getAddtionalParameterMap() {

        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {
            FilterRowData filter1 = (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
            addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void populateTotalClientPieChartBroking(
            ArrayList<TargetAchievementModel> targetAchievementModels) {
        Description des = chartTarget.getDescription();
        des.setEnabled(false);
        chartTarget.setUsePercentValues(true);
        chartTarget.setExtraOffsets(5, 10, 5, 5);
        chartTarget.setDragDecelerationFrictionCoef(0.85f);
        chartTarget.getLegend().setEnabled(false);
        chartTarget.setDrawHoleEnabled(true);
        chartTarget.setTransparentCircleColor(Color.WHITE);
        chartTarget.setTransparentCircleAlpha(100);
        chartTarget.setHoleRadius(70f);
        chartTarget.setTransparentCircleRadius(30f);

        chartTarget.setDrawCenterText(true);
        chartTarget.setDrawEntryLabels(false);

        // enable rotation of the chart by touch

        // totalClientPieChart.setOnChartValueSelectedListener(this);

        setDataBroking(targetAchievementModels);

        chartTarget.animateY(1400, Easing.EasingOption.EaseInOutQuad);
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

    private void setDataBroking(
            final ArrayList<TargetAchievementModel> targetAchievementModels) {

        ArrayList<PieEntry> yVals1 = new ArrayList<>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++) {
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                    targetAchievementModels.get(i).getLabel(), i));
        }

        // add a lot of colors

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Client");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);     // for text hidden
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(Typeface.DEFAULT);
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
                addtionalParameterMap.put(KEY,
                        targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.putAll(getAddtionalParameterMap());
                Bundle args = new Bundle();
                args.putString(SYSTEM_NAME, widgetSystemName);
                args.putString(LABEL, TEAM_TARGET_TITLE + "-" + widgetName);
                args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                        true, false);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chartTarget.invalidate();
    }

    private void webServiceCall() {
        targetAchievementModels = new ArrayList<>();
        tvUniqueLeads.setText("");
        populateTotalClientPieChartBroking(targetAchievementModels);
        tvError.setVisibility(View.GONE);

        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            ClientRequest request = new ClientRequest(
                    businessProfileId,
                    getAddtionalParameterMap(),
                    widgetSystemName,
                    dashboardModel.getProductId(),
                    dashboardModel.getEmployeeCode()
            );
            Disposable subscribe = getSaathiApi().getTargetAchievement(request)
                    .compose(applyUiModel())
                    .subscribe(this::render);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderTargetAchievement(TargetAchievementItem response) {
        targetAchievementModels.clear();
        if (response.table1 != null) {
            List<Table1Item> table1Items = response.table1;
            if (table1Items.size() != 0) {
                for (int i = 0; i < table1Items.size(); i++) {
                    Table1Item item = table1Items.get(i);
                    String value = stringOrZero(item.value);
                    String label = stringOrZero(item.label);
                    String formatted = stringOrZero(item.formatted);
                    targetAchievementModels.add(
                            new TargetAchievementModel(label, formatted, value, null));
                }
                verticalAdapter = new VerticalAdapter(targetAchievementModels);
                verticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                verticalRecyclerView.setAdapter(verticalAdapter);
                verticalAdapter.notifyDataSetChanged();
                populateTotalClientPieChartBroking(targetAchievementModels);
            } else {
                tvError.setVisibility(View.VISIBLE);
                verticalAdapter = new VerticalAdapter(targetAchievementModels);
                verticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                verticalRecyclerView.setAdapter(verticalAdapter);
                verticalAdapter.notifyDataSetChanged();
                populateTotalClientPieChartBroking(targetAchievementModels);
            }
        } else {
            tvError.setVisibility(View.VISIBLE);
            verticalAdapter = new VerticalAdapter(targetAchievementModels);
            verticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            verticalRecyclerView.setAdapter(verticalAdapter);
            verticalAdapter.notifyDataSetChanged();
            populateTotalClientPieChartBroking(targetAchievementModels);
        }
        if (response.table != null) {
            List<TableItem> tableItems = response.table;
            int arrayLength;
            if (tableItems.size() == 5) {
                arrayLength = 3;
            } else {
                arrayLength = tableItems.size();
            }
            if (tableItems.size() != 0) {
                llTop.setVisibility(View.VISIBLE);
                for (int i = 0; i < arrayLength; i++) {
                    if (arrayLength == 3) {
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        linearLayoutRight.setVisibility(View.VISIBLE);
                        linearLayoutCenter.setVisibility(View.VISIBLE);
                        TableItem item = tableItems.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrZero(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                FilterRowData filter1 =
                                        (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue()
                                        .equalsIgnoreCase("WTD")) {
                                    txtLeftStr.setText(String.format("MTD %s", displayText));
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
                    } else if (tableItems.size() == 2) {
                        linearLayoutRight.setVisibility(View.GONE);
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        linearLayoutCenter.setVisibility(View.VISIBLE);
                        TableItem item = tableItems.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrEmpty(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                FilterRowData filter1 =
                                        (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue()
                                        .equalsIgnoreCase("WTD")) {
                                    txtLeftStr.setText(String.format("MTD %s", displayText));
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
                    } else if (tableItems.size() == 1) {
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        linearLayoutRight.setVisibility(View.GONE);
                        linearLayoutCenter.setVisibility(View.GONE);
                        TableItem item = tableItems.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrEmpty(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                FilterRowData filter1 =
                                        (FilterRowData) spTargetVsAchivementFilter.getSelectedItem();
                                if (filter1.getValue().equalsIgnoreCase("DAY") || filter1.getValue()
                                        .equalsIgnoreCase("WTD")) {
                                    txtLeftStr.setText(String.format("MTD %s", displayText));
                                } else {
                                    txtLeftStr.setText(displayText);
                                }
                            }
                            txtLeftValue.setText(value);
                        }
                    }
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
    }

    private void render(UiModel<TargetAchievementItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            tvError.setVisibility(View.VISIBLE);
            tvUniqueLeads.setText("");
            verticalAdapter = new VerticalAdapter(targetAchievementModels);
            verticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
            verticalRecyclerView.setAdapter(verticalAdapter);
            verticalAdapter.notifyDataSetChanged();
            populateTotalClientPieChartBroking(targetAchievementModels);
            showOnFailurePopup(error);
        }

        TargetAchievementItem data = uiModel.data;
        if (data != null) {
            try {
                renderTargetAchievement(data);
            } catch (Exception e) {
                tvError.setVisibility(View.VISIBLE);
                tvUniqueLeads.setText("");
                verticalAdapter = new VerticalAdapter(targetAchievementModels);
                verticalRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                verticalRecyclerView.setAdapter(verticalAdapter);
                verticalAdapter.notifyDataSetChanged();
                populateTotalClientPieChartBroking(targetAchievementModels);
            }
        }
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<WidgetModel> widgetModelList;

        private HorizontalAdapter(List<WidgetModel> widgetModelList) {
            this.widgetModelList = widgetModelList;
        }

        @NotNull
        @Override
        public HorizontalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_list, parent, false);

            return new HorizontalAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull final HorizontalAdapter.MyViewHolder holder,
                                     final int position) {
            final WidgetModel widgetModel = widgetModelList.get(position);
            if (widgetModel.isSelected()) {
                holder.tvWidgetName.setTextColor(getResources().getColor(R.color.white));
                setImage(widgetModel.getIconUrl2().replaceAll(" ", "%20"), holder.imageView);
                holder.tvWidgetName.setText(widgetModel.getName());
                holder.card_view.setCardBackgroundColor(
                        getResources().getColor(R.color.secondaryBackground));
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
            holder.llWidgetList.setOnClickListener(v -> {
                if (!widgetModel.getName().isEmpty()) {
                    int post = 0;
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
            });
        }

        private void setImage(String imageUrl, final ImageView imageView) {
            Glide.with(requireContext())
                    .load(imageUrl)
                    //remove Chache
                    .asBitmap()
                    .placeholder(R.drawable.ic_white)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(requireContext().getResources(),
                                            resource);
                            //circularBitmapDrawable.setCircular(true);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return widgetModelList.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvWidgetName;
            private ImageView imageView;
            private RelativeLayout llWidgetList;
            private CardView card_view;

            private MyViewHolder(View view) {
                super(view);
                tvWidgetName = view.findViewById(R.id.tvWidgetName);
                imageView = view.findViewById(R.id.imageView);
                llWidgetList = view.findViewById(R.id.llWidgetList);
                card_view = view.findViewById(R.id.card_view);
            }
        }
    }

    public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        private VerticalAdapter(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @NotNull
        @Override
        public VerticalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new VerticalAdapter.MyViewHolder(itemView);
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvPercentage, tvLabel;
            private View imageView;

            private MyViewHolder(View view) {
                super(view);
                tvPercentage = view.findViewById(R.id.tvPercentage);
                tvLabel = view.findViewById(R.id.tvLabel);
                imageView = view.findViewById(R.id.view);
            }
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
}
