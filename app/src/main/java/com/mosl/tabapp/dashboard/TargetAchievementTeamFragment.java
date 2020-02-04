package com.mosl.tabapp.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mosl.app.coreui.spinner.AdapterUtils;
import com.mosl.commons.data.UiModel;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.saathi.api.model.dashboard.targetteam.Table3Item;
import com.mosl.saathi.api.model.dashboard.targetteam.TableItem;
import com.mosl.saathi.api.model.dashboard.targetteam.TargetAchievementTeamItem;
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
import com.mosl.tabapp.util.SpinnerAdapterFactory;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.Disposable;

import static com.jakewharton.rxbinding3.widget.RxAdapterView.itemSelections;
import static com.mosl.app.core.rx.RxDispatchers.applyUiModel;
import static com.mosl.commons.rx.RxUtils.InvalidSelectionPredicate;
import static com.mosl.tabapp.constants.MOSLConstants.DASHBOARD_MODEL;
import static com.mosl.tabapp.constants.MOSLConstants.KEY;
import static com.mosl.tabapp.constants.MOSLConstants.LABEL;
import static com.mosl.tabapp.constants.MOSLConstants.MAP;
import static com.mosl.tabapp.constants.MOSLConstants.SELF_KEY;
import static com.mosl.tabapp.constants.MOSLConstants.SYSTEM_NAME;
import static com.mosl.tabapp.constants.MOSLConstants.TEAM_TARGET_TITLE;
import static com.mosl.tabapp.constants.MOSLConstants.WIDGETTYPE;

/**
 * Created by raju on 2/20/2017.
 */

public class TargetAchievementTeamFragment extends CommonFragment
        implements View.OnClickListener {
    private TextView txtRightValue, txtRightStr, txtCenterValue, txtCenterStr, txtLeftStr,
            txtLeftValue, tvUniqueLeadsFirst, tvUniqueLeadsSecond, tvUniqueLeadsThird;
    private LockedPieChart pieChartFirst, pieChartSecond, pieChartThird;
    private ImageView ivView;
    private LinearLayout llFirst, llSecond, llThird, llTopFirst;
    private List<WidgetModel> subWidgetModelList;
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
    private Spinner spPeriod;
    private DashboardModel dashboardModel;
    private String widgetSystemName;
    private String leftStr, rightStr, centerStr, widgetName;
    private Map addtionalParameterMap = new HashMap<>();
    private LinearLayout linearLayoutRight, linearLayoutCenter, linearLayoutLeft;
    private List<WidgetType> widgetTypeList = new ArrayList<>();

    @Override
    public View onCreateView(@NotNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View targetView = super.onCreateView(inflater, container, savedInstanceState);
        setUpView(targetView);

        return targetView;
    }

    @Override
    protected int getLayout() {
        return R.layout.frag_target_achievement_team;
    }


    public void setUpView(View targetView) {
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
        RecyclerView horizontalRecyclerView = targetView.findViewById(R.id.horizontalRecyclerView);
        llFirst = targetView.findViewById(R.id.llFirst);
        llTopFirst = targetView.findViewById(R.id.llTopFirst);
        llSecond = targetView.findViewById(R.id.llSecond);
        llThird = targetView.findViewById(R.id.llThird);
        linearLayoutLeft = targetView.findViewById(R.id.linearLayoutLeft);
        linearLayoutCenter = targetView.findViewById(R.id.linearLayoutCenter);
        linearLayoutRight = targetView.findViewById(R.id.linearLayoutRight);

        ivView = targetView.findViewById(R.id.ivView);
        ImageView ivRightArrowFirst = targetView.findViewById(R.id.ivRightArrowFirst);
        ImageView ivRightArrowSecond = targetView.findViewById(R.id.ivRightArrowSecond);
        ImageView ivLeftArrowSecond = targetView.findViewById(R.id.ivLeftArrowSecond);
        ImageView ivLeftArrowThird = targetView.findViewById(R.id.ivLeftArrowThird);
        ivRightArrowFirst.setOnClickListener(this);
        ivRightArrowSecond.setOnClickListener(this);
        ivLeftArrowSecond.setOnClickListener(this);
        ivLeftArrowThird.setOnClickListener(this);
        linearLayoutRight.setOnClickListener(this);
        linearLayoutCenter.setOnClickListener(this);
        linearLayoutLeft.setOnClickListener(this);

        txtRightValue = targetView.findViewById(R.id.txtRightValue);
        txtRightStr = targetView.findViewById(R.id.txtRightStr);
        txtCenterValue = targetView.findViewById(R.id.txtCenterValue);
        txtCenterStr = targetView.findViewById(R.id.txtCenterStr);
        txtLeftStr = targetView.findViewById(R.id.txtLeftStr);
        txtLeftValue = targetView.findViewById(R.id.txtLeftValue);
        tvUniqueLeadsFirst = targetView.findViewById(R.id.tvUniqueLeadsFirst);
        tvErrorFirst = targetView.findViewById(R.id.tvErrorFirst);
        tvErrorSecond = targetView.findViewById(R.id.tvErrorSecond);
        tvErrorThird = targetView.findViewById(R.id.tvErrorThird);
        verticalRecyclerViewFirst =
                targetView.findViewById(R.id.verticalRecyclerViewFirst);
        verticalRecyclerViewSecond =
                targetView.findViewById(R.id.verticalRecyclerViewSecond);
        verticalRecyclerViewThird =
                targetView.findViewById(R.id.verticalRecyclerViewThird);
        spPeriod =
                targetView.findViewById(R.id.spPeriod);

        tvUniqueLeadsSecond = targetView.findViewById(R.id.tvUniqueLeadsSecond);
        tvUniqueLeadsThird = targetView.findViewById(R.id.tvUniqueLeadsThird);

        pieChartFirst = targetView.findViewById(R.id.pieChartFirst);
        pieChartSecond = targetView.findViewById(R.id.pieChartSecond);
        pieChartThird = targetView.findViewById(R.id.pieChartThird);

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
            ArrayAdapter<FilterRowData> periodAdapter = SpinnerAdapterFactory.getDefaultAdapter(requireContext());
            spPeriod.setAdapter(periodAdapter);
            AdapterUtils.setSpinnerData(periodAdapter, spPeriod, filterList.get(0).getFilterData());
            getSpinnerSelection();
        }
        HorizontalAdapter horizontalAdapter = new HorizontalAdapter(subWidgetModelList);
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        horizontalRecyclerView.setAdapter(horizontalAdapter);
        horizontalAdapter.notifyDataSetChanged();

    }

    private void getSpinnerSelection() {
        disposable.add(
                itemSelections(spPeriod).
                        filter(InvalidSelectionPredicate)
                        .skip(1)
                        .subscribe(xyz -> webServiceCallFirst()));

    }

    private FilterRowData getPeriod() {
        return (FilterRowData) spPeriod.getSelectedItem();
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
        pieChartFirst.setHoleRadius(70f);
        pieChartFirst.setTransparentCircleRadius(30f);

        pieChartFirst.setDrawCenterText(true);
        pieChartFirst.setDrawEntryLabels(false);

        pieChartFirst.setRotationAngle(0);
        pieChartFirst.setRotationEnabled(true);

        setDataFirst(targetAchievementModels);

        pieChartFirst.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataFirst(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                    targetAchievementModels.get(i).getLabel(), i));

        // add a lot of colors

        PieDataSet dataSet = new PieDataSet(yVals1, "Total Client");
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<>();

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
                addtionalParameterMap.put(KEY,
                        targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                        targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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
        pieChartSecond.setHoleRadius(70f);
        pieChartSecond.setTransparentCircleRadius(30f);

        pieChartSecond.setDrawCenterText(true);
        pieChartSecond.setDrawEntryLabels(false);

        pieChartSecond.setRotationAngle(0);
        pieChartSecond.setRotationEnabled(true);

        setDataSecond(targetAchievementModels);

        pieChartSecond.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataSecond(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                    targetAchievementModels.get(i).getLabel(), i));

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
                addtionalParameterMap.put(KEY,
                        targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                        targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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
        pieChartThird.setHoleRadius(70f);
        pieChartThird.setTransparentCircleRadius(30f);

        pieChartThird.setDrawCenterText(true);
        pieChartThird.setDrawEntryLabels(false);

        pieChartThird.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChartThird.setRotationEnabled(true);

        setDataThird(targetAchievementModels);

        pieChartThird.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setDataThird(final ArrayList<TargetAchievementModel> targetAchievementModels) {
        ArrayList<PieEntry> yVals1 = new ArrayList<>();
        // drawn above each other.
        for (int i = 0; i < targetAchievementModels.size(); i++)
            yVals1.add(new PieEntry(Float.valueOf(targetAchievementModels.get(i).getValue()),
                    targetAchievementModels.get(i).getLabel(), i));

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
                addtionalParameterMap.put(KEY,
                        targetAchievementModels.get((int) h.getX()).getLabel());
                addtionalParameterMap.put(WIDGETTYPE,
                        targetAchievementModels.get((int) h.getX()).getWidgetType().getWidgetTypes());
                secondClickTarget(addtionalParameterMap);
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

        if (id == R.id.ivLeftArrowThird) {
            llFirst.setVisibility(View.GONE);
            llSecond.setVisibility(View.VISIBLE);
            llThird.setVisibility(View.GONE);
        }

        if (id == R.id.linearLayoutCenter) {
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
        navigator.openPage(NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
                true, false);
    }

    private Map<String, String> getAddtionalParameterMap() {
        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {

            addtionalParameterMap.put(getPeriod().getKeyValue(), getPeriod().getValue());
            addtionalParameterMap.put(SELF_KEY, dashboardModel.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    private List<TargetAchievementModel> getTargetAchievementModel(List<TableItem> items) {
        if (items == null || items.size() == 0) return null;
        List<TargetAchievementModel> list = new ArrayList<>();
        for (int ctr = 0; ctr < items.size(); ctr++) {
            try {
                TableItem item = items.get(ctr);
                if (item == null) return null;

                String value = stringOrZero(item.value);
                String label = stringOrZero(item.label);
                String formatted = stringOrZero(item.formatted);
                String widgetType = stringOrZero(item.widgetType);
                list.add(new TargetAchievementModel(label, formatted, value,
                        new WidgetType(widgetType, null)));
            } catch (Exception ex) {
                ex.printStackTrace();
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

    private void render(UiModel<TargetAchievementTeamItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            updateScreenOnException();
            showOnFailurePopup(error);
        }

        TargetAchievementTeamItem data = uiModel.data;
        if (data != null) {
            try {
                renderTargetAchievementTeam(data);
            } catch (Exception e) {
                updateScreenOnException();
            }
        }
    }

    private void updateScreenOnException() {
        tvErrorFirst.setVisibility(View.VISIBLE);
        tvUniqueLeadsFirst.setText("");
        verticalAdapterFirst =
                new VerticalAdapterFirst(targetAchievementModelsFirst);
        verticalRecyclerViewFirst.setLayoutManager(
                new GridLayoutManager(requireContext(), 3));
        verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
        verticalAdapterFirst.notifyDataSetChanged();
        populatePieChartFirst(targetAchievementModelsFirst);

        tvErrorSecond.setVisibility(View.VISIBLE);
        tvUniqueLeadsSecond.setText("");
        verticalAdapterSecond =
                new VerticalAdapterSecond(targetAchievementModelsSecond);
        verticalRecyclerViewSecond.setLayoutManager(
                new GridLayoutManager(requireContext(), 3));
        verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
        verticalAdapterSecond.notifyDataSetChanged();
        populatePieChartSecond(targetAchievementModelsSecond);

        tvErrorThird.setVisibility(View.VISIBLE);
        tvUniqueLeadsThird.setText("");
        verticalAdapterThird =
                new VerticalAdapterThird(targetAchievementModelsThird);
        verticalRecyclerViewThird.setLayoutManager(
                new GridLayoutManager(requireContext(), 3));
        verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
        verticalAdapterThird.notifyDataSetChanged();
        populatePieChartThird(targetAchievementModelsThird);
    }

    private void webServiceCallFirst() {
        llSecond.setVisibility(View.GONE);
        llFirst.setVisibility(View.VISIBLE);
        llThird.setVisibility(View.GONE);
        targetAchievementModelsFirst = new ArrayList<>();
        targetAchievementModelsSecond = new ArrayList<>();
        targetAchievementModelsThird = new ArrayList<>();
        tvUniqueLeadsFirst.setText("");
        tvUniqueLeadsSecond.setText("");
        tvUniqueLeadsThird.setText("");
        tvErrorFirst.setVisibility(View.GONE);
        tvErrorSecond.setVisibility(View.GONE);
        tvErrorThird.setVisibility(View.GONE);
        populatePieChartFirst(targetAchievementModelsFirst);
        populatePieChartSecond(targetAchievementModelsSecond);
        populatePieChartThird(targetAchievementModelsThird);

        if (CRMAppUtil.isNetworkEnabled(requireContext())) {
            String businessProfileId = dashboardModel.getBusinessProfileIdForDashboard();
            ClientRequest request = new ClientRequest(
                    businessProfileId,
                    getAddtionalParameterMap(),
                    widgetSystemName,
                    dashboardModel.getProductId(),
                    dashboardModel.getEmployeeCode()
            );
            Disposable subscribe = getSaathiApi().getTargetAchievementTeam(request)
                    .compose(applyUiModel())
                    .subscribe(this::render);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderTargetAchievementTeam(TargetAchievementTeamItem response) {
        targetAchievementModelsSecond.clear();
        targetAchievementModelsFirst.clear();
        targetAchievementModelsThird.clear();
        if (response.table3 != null) {
            List<Table3Item> table3Items = response.table3;
            int arrayLength;
            if (table3Items.size() == 5) {
                arrayLength = 3;
            } else {
                arrayLength = table3Items.size();
            }
            if (table3Items.size() != 0) {
                ivView.setVisibility(View.VISIBLE);
                llTopFirst.setVisibility(View.VISIBLE);
                for (int i = 0; i < arrayLength; i++) {
                    if (arrayLength == 3) {
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        linearLayoutRight.setVisibility(View.VISIBLE);
                        linearLayoutCenter.setVisibility(View.VISIBLE);
                        Table3Item item = table3Items.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrZero(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                if (getPeriod().getValue().equalsIgnoreCase("DAY")
                                        || getPeriod().getValue().equalsIgnoreCase("WTD")) {
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
                    } else if (table3Items.size() == 2) {
                        linearLayoutRight.setVisibility(View.GONE);
                        linearLayoutCenter.setVisibility(View.VISIBLE);
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        Table3Item item = table3Items.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrEmpty(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                if (getPeriod().getValue().equalsIgnoreCase("DAY")
                                        || getPeriod().getValue().equalsIgnoreCase("WTD")) {
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
                    } else if (table3Items.size() == 1) {
                        linearLayoutRight.setVisibility(View.GONE);
                        linearLayoutCenter.setVisibility(View.GONE);
                        linearLayoutLeft.setVisibility(View.VISIBLE);
                        Table3Item item = table3Items.get(i);
                        String displayText = stringOrEmpty(item.displayText);
                        String value = stringOrEmpty(item.value);
                        int ord = Integer.valueOf(stringOrZero(item.ord));
                        if (ord == 1) {
                            leftStr = displayText;
                            if (!displayText.equalsIgnoreCase("Target")) {
                                txtLeftStr.setText(displayText);
                            } else {
                                if (getPeriod().getValue().equalsIgnoreCase("DAY")
                                        || getPeriod().getValue().equalsIgnoreCase("WTD")) {
                                    txtLeftStr.setText("MTD " + displayText);
                                } else {
                                    txtLeftStr.setText(displayText);
                                }
                            }
                            txtLeftValue.setText(value);
                        }
                    }
                }
            }
        }

        Map<String, List<TargetAchievementModel>> dataMap = new HashMap();
        List<TableItem> tableItems = response.table;
        List<TableItem> table1Items = response.table1;
        List<TableItem> table2Items = response.table2;

        dataMap.put("Table", getTargetAchievementModel(tableItems));
        dataMap.put("Table1", getTargetAchievementModel(table1Items));
        dataMap.put("Table2", getTargetAchievementModel(table2Items));

        List<TargetAchievementModel> tableDataFirst = null;
        if (!widgetTypeList.isEmpty()) {
            tableDataFirst = getDataByWidgetType(dataMap, widgetTypeList.get(0));
        }

        if (tableDataFirst == null || tableDataFirst.isEmpty()) {
            tvErrorFirst.setVisibility(View.VISIBLE);
            tvUniqueLeadsFirst.setText(widgetTypeList.get(0).getTitle());
            verticalAdapterFirst = new VerticalAdapterFirst(targetAchievementModelsFirst);
            verticalRecyclerViewFirst.setLayoutManager(
                    new GridLayoutManager(requireContext(), 3));
            verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
            verticalAdapterFirst.notifyDataSetChanged();
            populatePieChartFirst(targetAchievementModelsFirst);
        } else {
            for (TargetAchievementModel model : tableDataFirst) {
                targetAchievementModelsFirst.add(model);
                tvUniqueLeadsFirst.setText(widgetTypeList.get(0).getTitle());
                verticalAdapterFirst = new VerticalAdapterFirst(targetAchievementModelsFirst);
                verticalRecyclerViewFirst.setLayoutManager(
                        new GridLayoutManager(requireContext(), 3));
                verticalRecyclerViewFirst.setAdapter(verticalAdapterFirst);
                verticalAdapterFirst.notifyDataSetChanged();
                populatePieChartFirst(targetAchievementModelsFirst);
            }
        }

        List<TargetAchievementModel> tableDataSecond = null;
        if (!widgetTypeList.isEmpty()) {
            tableDataSecond = getDataByWidgetType(dataMap, widgetTypeList.get(1));
        }

        if (tableDataSecond == null || tableDataSecond.isEmpty()) {
            tvErrorSecond.setVisibility(View.VISIBLE);
            tvUniqueLeadsSecond.setText(widgetTypeList.get(1).getTitle());
            verticalAdapterSecond = new VerticalAdapterSecond(targetAchievementModelsSecond);
            verticalRecyclerViewSecond.setLayoutManager(
                    new GridLayoutManager(requireContext(), 3));
            verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
            verticalAdapterSecond.notifyDataSetChanged();
            populatePieChartSecond(targetAchievementModelsSecond);
        } else {
            for (TargetAchievementModel model : tableDataSecond) {
                targetAchievementModelsSecond.add(model);
                tvUniqueLeadsSecond.setText(widgetTypeList.get(1).getTitle());
                verticalAdapterSecond =
                        new VerticalAdapterSecond(targetAchievementModelsSecond);
                verticalRecyclerViewSecond.setLayoutManager(
                        new GridLayoutManager(requireContext(), 3));
                verticalRecyclerViewSecond.setAdapter(verticalAdapterSecond);
                verticalAdapterSecond.notifyDataSetChanged();
                populatePieChartSecond(targetAchievementModelsSecond);
            }
        }

        List<TargetAchievementModel> tableDataThird = null;
        if (!widgetTypeList.isEmpty()) {
            tableDataThird = getDataByWidgetType(dataMap, widgetTypeList.get(2));
        }

        if (tableDataThird == null || tableDataThird.isEmpty()) {
            tvErrorThird.setVisibility(View.VISIBLE);
            tvUniqueLeadsThird.setText(widgetTypeList.get(2).getTitle());
            verticalAdapterThird = new VerticalAdapterThird(targetAchievementModelsThird);
            verticalRecyclerViewThird.setLayoutManager(
                    new GridLayoutManager(requireContext(), 3));
            verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
            verticalAdapterThird.notifyDataSetChanged();
            populatePieChartThird(targetAchievementModelsThird);
        } else {
            for (TargetAchievementModel model : tableDataThird) {
                targetAchievementModelsThird.add(model);
                tvUniqueLeadsThird.setText(widgetTypeList.get(2).getTitle());
                verticalAdapterThird = new VerticalAdapterThird(targetAchievementModelsThird);
                verticalRecyclerViewThird.setLayoutManager(
                        new GridLayoutManager(requireContext(), 3));
                verticalRecyclerViewThird.setAdapter(verticalAdapterThird);
                verticalAdapterThird.notifyDataSetChanged();
                populatePieChartThird(targetAchievementModelsThird);
            }
        }
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private List<WidgetModel> widgetModelList;
        private Context context;
        HorizontalAdapter(List<WidgetModel> widgetModelList) {
            this.widgetModelList = widgetModelList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_list, parent, false);
            context = parent.getContext();
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NotNull final MyViewHolder holder,
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
                widgetTypeList = widgetModel.getWidgetTypeList();
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
                    txtCenterValue.setText("");
                    txtRightValue.setText("");
                    txtCenterValue.setText("");
                    tvUniqueLeadsFirst.setText("");
                    llTopFirst.setVisibility(View.GONE);
                    ivView.setVisibility(View.GONE);
                    tvUniqueLeadsSecond.setText("");
                    tvUniqueLeadsThird.setText("");
                }
            });
        }

        void setImage(String imageUrl, final ImageView imageView) {

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
                                    RoundedBitmapDrawableFactory.create(context.getResources(),
                                            resource);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvWidgetName;
            public ImageView imageView;
            RelativeLayout llWidgetList;
            CardView card_view;

            MyViewHolder(View view) {
                super(view);
                tvWidgetName = view.findViewById(R.id.tvWidgetName);
                imageView = view.findViewById(R.id.imageView);
                llWidgetList = view.findViewById(R.id.llWidgetList);
                card_view = view.findViewById(R.id.card_view);
            }
        }

        @Override
        public int getItemCount() {
            return widgetModelList.size();
        }
    }

    public class VerticalAdapterFirst
            extends RecyclerView.Adapter<VerticalAdapterFirst.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        VerticalAdapterFirst(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new MyViewHolder(itemView);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvPercentage, tvLabel;
            public View imageView;

            MyViewHolder(View view) {
                super(view);
                tvPercentage = view.findViewById(R.id.tvPercentage);
                tvLabel = view.findViewById(R.id.tvLabel);
                imageView = view.findViewById(R.id.view);
            }
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
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

        VerticalAdapterSecond(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder,
                                     int position) {
            final TargetAchievementModel widgetModel = targetAchievementModelList.get(position);
            holder.tvLabel.setText(widgetModel.getLabel());
            holder.tvPercentage.setText(widgetModel.getForrmated());
            holder.imageView.setBackgroundColor(VORDIPLOM_COLORS[position]);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvPercentage, tvLabel;
            public View imageView;

            MyViewHolder(View view) {
                super(view);
                tvPercentage = view.findViewById(R.id.tvPercentage);
                tvLabel = view.findViewById(R.id.tvLabel);
                imageView = view.findViewById(R.id.view);
            }
        }

        @Override
        public int getItemCount() {
            return targetAchievementModelsSecond.size();
        }
    }

    public class VerticalAdapterThird
            extends RecyclerView.Adapter<VerticalAdapterThird.MyViewHolder> {

        private List<TargetAchievementModel> targetAchievementModelList;

        VerticalAdapterThird(List<TargetAchievementModel> targetAchievementModelList) {
            this.targetAchievementModelList = targetAchievementModelList;
        }

        @NotNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_target_achievement_dynamic_list, parent, false);

            return new MyViewHolder(itemView);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tvPercentage, tvLabel;
            public View imageView;

            MyViewHolder(View view) {
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
            return targetAchievementModelsThird.size();
        }
    }
}
