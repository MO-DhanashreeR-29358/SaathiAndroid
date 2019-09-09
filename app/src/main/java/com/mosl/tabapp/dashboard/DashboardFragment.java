package com.mosl.tabapp.dashboard;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.mosl.app.core.data.UiModel;
import com.mosl.app.core.exception.ServiceFailureException;
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountItem;
import com.mosl.saathi.api.model.dashboard.actionablecount.ActionableCountRequest;
import com.mosl.saathi.api.model.dashboard.actionablecount.Table1Item;
import com.mosl.saathi.api.model.dashboard.actionablecount.TableItem;
import com.mosl.saathi.api.model.dashboard.client.ClientRequest;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableFilterItem;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.AvailableWidgetsItem;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataItem;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.DashboardMetaDataRequest;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.FilterData;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.GroupsItem;
import com.mosl.saathi.api.model.dashboard.dashboardmetadata.WidgetTypeItem;
import com.mosl.saathi.api.model.dashboard.incentive.IncentiveItem;
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyItem;
import com.mosl.saathi.api.model.dashboard.incentivepolicy.IncentivePolicyRequest;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMDecimalValueFormater;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.TypeActionableFilterModel;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.model.dashboard.WidgetType;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ui.SplashActivity;
import io.reactivex.disposables.Disposable;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.mosl.app.core.data.UiModelHelper.applyUiModel;
import static com.mosl.tabapp.model.AccountDetails.isAufsb;
import static com.mosl.tabapp.ui.MOSLCommonActivity.getInputBody;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

/**
 * Created by raju on 2/7/2017.
 */

public class DashboardFragment extends MOSLCommonFragment
    implements View.OnClickListener, MOSLConstants {
    private final int itemcount = 4;
    Bundle args = new Bundle();
    private String businessProfileIdForDashboard, productId, employeeCode, dashboardId = "";
    private View dashAdvisoryView;
    private TextView txtClients, txtTarget, tvMain, txtTelephoneNo1, txtTelephoneNo2;
    private LinearLayout llClient, llTarget, llPerformanceAndIncentives, llBusinessSnapshotHead,
        llClientsLeads, llTargetAchievementADV, llTeamTargetAchievement,
        llPerformanceAndIncentivesHeader, lldashboardAufsb;
    private ImageView ivActionableInsights, ivClientLeads, ivTargetAchievementADV,
        ivTeamTargetAchievement, ivMonthOnMonth, ivBusinessSnapshotHead,
        ivPerformanceAndIncentivesHeader;
    //
    //Subb button
    private Button btnClient, btnLeads, btnBusinessSnapshot, btnClientTraded, btnProductivity,
        btnBusinessSnapshotAdvisor;
    private AdvisoryActionableInsightAdapter advisoryActionableInsightAdapter;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton leadcreation;
    private RelativeLayout llMain, rlClientLeads, rlTargetAchievementADV, rlTeamTargetAchievement,
        rlMonthOnMonth, rlBusinessSnapshotHead, rlPerformanceAndIncentivesHeader,
        rlFreshRegistration, rlExisting;
    private Dialog dialog;
    //Team
    private LinearLayout llTeamTarget;

    //Hierarchy
    private TextView txtTeamTarget;
    private TableLayout tableMData, tableCurrentQuaterStatusThree, tableCurrentQuaterStatusFour,
        tableAdvPerformance, tableCurrentQuaterStatusFirst,
        tableCurrentQuaterStatusSecond;
    private LinearLayout llSnapshot;
    private TextView tvSnapshot, txtMonthTrends;

    private LinearLayout llMonthTrend, llMonthvsTrends;
    private List<DashboardModel> dashboardList = new ArrayList<>();
    //incentive
    private List<FloatingActionButton> dashboardBtnList = new ArrayList<>();
    private DashboardModel currentDashboard;
    private TextView tvIncentives, tvPerformance, tvPerformanceAndIncentivesHeader,
        tvCurrentQuaterStatusThreeHeading, tvCurrentQuaterStatusFourHeading,
        tvCurrentQuaterStatusFirstHeading, tvCurrentQuaterStatusSecondHeading;
    private Spinner spQuarter;
    //Adviosory Performance
    private BarChart mChartAdv;
    private ArrayList<String> yDataGraphArrAdv = new ArrayList<>();

    private TextView tvDetailIncentive;
    private ArrayList<HashMap<String, String>> arrayListIncentive =
        new ArrayList<HashMap<String, String>>();
    private LinearLayout llIncentive, llCurrentQuaterStatusThree, llCurrentQuaterStatusFour,
        llCurrentQuaterStatusFirst, llCurrentQuaterStatusSecond, llCurrentQuarterStatusAdviosory,
        llAdvPerformance, llTeamPerformance, llCurrentQuarterStatusOneUp, llEmail;
    private ScrollView scrollView;

    private XAxis xAxis;
    private String[] mMonths;
    ////////////////////
    // Progress Dialog
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        dashAdvisoryView = super.onCreateView(inflater, container, savedInstanceState);
        attachLayout(R.layout.frag_dashboard);
        setUpView(dashAdvisoryView);
        hideAppTitle(getClass().toString());

        try {
            //CHECK IF CALLED FROM SEARCH RESULT
            Bundle bund = this.getArguments();
            if (bund == null) {//No argument passedise my own profile id
                businessProfileIdForDashboard = AccountDetails.getBusinessProfileId();
            } else {
                businessProfileIdForDashboard = bund.getString(PROFILE_ID);
                productId = bund.getString(PRODUCT_ID);
                employeeCode = bund.getString(EMPLOYEE_CODE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setUserVisibleHint(true);

        return dashAdvisoryView;
    }

    private void setUpView(View dashAdvisoryView) {
        scrollView = dashAdvisoryView.findViewById(R.id.scrollView);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        tvDetailIncentive = dashAdvisoryView.findViewById(R.id.tvDetailIncentive);
        tvDetailIncentive.setOnClickListener(this);
        llMain = dashAdvisoryView.findViewById(R.id.llMain);
        floatingActionsMenu = dashAdvisoryView.findViewById(R.id.floatingActionsMenu);
        leadcreation = dashAdvisoryView.findViewById(R.id.leadcreation);

        ivActionableInsights = dashAdvisoryView.findViewById(R.id.ivActionableInsights);
        ivClientLeads = dashAdvisoryView.findViewById(R.id.ivClientLeads);
        ivTargetAchievementADV = dashAdvisoryView.findViewById(R.id.ivTargetAchievementADV);
        ivTeamTargetAchievement = dashAdvisoryView.findViewById(R.id.ivTeamTargetAchievement);
        ivMonthOnMonth = dashAdvisoryView.findViewById(R.id.ivMonthOnMonth);
        ivBusinessSnapshotHead = dashAdvisoryView.findViewById(R.id.ivBusinessSnapshotHead);
        ivPerformanceAndIncentivesHeader =
            dashAdvisoryView.findViewById(R.id.ivPerformanceAndIncentivesHeader);
        txtClients = dashAdvisoryView.findViewById(R.id.txtClients);
        txtTarget = dashAdvisoryView.findViewById(R.id.txtTarget);
        txtTeamTarget = dashAdvisoryView.findViewById(R.id.txtTeamTarget);
        txtTelephoneNo1 = dashAdvisoryView.findViewById(R.id.txtTelephoneNo1);
        txtTelephoneNo2 = dashAdvisoryView.findViewById(R.id.txtTelephoneNo2);
        tvPerformanceAndIncentivesHeader =
            dashAdvisoryView.findViewById(R.id.tvPerformanceAndIncentivesHeader);
        tvMain = dashAdvisoryView.findViewById(R.id.tvMain);
        txtTeamTarget.setText(ADVISORY_TARGET_TITLE);
        btnLeads = dashAdvisoryView.findViewById(R.id.btnLeads);
        btnProductivity = dashAdvisoryView.findViewById(R.id.btnProductivity);
        btnBusinessSnapshotAdvisor = dashAdvisoryView.findViewById(R.id.btnBusinessSnapshotAdvisor);
        btnClient = dashAdvisoryView.findViewById(R.id.btnClient);

        llClient = dashAdvisoryView.findViewById(R.id.llClient);
        lldashboardAufsb = dashAdvisoryView.findViewById(R.id.lldashboardAufsb);
        llClientsLeads = dashAdvisoryView.findViewById(R.id.llClientsLeads);
        llTargetAchievementADV =
            dashAdvisoryView.findViewById(R.id.llTargetAchievementADV);
        llTeamTargetAchievement =
            dashAdvisoryView.findViewById(R.id.llTeamTargetAchievement);
        llPerformanceAndIncentivesHeader =
            dashAdvisoryView.findViewById(R.id.llPerformanceAndIncentivesHeader);
        rlClientLeads = dashAdvisoryView.findViewById(R.id.rlClientLeads);
        rlExisting = dashAdvisoryView.findViewById(R.id.rlExisting);
        rlFreshRegistration = dashAdvisoryView.findViewById(R.id.rlFreshRegistration);
        rlTargetAchievementADV =
            dashAdvisoryView.findViewById(R.id.rlTargetAchievementADV);
        rlTeamTargetAchievement =
            dashAdvisoryView.findViewById(R.id.rlTeamTargetAchievement);
        rlMonthOnMonth = dashAdvisoryView.findViewById(R.id.rlMonthOnMonth);
        rlBusinessSnapshotHead =
            dashAdvisoryView.findViewById(R.id.rlBusinessSnapshotHead);
        rlPerformanceAndIncentivesHeader =
            dashAdvisoryView.findViewById(R.id.rlPerformanceAndIncentivesHeader);

        btnClientTraded = dashAdvisoryView.findViewById(R.id.btnClientTraded);
        btnBusinessSnapshot = dashAdvisoryView.findViewById(R.id.btnBusinessSnapshot);

        llTarget = dashAdvisoryView.findViewById(R.id.llTarget);
        llTeamTarget = dashAdvisoryView.findViewById(R.id.llTeamTarget);
        llPerformanceAndIncentives =
            dashAdvisoryView.findViewById(R.id.llPerformanceAndIncentives);
        llMonthTrend = dashAdvisoryView.findViewById(R.id.llMonthTrend);
        llMonthvsTrends = dashAdvisoryView.findViewById(R.id.llMonthvsTrends);
        llBusinessSnapshotHead =
            dashAdvisoryView.findViewById(R.id.llBusinessSnapshotHead);

        llIncentive = dashAdvisoryView.findViewById(R.id.llIncentive);
        llCurrentQuaterStatusThree =
            dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusThree);
        llCurrentQuaterStatusFour =
            dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusFour);
        llCurrentQuaterStatusFirst =
            dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusFirst);
        llCurrentQuaterStatusSecond =
            dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusSecond);
        llCurrentQuarterStatusAdviosory =
            dashAdvisoryView.findViewById(R.id.llCurrentQuarterStatusAdviosory);
        llCurrentQuarterStatusOneUp =
            dashAdvisoryView.findViewById(R.id.llCurrentQuarterStatusOneUp);
        llAdvPerformance = dashAdvisoryView.findViewById(R.id.llAdvPerformance);
        llTeamPerformance = dashAdvisoryView.findViewById(R.id.llTeamPerformance);
        llEmail = dashAdvisoryView.findViewById(R.id.llEmail);

        rlClientLeads.setOnClickListener(this);
        rlTargetAchievementADV.setOnClickListener(this);
        rlTeamTargetAchievement.setOnClickListener(this);
        rlMonthOnMonth.setOnClickListener(this);
        rlBusinessSnapshotHead.setOnClickListener(this);
        rlPerformanceAndIncentivesHeader.setOnClickListener(this);
        //Hierarchy

        llSnapshot = dashAdvisoryView.findViewById(R.id.llSnapshot);
        tvSnapshot = dashAdvisoryView.findViewById(R.id.tvSnapshot);
        txtMonthTrends = dashAdvisoryView.findViewById(R.id.txtMonthTrends);
        tvSnapshot.setText(BUSINESS_SNAPSHOT_TITLE);
        txtMonthTrends.setText(TRENDS_TITLE);

        btnClient.setOnClickListener(this);
        btnBusinessSnapshot.setOnClickListener(this);
        btnLeads.setOnClickListener(this);
        btnProductivity.setOnClickListener(this);
        btnBusinessSnapshotAdvisor.setOnClickListener(this);

        ivActionableInsights.setOnClickListener(this);
        leadcreation.setOnClickListener(this);

        btnClientTraded.setOnClickListener(this);

        if (isAufsb()) floatingActionsMenu.setVisibility(View.GONE);

        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(
            new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                @Override
                public void onMenuExpanded() {
                    llMain.setBackgroundColor(getResources().getColor(R.color.black));
                    llMain.getBackground().setAlpha(180);
                    llMain.setVisibility(View.VISIBLE);
                }

                @Override
                public void onMenuCollapsed() {
                    llMain.setBackgroundColor(getResources().getColor(R.color.light_black));
                    llMain.getBackground().setAlpha(0);
                    llMain.setVisibility(View.GONE);
                }
            });
        llMain.setOnClickListener(this);
        rlFreshRegistration.setOnClickListener(this);
        rlExisting.setOnClickListener(this);
        txtTelephoneNo2.setOnClickListener(this);
        txtTelephoneNo1.setOnClickListener(this);
        llEmail.setOnClickListener(this);

        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.black));

        //incentive

        spQuarter = dashAdvisoryView.findViewById(R.id.spQuarter);
        tableMData = dashAdvisoryView.findViewById(R.id.tableMData);
        tableCurrentQuaterStatusFirst =
            dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusFirst);
        tableCurrentQuaterStatusSecond =
            dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusSecond);
        tableCurrentQuaterStatusThree =
            dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusThree);
        tableCurrentQuaterStatusFour =
            dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusFour);
        tableAdvPerformance = dashAdvisoryView.findViewById(R.id.tableAdvPerformance);
        spQuarter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                int position,
                long id) {

                incentiveView(spQuarter.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
        String[] quarterArray = getActivity().getResources().getStringArray(R.array.SelectQuarter);
        ArrayList<String> arrEventType = new ArrayList<String>(Arrays.asList(quarterArray));
        ArrayAdapter<String> arrAdapEventType =
            new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrEventType);
        arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
        spQuarter.setAdapter(arrAdapEventType);
        tvIncentives = dashAdvisoryView.findViewById(R.id.tvIncentives);
        tvPerformance = dashAdvisoryView.findViewById(R.id.tvPerformance);
        tvCurrentQuaterStatusFirstHeading =
            dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusFirstHeading);
        tvCurrentQuaterStatusSecondHeading =
            dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusSecondHeading);
        tvCurrentQuaterStatusThreeHeading =
            dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusThreeHeading);
        tvCurrentQuaterStatusFourHeading =
            dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusFourHeading);

        mChartAdv = (BarChart) dashAdvisoryView.findViewById(R.id.chart);
        mChartAdv.getDescription().setEnabled(false);
        mChartAdv.setPinchZoom(false);
        mChartAdv.setDrawBarShadow(false);
        mChartAdv.setDrawGridBackground(false);

        Legend legendAdv = mChartAdv.getLegend();
        legendAdv.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendAdv.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendAdv.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendAdv.setDrawInside(true);
        legendAdv.setYOffset(0f);
        legendAdv.setXOffset(10f);
        legendAdv.setYEntrySpace(0f);
        legendAdv.setTextSize(8f);
        legendAdv.setEnabled(false);

        xAxis = mChartAdv.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis leftAxis = mChartAdv.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceTop(5f);

        mChartAdv.getAxisRight().setEnabled(false);
        mChartAdv.setScaleXEnabled(false);
        mChartAdv.setScaleYEnabled(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[] { "Q1", "Q2", "Q3", "Q4", "YTD" };

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });

        if (isAufsb()) {
            rlClientLeads.setVisibility(View.GONE);
            btnLeads.setText(DASHBOARD);
        }
    }

    private void generateAxisLabel(ChartDataModel data) {
        if (data != null) {
            LinkedHashMap map = data.getMonthData();

            if (data.getMonthData() != null && map.size() != 0) {
                Set<String> monthNames = map.keySet();
                mMonths = monthNames.toArray(new String[monthNames.size()]);
            }
        }
    }

    private int getColor(int index) {
        switch (index) {
            case 0:
                return Color.rgb(60, 220, 78);
            case 1:
                return Color.rgb(61, 165, 255);
            default:
                return Color.rgb(38, 127, 183);
        }
    }

    private BarData generateBarData(List<ChartDataModel> barDataList) {

        generateAxisLabel(barDataList.get(0));
        if (barDataList.size() == 0) return null;

        BarData d = new BarData();

        List<ArrayList<BarEntry>> entryList = new ArrayList<>();

        for (int x = 0; x < barDataList.size(); x++) {
            ArrayList<BarEntry> entry = new ArrayList<BarEntry>();
            entryList.add(entry);
        }
        BarDataSet set1 = null;
        for (int x = 0; x < barDataList.size(); x++) {
            ChartDataModel model = barDataList.get(x);
            ArrayList<BarEntry> entries = entryList.get(x);
            for (int y = 0; y < mMonths.length; y++) {
                if (barDataList.size() == 1) {
                    entries.add(new BarEntry(y + 0.2f, model.getMonthData().get(mMonths[y])));
                } else {
                    entries.add(new BarEntry(0, model.getMonthData().get(mMonths[y])));
                }
                set1 = new BarDataSet(entries, "");
                set1.setColor(getColor(x));
                set1.setValueTextColor(getColor(x));
                set1.setValueTextSize(10f);
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setValueFormatter(new CRMDecimalValueFormater());
            }
            d.addDataSet(set1);
        }

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.37f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        // make this BarData object grouped
        d.setBarWidth(barWidth);
        if (barDataList.size() > 1) {
            d.groupBars(0, groupSpace, barSpace); // start at x = 0
        } else {

        }

        return d;
    }

    public void advLoadChart(
        List<com.mosl.saathi.api.model.dashboard.incentive.Table1Item> table1Items) {
        String ytd = "";
        float groupSpace = 0.25f;
        float barSpace = 0.05f;
        float barWidth = 0.35f;
        List<ChartDataModel> dataList = new ArrayList<ChartDataModel>();
        for (int i = 0; i < table1Items.size(); i++) {
            ChartDataModel model = new ChartDataModel();
            com.mosl.saathi.api.model.dashboard.incentive.Table1Item item = table1Items.get(i);
            String Name = stringOrEmpty(item.name);
            String Q1 = stringOrZero(item.q1);
            String Q2 = stringOrZero(item.q2);
            String Q3 = stringOrZero(item.q3);
            String Q4 = stringOrZero(item.q4);
            String WidgetType = stringOrEmpty(item.widgetType);
            if (item.yTD != null) {
                ytd = stringOrZero(item.yTD);
                yDataGraphArrAdv.add(ytd);
            }
            yDataGraphArrAdv.add(Name);
            yDataGraphArrAdv.add(Q1);
            yDataGraphArrAdv.add(Q2);
            yDataGraphArrAdv.add(Q3);
            yDataGraphArrAdv.add(Q4);
            yDataGraphArrAdv.add(WidgetType);
            model.getMonthData().put("Q1", Float.parseFloat(Q1));
            model.getMonthData().put("Q2", Float.parseFloat(Q2));
            model.getMonthData().put("Q3", Float.parseFloat(Q3));
            model.getMonthData().put("Q4", Float.parseFloat(Q4));

            if (item.yTD != null) {
                model.getMonthData().put("YTD", Float.parseFloat(ytd));
            }
            dataList.add(model);
        }

        if (generateBarData(dataList) != null) {
            mChartAdv.setData(generateBarData(dataList));
        }

        int groupCount = mMonths.length;
        int startYear = 2010;
        int endYear = 2015;
        tableAdvPerformance.removeAllViews();
        //Adding head
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View tableRowHeader = layoutInflater.inflate(R.layout.frag_performance_team_tab_row, null);
        TextView tvTeamAvgHeader = (TextView) tableRowHeader.findViewById(R.id.tvTeamAvg);
        TextView tvNationalAvgHeader = (TextView) tableRowHeader.findViewById(R.id.tvNationalAvg);
        TextView tvIncentivesHeader = (TextView) tableRowHeader.findViewById(R.id.tvIncentives);
        tvIncentivesHeader.setText(yDataGraphArrAdv.get(0));
        tvIncentivesHeader.setTypeface(null, Typeface.BOLD);
        tvIncentivesHeader.setTextColor(Color.rgb(60, 220, 78));
        tvTeamAvgHeader.setText(yDataGraphArrAdv.get(6));
        tvTeamAvgHeader.setTypeface(null, Typeface.BOLD);
        tvTeamAvgHeader.setTextColor(Color.rgb(61, 165, 255));
        if (dataList.size() == 3) {
            tvNationalAvgHeader.setVisibility(View.VISIBLE);
            tvNationalAvgHeader.setText(yDataGraphArrAdv.get(12));
            tvNationalAvgHeader.setTypeface(null, Typeface.BOLD);
            tvNationalAvgHeader.setTextColor(Color.rgb(38, 127, 183));
        }

        tableAdvPerformance.addView(tableRowHeader);
        for (int i = 1; i < mMonths.length + 1; i++) {
            View tableRow = layoutInflater.inflate(R.layout.frag_performance_team_tab_row, null);
            TextView tvTeamAvg = (TextView) tableRow.findViewById(R.id.tvTeamAvg);
            TextView tvNationalAvg = (TextView) tableRow.findViewById(R.id.tvNationalAvg);
            TextView tvIncentives = (TextView) tableRow.findViewById(R.id.tvIncentives);

            tvIncentives.setText(yDataGraphArrAdv.get(i));
            tvTeamAvg.setText(yDataGraphArrAdv.get(6 + i));
            if (dataList.size() == 3) {
                tvNationalAvg.setVisibility(View.VISIBLE);
                tvNationalAvg.setText(yDataGraphArrAdv.get(12 + i));
                groupSpace = 0.18f;
                barSpace = 0.03f;
                barWidth = 0.25f;
            }
            tableAdvPerformance.addView(tableRow);
        }

        // specify the width each bar should have
        mChartAdv.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChartAdv.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChartAdv.getXAxis()
            .setAxisMaximum(
                startYear
                    + mChartAdv.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mChartAdv.groupBars(startYear, groupSpace, barSpace);
        mChartAdv.invalidate();

        llAdvPerformance.setVisibility(View.VISIBLE);
        tvPerformance.setText("Performance");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Bundle args = new Bundle();
        try {
            if (id == R.id.tvDetailIncentive) {
                if (!dashboardId.isEmpty()) {
                    webServiceInceniveSlab(dashboardId);
                }
            }
            if (id == R.id.ivActionableInsights) {
                hideKeyboard(view);
                actionableInsightDialog();
            } else if (id == R.id.btnClient) {
                hideKeyboard(view);
                loadClients();
            } else if (id == R.id.btnBusinessSnapshot) {
                hideKeyboard(view);
                loadLeadBusinessSnapShot();
            } else if (id == R.id.btnLeads) {
                hideKeyboard(view);
                loadLead();
            } else if (id == R.id.btnProductivity) {
                hideKeyboard(view);
                loadProductivity();
            } else if (id == R.id.btnBusinessSnapshotAdvisor) {
                hideKeyboard(view);
                loadBusinessSnapShotAdvisor();
            } else if (id == R.id.btnClientTraded) {
                hideKeyboard(view);
                loadClientTraded();
            } else if (id == R.id.leadcreation) {
                hideKeyboard(view);
                floatingActionsMenu.collapse();
                navigateTo(NAV_TO_LEAD_CREATION, args, getFragmentManager(), true);
            } else if (id == R.id.llMain) {
                floatingActionsMenu.collapse();
                llMain.setVisibility(View.GONE);
            } else if (id == R.id.rlClientLeads) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            } else if (id == R.id.rlTargetAchievementADV) {
                toggleSectionExpansion(llTargetAchievementADV, ivTargetAchievementADV);
            } else if (id == R.id.rlMonthOnMonth) {
                toggleSectionExpansion(llMonthvsTrends, ivMonthOnMonth);
            } else if (id == R.id.rlBusinessSnapshotHead) {
                toggleSectionExpansion(llBusinessSnapshotHead, ivBusinessSnapshotHead);
            } else if (id == R.id.rlTeamTargetAchievement) {
                toggleSectionExpansion(llTeamTargetAchievement, ivTeamTargetAchievement);
            } else if (id == R.id.rlPerformanceAndIncentivesHeader) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader,
                    ivPerformanceAndIncentivesHeader);
            } else if (id == R.id.rlFreshRegistration) {
                Map<String, String> map = new HashMap();
                map.put(NEW_USERID, "");
                map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                map.put(COMMANDTYPE, "NEW");
                map.put(APPLICATIONTYPE, "CRM");
                map.put(OSTYPE, "ANDROID");
                map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                map.put(TOKENKEY, AccountDetails.getToken());
                map.put(ISAUFSB, String.valueOf(AccountDetails.isAufsb()));
                gotoNewEkyc(getInputBody(map));
            } else if (id == R.id.rlExisting) {
                HashMap<String, String> map = new HashMap();
                map.put(NEW_USERID, "");
                map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                map.put(COMMANDTYPE, "RESUME");
                map.put(APPLICATIONTYPE, "CRM");
                map.put(OSTYPE, "ANDROID");
                map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                map.put(TOKENKEY, AccountDetails.getToken());
                map.put(ISAUFSB, String.valueOf(AccountDetails.isAufsb()));
                gotoNewEkyc(getInputBody(map));
            } else if (id == R.id.txtTelephoneNo1) {
                MSFDialog.alertDialog(getActivity(), 0,
                    getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes",
                    "No", true, new MSFDialog.DialogListener() {
                        @Override
                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                            if (action == MSFDialog.Action.OK) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + "02239982515"));
                                startActivity(callIntent);
                            }
                        }
                    });
            } else if (id == R.id.txtTelephoneNo2) {
                MSFDialog.alertDialog(getActivity(), 0,
                    getResources().getString(R.string.motilal_oswal), "Do you want to call?", "Yes",
                    "No", true, new MSFDialog.DialogListener() {
                        @Override
                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                            if (action == MSFDialog.Action.OK) {
                                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                callIntent.setData(Uri.parse("tel:" + "02239982515"));
                                startActivity(callIntent);
                            }
                        }
                    });
            } else if (id == R.id.llEmail) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, "rggupta72@gmail.com");
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");
                email.setType("message/rfc822");
                email = Intent.createChooser(email, "Choose an Email");
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(email);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadLead() {
        Bundle args = new Bundle();
        setTabActivre(btnLeads);
        ClientsLeadsFragment leadsFragment = ClientsLeadsFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, leadsFragment)
            .commit();
    }

    private void loadProductivity() {
        Bundle args = new Bundle();
        setTabActivre(btnProductivity);
        ProductivityFragment productivityFragment = ProductivityFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        productivityFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, productivityFragment)
            .commit();
    }

    private void loadBusinessSnapShotAdvisor() {
        setTabActivre(btnBusinessSnapshotAdvisor);
        Bundle args = new Bundle();
        BusSnapshotAdvFragment leadsFragment = BusSnapshotAdvFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, leadsFragment)
            .commit();
    }

    private void loadClientTraded() {
        Bundle args = new Bundle();
        setTabActivre(btnClientTraded);
        ClientsTradedYesterdayFragment clientsFragment =
            ClientsTradedYesterdayFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);

        clientsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, clientsFragment)
            .commit();
    }

    private void loadLeadBusinessSnapShot() {
        setTabActivre(btnBusinessSnapshot);
        Bundle args = new Bundle();
        BusinessSnapshotFragment leadsFragment = BusinessSnapshotFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, leadsFragment)
            .commit();
    }

    private void loadClients() {
        setTabActivre(btnClient);
        ClientsFragment clientsFragment = ClientsFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        clientsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llReplaceClientFragment, clientsFragment)
            .commit();
    }

    private void setTabActivre(Button button) {
        btnBusinessSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBusinessSnapshotAdvisor.setBackgroundDrawable(
            getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshotAdvisor.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClient.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClient.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnProductivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnProductivity.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnLeads.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClientTraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClientTraded.setTextColor(getResources().getColor(R.color.Button_text_color));

        button.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        button.setTextColor(getResources().getColor(R.color.white));
    }

    private void loadTargetAchievement() {
        Bundle args = new Bundle();
        TargetAchievementFragment leadsFragment =
            TargetAchievementFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llTargetAchievementADV, leadsFragment)
            .commit();
    }

    private void loadTeamTargetAchievement() {
        Bundle args = new Bundle();
        TargetAchievementTeamFragment leadsFragment =
            TargetAchievementTeamFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llTeamTargetAchievement, leadsFragment)
            .commit();
    }

    private void loadMonthTrends() {
        Bundle args = new Bundle();
        TrendsTabFragment leadsFragment = TrendsTabFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llMonthvsTrends, leadsFragment)
            .commit();
    }

    private void loadBusinessSnapShot() {
        Bundle args = new Bundle();
        BusinessSnapshotFragment leadsFragment = BusinessSnapshotFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.llBusinessSnapshotHead, leadsFragment)
            .commit();
    }

    private void actionableInsightDialog() {
        if (currentDashboard == null) return;

        dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_actionable_insights);
        dialog.getWindow()
            .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);

        ImageView ivClose = (ImageView) dialog.findViewById(R.id.ivClose);
        ListView lvActionableInsights = (ListView) dialog.findViewById(R.id.lvActionableInsights);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        lvActionableInsights.setAdapter(advisoryActionableInsightAdapter);

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser && dashAdvisoryView != null) {
                if (AccountDetails.getToken() == null) {
                    Intent intent = new Intent(getMainActivity(), SplashActivity.class);
                    startActivity(intent);
                } else {
                    for (FloatingActionButton sFab : dashboardBtnList) {
                        floatingActionsMenu.removeButton(sFab);
                    }
                    dashboardBtnList.clear();
                    dashboardList.clear();
                    if (isAufsb()) {
                        lldashboardAufsb.setVisibility(View.VISIBLE);
                        ivActionableInsights.setVisibility(View.GONE);
                        tvMain.setVisibility(View.GONE);
                    } else {
                        lldashboardAufsb.setVisibility(View.GONE);
                        loadDashboardMetaData();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderMetaData(UiModel<List<DashboardMetaDataItem>> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        List<DashboardMetaDataItem> dataList = uiModel.data;
        if (dataList != null) {
            try {
                for (int i = 0; i < dataList.size(); i++) {
                    DashboardMetaDataItem data = dataList.get(i);
                    fillAvailableWidgetModel(data);
                }
                if (dashboardList.size() > 0) {
                    currentDashboard = dashboardList.get(0);
                    currentDashboard.setBusinessProfileIdForDashboard(
                        businessProfileIdForDashboard);
                    currentDashboard.setProductId(productId);
                    currentDashboard.setEmployeeCode(employeeCode);
                    createFloatingButtons();
                    renderDashboard();
                }
            } catch (Exception e) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void loadDashboardMetaData() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            DashboardMetaDataRequest request = new DashboardMetaDataRequest(
                businessProfileIdForDashboard,
                productId,
                employeeCode
            );

            Disposable subscribe = saathiApi.getDashboardMetaData(request)
                .compose(applyUiModel())
                .subscribe(this::renderMetaData);
        } else {
            noInternetConnectionDialog();
        }
    }

    private Map<String, String> getAddtionalParameterMapActionable() {

        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {
            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void renderActionableCount(UiModel<ActionableCountItem> uiModel, String systemName) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            // showOnFailurePopup(error);
        }

        ActionableCountItem data = uiModel.data;
        if (data != null) {
            try {
                String type = "";
                ArrayList<TypeActionableFilterModel> typeList = new ArrayList<>();

                if (data.table != null) {
                    List<TableItem> tableList = data.table;
                    for (int i = 0; i < tableList.size(); i++) {
                        TableItem item = tableList.get(i);
                        if (item.types != null) {
                            type = stringOrEmpty(item.types);
                            TypeActionableFilterModel tmpType =
                                new TypeActionableFilterModel(type);
                            typeList.add(tmpType);
                        }
                    }
                }

                String count = "";
                if (data.table1 != null) {
                    List<Table1Item> items = data.table1;
                    Table1Item item = items.get(0);
                    count = stringOrZero(String.valueOf(item.count));
                }
                currentDashboard.setActionableCount(systemName, count,
                    typeList);

                advisoryActionableInsightAdapter.showCount(true,
                    systemName);
            } catch (Exception e) {
                currentDashboard.setActionableCount(systemName, "",
                    new ArrayList<TypeActionableFilterModel>());
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void loadActionableCount(final String systemName) {
        if (currentDashboard == null) return;
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ActionableCountRequest request = new ActionableCountRequest(
                getAddtionalParameterMapActionable(),
                systemName
            );
            Disposable subscribe = saathiApi.getActionableCount(request)
                .compose(applyUiModel())
                .subscribe(uiModel -> {
                    this.renderActionableCount(uiModel, systemName);
                });
        } else {
            noInternetConnectionDialog();
        }
    }

    private void fillAvailableWidgetModel(DashboardMetaDataItem data) throws Exception {
        if (data != null) {
            String dashboardId = String.valueOf(data.id);
            String dashboardName = data.name;
            String dashboardFor = data.dashboardFor;

            DashboardModel dashboardModel = new DashboardModel();
            dashboardList.add(dashboardModel);
            dashboardModel.setId(dashboardId);
            dashboardModel.setName(dashboardName);
            dashboardModel.setDashboardFor(dashboardFor);

            List<GroupsItem> groupsItems = data.groups;

            List<DashboardGroupModel> mainGroupList = getGroupModelFromJsonArray(groupsItems);
            dashboardModel.getGroupList().clear();
            dashboardModel.getGroupList().addAll(mainGroupList);
        }
    }

    private List<DashboardGroupModel> getGroupModelFromJsonArray(List<GroupsItem> groupsItems)
        throws Exception {
        List<DashboardGroupModel> groupList = new ArrayList();

        for (int liX = 0; liX < groupsItems.size(); liX++) {

            GroupsItem item = groupsItems.get(liX);
            if (item != null) {
                DashboardGroupModel groupModel = new DashboardGroupModel();
                groupList.add(groupModel);

                String dashBordGroupId = String.valueOf(item.dashBoardGroupId);
                String groupName = item.groupName;
                String groupSystemName = item.systemName;
                String imageUrl1 = item.imageUrl;
                String imageUrl2 = item.imageUrl2;

                groupModel.setId(dashBordGroupId);
                groupModel.setDisplayName(groupName);
                groupModel.setSystemName(groupSystemName);
                groupModel.setIconUrl1(imageUrl1);
                groupModel.setIconUrl2(imageUrl2);
                boolean selected = (liX == 0) ? true : false;
                groupModel.setSelected(selected);

                List<GroupsItem> childrenGroupItems = item.children;
                if (childrenGroupItems != null && childrenGroupItems.size() > 0) {
                    List<DashboardGroupModel> childrenGroup =
                        getGroupModelFromJsonArray(childrenGroupItems);
                    groupModel.getSubGroupList().addAll(childrenGroup);
                }

                List<AvailableWidgetsItem> availableWidgets = item.availableWidgets;
                List<WidgetModel> widgetModelList =
                    getWidgetModelFromAvailableWidgets(availableWidgets);

                groupModel.getWidgetList().addAll(widgetModelList);

                //filter list
                groupModel.setGroupFilterList(getFilterList(item.availableFilter));
            }
        }
        return groupList;
    }

    /**
     * this method will collect the filter data (dropdown)
     */
    public List<WidgetFilterModel> getFilterList(List<AvailableFilterItem> availableFilterItems) {
        //Timber.v("All " + jsonObject.toString());
        List<WidgetFilterModel> filterList = new ArrayList<>();
        try {

            if (availableFilterItems != null) {
                for (int filterCount = 0; filterCount < availableFilterItems.size();
                    filterCount++) {
                    List<FilterRowData> filterRowList = new ArrayList<>();
                    AvailableFilterItem item = availableFilterItems.get(filterCount);
                    String filterSysName = item.filterSysName;
                    String keyValue = item.keyValue;

                    FilterData filterData = item.filterData;

                    if (filterData != null) {
                        if (filterData.table != null) {
                            List<com.mosl.saathi.api.model.dashboard.dashboardmetadata.TableItem>
                                filterRowArray = filterData.table;
                            for (int filterRowCount = 0; filterRowCount < filterRowArray.size();
                                filterRowCount++) {
                                com.mosl.saathi.api.model.dashboard.dashboardmetadata.TableItem
                                    rowObj = filterRowArray.get(filterRowCount);
                                String text = rowObj.text;
                                String value = rowObj.value;
                                FilterRowData rowData =
                                    new FilterRowData(filterRowCount, text, value, keyValue);
                                filterRowList.add(rowData);
                            }
                        }
                    }

                    WidgetFilterModel filterModel =
                        new WidgetFilterModel(filterSysName, keyValue, filterRowList);
                    filterList.add(filterModel);
                }
            }
            ////
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return filterList;
    }

    /**
     * This will return list of widget model from AvailableWidgets json array
     *
     * @throws Exception
     */
    private List<WidgetModel> getWidgetModelFromAvailableWidgets(
        List<AvailableWidgetsItem> widgetsItems)
        throws Exception {
        List<WidgetModel> widgetModelList = new ArrayList<>();

        for (int ctr = 0; ctr < widgetsItems.size(); ctr++) {
            AvailableWidgetsItem item = (AvailableWidgetsItem) widgetsItems.get(ctr);

            if (item != null) {
                WidgetModel widgetModel = new WidgetModel();
                widgetModelList.add(widgetModel);
                String id = stringOrEmpty(String.valueOf(item.id));
                String displayName = stringOrEmpty(item.name);
                String systemName = stringOrEmpty(item.systemName);
                String iconUrl1 = stringOrEmpty(item.imageUrl);
                String iconUr2 = stringOrEmpty(item.imageUrl2);

                List<WidgetTypeItem> widgetTypeItems = item.widgetType;

                if (widgetTypeItems != null) {
                    List<WidgetType> childrenGroup =
                        getWidgetTypeFromAvailableWidgets(widgetTypeItems);
                    widgetModel.setWidgetTypeList(childrenGroup);
                }

                boolean selected = (ctr == 0) ? true : false;
                widgetModel.setSelected(selected);
                widgetModel.setId(id);
                widgetModel.setName(displayName);
                widgetModel.setSystemName(systemName);
                widgetModel.setIconUrl1(iconUrl1);
                widgetModel.setIconUrl2(iconUr2);
                widgetModel.setWidgetFilterList(getFilterList(item.availableFilter));
                widgetModel.setPreviousSelectedPosition(ctr);
            }
        }
        return widgetModelList;
    }

    private List<WidgetType> getWidgetTypeFromAvailableWidgets(List<WidgetTypeItem> widgetTypeItems)
        throws Exception {
        List<WidgetType> widgetModelList = new ArrayList<>();

        for (int ctr = 0; ctr < widgetTypeItems.size(); ctr++) {
            WidgetTypeItem item = (WidgetTypeItem) widgetTypeItems.get(ctr);

            if (item != null) {
                String widgetTypes = stringOrEmpty(item.widgetTypes);
                String title = stringOrEmpty(item.title);
                widgetModelList.add(new WidgetType(widgetTypes, title));
            }
        }
        return widgetModelList;
    }

    private void setClientLayoutVisibility(String systemsName) {
        txtClients.setText(currentDashboard.getGroupModel(systemsName).getDisplayName());
        switch (systemsName) {
            case MyClient_MyLead:
                btnBusinessSnapshot.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                break;
            case MY_LEAD:
                btnClient.setVisibility(View.GONE);
                btnBusinessSnapshot.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                break;
            case MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY:
                btnBusinessSnapshot.setVisibility(View.GONE);
                btnLeads.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                break;
            case SYS_LEAD_SNAPSHOT:
                btnClient.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                break;
            case TEAM_LEAD:
                btnClientTraded.setVisibility(View.GONE);
                btnBusinessSnapshot.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                btnClient.setVisibility(View.GONE);
                break;
            case SYS_MY_CLIENTS_SNAPSHOOT:
                btnBusinessSnapshot.setVisibility(View.GONE);
                btnLeads.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                break;
            case SYS_TEAM_SNAPSHOT:
                btnClient.setVisibility(View.GONE);
                btnLeads.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                break;
            case SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT:
                btnClient.setVisibility(View.GONE);
                btnClientTraded.setVisibility(View.GONE);
                btnProductivity.setVisibility(View.GONE);
                btnBusinessSnapshotAdvisor.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * This method will dynamically hide/show widget fragments
     */
    private void setGroupVisibility() {
        dashboardId = currentDashboard.getId();
        llClient.setVisibility(View.GONE);
        ivActionableInsights.setVisibility(View.GONE);
        llTarget.setVisibility(View.GONE);
        llTeamTarget.setVisibility(View.GONE);
        llPerformanceAndIncentives.setVisibility(View.GONE);
        llIncentive.setVisibility(View.GONE);
        llCurrentQuarterStatusAdviosory.setVisibility(View.GONE);
        llAdvPerformance.setVisibility(View.GONE);
        llTeamPerformance.setVisibility(View.GONE);
        llCurrentQuarterStatusOneUp.setVisibility(View.GONE);
        llMonthTrend.setVisibility(View.GONE);
        llSnapshot.setVisibility(View.GONE);
        if (currentDashboard == null) return;
        if (currentDashboard.isGroupExists(ACTIONABLE)) {
            ivActionableInsights.setVisibility(View.VISIBLE);

            List<WidgetModel> widgetModelList =
                currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
            advisoryActionableInsightAdapter =
                new AdvisoryActionableInsightAdapter(getActivity(), widgetModelList);
        }
        if (currentDashboard.isGroupExists(MyClient_MyLead)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(MyClient_MyLead);
            loadClients();
            DashboardSystemModel.setLeadSystemName(MyClient_MyLead);
            DashboardSystemModel.setClientSystemName(MyClient_MyLead);
        } else if (currentDashboard.isGroupExists(MY_LEAD)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(MY_LEAD);
            loadLead();
            DashboardSystemModel.setLeadSystemName(MY_LEAD);
        } else if (currentDashboard.isGroupExists(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY);
            loadClients();
            DashboardSystemModel.setBusSnapshotAdvisorSystemName(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY);
            DashboardSystemModel.setClientSystemName(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY);
            DashboardSystemModel.setProductivitySystemName(MYCLIENTS_MYSNAPSHOT_PRODUCTIVITY);
        }
        if (currentDashboard.isGroupExists(SYS_LEAD_SNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            setClientLayoutVisibility(SYS_LEAD_SNAPSHOT);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            loadLeadBusinessSnapShot();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_LEAD_SNAPSHOT);
            DashboardSystemModel.setLeadSystemName(SYS_LEAD_SNAPSHOT);
        }

        if (currentDashboard.isGroupExists(TEAM_LEAD)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(TEAM_LEAD);
            loadLead();
            DashboardSystemModel.setLeadSystemName(TEAM_LEAD);
        }
        if (currentDashboard.isGroupExists(SYS_MY_CLIENTS_SNAPSHOOT)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(SYS_MY_CLIENTS_SNAPSHOOT);
            loadClients();
            DashboardSystemModel.setClientSystemName(SYS_MY_CLIENTS_SNAPSHOOT);
            DashboardSystemModel.setClientTradedSystemName(SYS_MY_CLIENTS_SNAPSHOOT);
        }
        if (currentDashboard.isGroupExists(SYS_TEAM_SNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            setClientLayoutVisibility(SYS_TEAM_SNAPSHOT);
            loadLeadBusinessSnapShot();
            btnBusinessSnapshot.setText("Team Snapshot");
            //  loadLead();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_TEAM_SNAPSHOT);
        }
        if (currentDashboard.isGroupExists(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            setClientLayoutVisibility(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            loadLeadBusinessSnapShot();
            DashboardSystemModel.setBusinessSnapshotSystemName(
                SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT);
            DashboardSystemModel.setLeadSystemName(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT);
        }
        if (currentDashboard.isGroupExists(SYS_BUSINESS_SNAPSHOT)) {
            llSnapshot.setVisibility(View.VISIBLE);
            setIcon(llBusinessSnapshotHead, ivBusinessSnapshotHead);
            if (llBusinessSnapshotHead.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llBusinessSnapshotHead, ivBusinessSnapshotHead);
            }
            loadBusinessSnapShot();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_BUSINESS_SNAPSHOT);
        }
        if (currentDashboard.isGroupExists(SYS_RH_BUSINESS_SNAPSHOT)) {
            llSnapshot.setVisibility(View.VISIBLE);
            setIcon(llBusinessSnapshotHead, ivBusinessSnapshotHead);
            if (llBusinessSnapshotHead.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llBusinessSnapshotHead, ivBusinessSnapshotHead);
            }
            loadBusinessSnapShot();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_RH_BUSINESS_SNAPSHOT);
        }
        if (currentDashboard.isGroupExists(SELF_TARGET_ACHIEVEMNET)) {
            txtTarget.setText(
                currentDashboard.getGroupModel(SELF_TARGET_ACHIEVEMNET).getDisplayName());
            setIcon(llTargetAchievementADV, ivTargetAchievementADV);
            if (llTargetAchievementADV.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llTargetAchievementADV, ivTargetAchievementADV);
            }
            llTarget.setVisibility(View.VISIBLE);
            loadTargetAchievement();
            DashboardSystemModel.setTargetAchivementSystemName(SELF_TARGET_ACHIEVEMNET);
        }

        if (currentDashboard.isGroupExists(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT)) {
            txtTeamTarget.setText(
                currentDashboard.getGroupModel(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT)
                    .getDisplayName());
            setIcon(llTeamTargetAchievement, ivTeamTargetAchievement);
            if (llTeamTargetAchievement.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llTeamTargetAchievement, ivTeamTargetAchievement);
            }
            llTeamTarget.setVisibility(View.VISIBLE);
            loadTeamTargetAchievement();
            DashboardSystemModel.setTargetAchivementSystemName(
                TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT);
        }
        if (currentDashboard.isGroupExists(MONTH_ON_MONTH)) {
            llMonthTrend.setVisibility(View.VISIBLE);
            setIcon(llMonthvsTrends, ivMonthOnMonth);
            if (llMonthvsTrends.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llMonthvsTrends, ivMonthOnMonth);
            }
            loadMonthTrends();
            DashboardSystemModel.setMonthTrends(MONTH_ON_MONTH);
        } else if (currentDashboard.isGroupExists(MONTH_ON_MONTH_ACCOUNT_REVENUE)) {
            llMonthTrend.setVisibility(View.VISIBLE);
            setIcon(llMonthvsTrends, ivMonthOnMonth);
            if (llMonthvsTrends.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llMonthvsTrends, ivMonthOnMonth);
            }
            loadMonthTrends();
            DashboardSystemModel.setMonthTrends(MONTH_ON_MONTH_ACCOUNT_REVENUE);
        }

        if (currentDashboard.isGroupExists(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
            tvPerformanceAndIncentivesHeader.setText(
                currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            setIncentiveVisibility(PERFORMANCE_INCENTIVE_ACHIEVEMNET);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader,
                    ivPerformanceAndIncentivesHeader);
            }
        } else if (currentDashboard.isGroupExists(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)) {
            tvPerformanceAndIncentivesHeader.setText(
                currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            setIncentiveVisibility(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader,
                    ivPerformanceAndIncentivesHeader);
            }
        } else if (currentDashboard.isGroupExists(MY_PERFORMANCE_INCENTIVE)) {
            tvPerformanceAndIncentivesHeader.setText(
                currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            setIncentiveVisibility(MY_PERFORMANCE_INCENTIVE);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader,
                    ivPerformanceAndIncentivesHeader);
            }
        }
        //endregion

    }

    private void setIncentiveVisibility(String systemName) {
        if (currentDashboard != null) {
            DashboardGroupModel groupModel = currentDashboard.getGroupModel(systemName);

            List<WidgetModel> widgetList = groupModel.getWidgetList();
            for (int i = 0; i < widgetList.size(); i++) {
                String widgetTypes = widgetList.get(i).getWidgetTypeList().get(0).getWidgetTypes();
                if (widgetTypes.equalsIgnoreCase(PERFORMANCE_INCENTIVE)) {
                    incentiveWebServiceCall(widgetList.get(i).getSystemName());
                } else if (widgetTypes.equalsIgnoreCase(CURRENT_QUARTER_STATUS)) {
                    currentQuarterStatusWebServiceCall(widgetList.get(i).getSystemName());
                    llCurrentQuarterStatusAdviosory.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private Map<String, String> getAddtionalParameterMap() {
        Map<String, String> addtionalParameterMap = new HashMap<>();
        try {
            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    private void updateActionableCount() {
        if (currentDashboard == null) return;

        if (currentDashboard.isGroupExists(ACTIONABLE)) {
            //draw menu list
            List<WidgetModel> actionableWidgetList =
                currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
            if (actionableWidgetList != null && actionableWidgetList.size() > 0) {
                for (WidgetModel model : actionableWidgetList) {
                    loadActionableCount(model.getSystemName());
                }
            } else {
                hideProgress();
            }
        } else {
            hideProgress();
        }
    }

    private void renderIncentive(UiModel<IncentiveItem> uiModel) {
        toggleProgress(uiModel.inProgress);

        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        IncentiveItem data = uiModel.data;

        if (data != null) {
            try {
                if (data.table != null) {
                    List<com.mosl.saathi.api.model.dashboard.incentive.TableItem> tableItems =
                        data.table;
                    incentiveData(tableItems);
                }
                if (data.table1 != null) {
                    List<com.mosl.saathi.api.model.dashboard.incentive.Table1Item> table1Items =
                        data.table1;
                    advLoadChart(table1Items);
                }
            } catch (Exception e) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void incentiveWebServiceCall(final String widgetSystemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientRequest request = new ClientRequest(
                businessProfileIdForDashboard,
                getAddtionalParameterMap(),
                widgetSystemName,
                productId,
                employeeCode
            );
            Disposable subscribe = saathiApi.getIncentive(request)
                .compose(applyUiModel())
                .subscribe(this::renderIncentive);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderStatus(UiModel<JSONObject> uiModel) {
        toggleProgress(uiModel.inProgress);

        /*Throwable error = uiModel.error;
        if (error != null) {
            //showOnFailurePopup(error);
        }*/

        JSONObject data = uiModel.data;
        if (data != null) {
            try {
                renderCurrentQuarterStatus(data);
            } catch (Exception e) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    private void currentQuarterStatusWebServiceCall(final String widgetSystemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            ClientRequest request = new ClientRequest(
                businessProfileIdForDashboard,
                getAddtionalParameterMap(),
                widgetSystemName,
                productId,
                employeeCode
            );

            Disposable subscribe = saathiApi.getDashboardData(request)
                .map(responseS -> {
                    JSONObject response = new JSONObject(responseS);
                    String status = response.get(STATUS).toString();
                    if (status.equalsIgnoreCase("true")) {
                        return response.getJSONObject(RESPONSE);
                    } else {
                        String message = response.get(MESSAGE).toString();
                        throw new ServiceFailureException(message, message);
                    }
                })
                .compose(applyUiModel())
                .subscribe(this::renderStatus);
        } else {
            noInternetConnectionDialog();
        }
    }

    private void renderCurrentQuarterStatus(JSONObject responseJsonObj) throws JSONException {
        if (responseJsonObj.has("Table")) {
            JSONArray jsonArray =
                responseJsonObj.getJSONArray("Table");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String Table =
                (jsonObject.isNull("Table") || jsonObject.getString(
                    "Table").equals(""))
                    ? "" : jsonObject.getString("Table");
            if (Table.equalsIgnoreCase("3")) {
                ArrayList<String> keyList = new ArrayList<String>();
                String headingName =
                    (jsonObject.isNull("Heading")
                        || jsonObject.getString("Heading")
                        .equals("")) ? ""
                        : jsonObject.getString("Heading");
                Iterator iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String columnName = iter.next().toString();
                    keyList.add(columnName);
                }

                final ArrayList<HashMap<String, String>> yAxisList =
                    new ArrayList<HashMap<String, String>>();

                for (String keySetE : keyList) {
                    HashMap<String, String> keyValue =
                        new HashMap<>();
                    if (keySetE.toString()
                        .equalsIgnoreCase("Heading")
                        || keySetE.toString()
                        .equalsIgnoreCase("Table")) {
                    } else {
                        String columnData =
                            jsonObject.getString(keySetE)
                                .toString()
                                .equals("") ? ""
                                : jsonObject.getString(keySetE)
                                    .toString();
                        keyValue.put(keySetE.toString(),
                            columnData);
                        yAxisList.add(keyValue);
                    }
                }
                currentQuarterStatusTableThird(headingName,
                    yAxisList);
            } else if (Table.equalsIgnoreCase("1")) {
                String headingName =
                    (jsonObject.isNull("Heading")
                        || jsonObject.getString("Heading")
                        .equals("")) ? ""
                        : jsonObject.getString("Heading");
                currentQuarterStatusTableFirst(headingName,
                    jsonArray);
            }
            // incentiveData(jsonArray);
        }
        if (responseJsonObj.has("Table1")) {
            JSONArray jsonArray =
                responseJsonObj.getJSONArray("Table1");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String Table =
                (jsonObject.isNull("Table") || jsonObject.getString(
                    "Table").equals(""))
                    ? "" : jsonObject.getString("Table");
            if (Table.equalsIgnoreCase("4")) {
                ArrayList<String> keyList = new ArrayList<String>();
                String headingName =
                    (jsonObject.isNull("Heading")
                        || jsonObject.getString("Heading")
                        .equals("")) ? ""
                        : jsonObject.getString("Heading");
                Iterator iter = jsonObject.keys();
                while (iter.hasNext()) {
                    String columnName = iter.next().toString();
                    keyList.add(columnName);
                }

                final ArrayList<HashMap<String, String>> yAxisList =
                    new ArrayList<HashMap<String, String>>();

                for (String keySetE : keyList) {
                    HashMap<String, String> keyValue =
                        new HashMap<>();
                    if (keySetE.toString()
                        .equalsIgnoreCase("Heading")
                        || keySetE.toString()
                        .equalsIgnoreCase("Table")) {
                    } else {
                        String columnData =
                            jsonObject.getString(keySetE)
                                .toString()
                                .equals("") ? ""
                                : jsonObject.getString(keySetE)
                                    .toString();
                        keyValue.put(keySetE.toString(),
                            columnData);
                        yAxisList.add(keyValue);
                    }
                }
                currentQuarterStatusTableFourth(headingName,
                    yAxisList);
            } else if (Table.equalsIgnoreCase("2")) {
                String headingName =
                    (jsonObject.isNull("Heading")
                        || jsonObject.getString("Heading")
                        .equals("")) ? ""
                        : jsonObject.getString("Heading");
                currentQuarterStatusTableSecond(headingName,
                    jsonArray);
            }
            // incentiveData(jsonArray);
        }
    }

    private String keyName(Set<String> keySet) {
        String key = "";
        if (keySet != null) {
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                key = iterator.next();
                break;
            }
        }
        return key;
    }

    private void currentQuarterStatusTableFirst(String headingName, JSONArray jsonArray) {
        tableCurrentQuaterStatusFirst.removeAllViews();
        try {
            if (jsonArray == null) return;

            LinkedHashMap<String, List<Map>> tableMap = new LinkedHashMap<>();

            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jsonRowObject = jsonArray.getJSONObject(x);
                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String businessChannel = new String(array, Charset.forName("UTF-8"));
                //  String businessChannel = jsonRowObject.getString("Heading");
                JSONArray paramNameJsonArray = jsonRowObject.names();

                List<Map> list = new ArrayList<>();

                for (int y = 0; y < paramNameJsonArray.length(); y++) {
                    String paramName = paramNameJsonArray.getString(y);
                    String paramValue =
                        jsonRowObject.getString(paramName).equalsIgnoreCase("null") ? ""
                            : jsonRowObject.getString(paramName);
                    if (!paramName.equalsIgnoreCase("Heading") && !paramName.equalsIgnoreCase(
                        "Table")) {
                        Map map = new HashMap();
                        map.put(paramName, paramValue);
                        list.add(map);
                    }
                }

                tableMap.put(businessChannel, list);
            }

            //draw table

            Set<String> rowHeadingSet = tableMap.keySet();

            //header
            TableRow headingRow = new TableRow(getActivity());
            tableCurrentQuaterStatusFirst.addView(headingRow);
            headingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
            headingRow.setBackgroundColor(getResources().getColor(R.color.mosl_web_color));

            Iterator headingIterator = tableMap.keySet().iterator();

            if (headingIterator.hasNext()) {
                String rowHead = headingIterator.next().toString();
                List<Map> mm = tableMap.get(rowHead);

                for (Map cellmap : mm) {
                    String headingText = cellmap.keySet().iterator().next().toString();
                    headingText = headingText.replaceAll("_", " ");

                    TextView headingCell = new TextView(getActivity());
                    headingRow.addView(headingCell);
                    headingCell.setLayoutParams(
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    headingCell.setBackgroundResource(R.drawable.cell_shape);
                    final int padding = (int) getResources().getDimension(R.dimen._5sdp);
                    headingCell.setPadding(padding, padding, padding, padding);
                    //column.setTextAppearance(context, android.R.attr.textAppearanceMedium);
                    headingCell.setText(headingText);
                }
            }

            //row data
            for (String column1Heading : rowHeadingSet) {
                TableRow tableRow = new TableRow(getActivity());
                tableCurrentQuaterStatusFirst.addView(tableRow);
                tableRow.setLayoutParams(
                    new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                List<Map> rowList = tableMap.get(column1Heading);
                for (Map map : rowList) {
                    String header = map.keySet().iterator().next().toString();
                    String value = map.values().iterator().next().toString();

                    TextView textView = new TextView(getActivity());
                    tableRow.addView(textView);
                    textView.setLayoutParams(
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    textView.setBackgroundResource(R.drawable.cell_shape);
                    final int padding = (int) getResources().getDimension(R.dimen._5sdp);
                    textView.setPadding(padding, padding, padding, padding);
                    //column.setTextAppearance(context, android.R.attr.textAppearanceMedium);
                    textView.setText(value);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tvCurrentQuaterStatusFirstHeading.setText(headingName);
        llCurrentQuaterStatusFirst.setVisibility(View.VISIBLE);
    }

    private void currentQuarterStatusTableSecond(String headingName, JSONArray jsonArray) {
        tableCurrentQuaterStatusSecond.removeAllViews();
        try {
            if (jsonArray == null) return;

            LinkedHashMap<String, List<Map>> tableMap = new LinkedHashMap<>();

            for (int x = 0; x < jsonArray.length(); x++) {
                JSONObject jsonRowObject = jsonArray.getJSONObject(x);
                byte[] array = new byte[7]; // length is bounded by 7
                new Random().nextBytes(array);
                String businessChannel = new String(array, Charset.forName("UTF-8"));
                //  String businessChannel = jsonRowObject.getString("Heading");
                JSONArray paramNameJsonArray = jsonRowObject.names();

                List<Map> list = new ArrayList<>();

                for (int y = 0; y < paramNameJsonArray.length(); y++) {
                    String paramName = paramNameJsonArray.getString(y);
                    String paramValue =
                        jsonRowObject.getString(paramName).equalsIgnoreCase("null") ? ""
                            : jsonRowObject.getString(paramName);
                    if (!paramName.equalsIgnoreCase("Heading") && !paramName.equalsIgnoreCase(
                        "Table")) {
                        Map map = new HashMap();
                        map.put(paramName, paramValue);
                        list.add(map);
                    }
                }

                tableMap.put(businessChannel, list);
            }

            //draw table

            Set<String> rowHeadingSet = tableMap.keySet();

            //header
            TableRow headingRow = new TableRow(getActivity());
            tableCurrentQuaterStatusSecond.addView(headingRow);
            headingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
            headingRow.setBackgroundColor(getResources().getColor(R.color.mosl_web_color));

            Iterator headingIterator = tableMap.keySet().iterator();

            if (headingIterator.hasNext()) {
                String rowHead = headingIterator.next().toString();
                List<Map> mm = tableMap.get(rowHead);

                for (Map cellmap : mm) {
                    String headingText = cellmap.keySet().iterator().next().toString();
                    headingText = headingText.replaceAll("_", " ");

                    TextView headingCell = new TextView(getActivity());
                    headingRow.addView(headingCell);
                    headingCell.setLayoutParams(
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    headingCell.setBackgroundResource(R.drawable.cell_shape);
                    final int padding = (int) getResources().getDimension(R.dimen._5sdp);
                    headingCell.setPadding(padding, padding, padding, padding);
                    //column.setTextAppearance(context, android.R.attr.textAppearanceMedium);
                    headingCell.setText(headingText);
                }
            }

            //row data
            for (String column1Heading : rowHeadingSet) {
                TableRow tableRow = new TableRow(getActivity());
                tableCurrentQuaterStatusSecond.addView(tableRow);
                tableRow.setLayoutParams(
                    new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                List<Map> rowList = tableMap.get(column1Heading);
                for (Map map : rowList) {
                    String header = map.keySet().iterator().next().toString();
                    String value = map.values().iterator().next().toString();

                    TextView textView = new TextView(getActivity());
                    tableRow.addView(textView);
                    textView.setLayoutParams(
                        new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    textView.setBackgroundResource(R.drawable.cell_shape);
                    final int padding = (int) getResources().getDimension(R.dimen._5sdp);
                    textView.setPadding(padding, padding, padding, padding);
                    //column.setTextAppearance(context, android.R.attr.textAppearanceMedium);
                    textView.setText(value);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tvCurrentQuaterStatusSecondHeading.setText(headingName);
        llCurrentQuaterStatusSecond.setVisibility(View.VISIBLE);
    }

    private void currentQuarterStatusTableThird(String headingName,
        ArrayList<HashMap<String, String>> arrayList) {
        tableCurrentQuaterStatusThree.removeAllViews();
        LayoutInflater inFla = getActivity().getLayoutInflater();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {

                Map<String, String> filterData = arrayList.get(i);
                Set<String> keySet = filterData.keySet();

                String value = arrayList.get(i).get(keyName(keySet));

                View tableRow = inFla.inflate(R.layout.table_current_quarter_status_three, null);

                TextView tvKey = (TextView) tableRow.findViewById(R.id.tvKey);
                TextView tvValue = (TextView) tableRow.findViewById(R.id.tvValue);

                tvKey.setText(keyName(keySet).replace("_", " "));
                tvValue.setText(value);

                tableCurrentQuaterStatusThree.addView(tableRow);
            }
        }
        tvCurrentQuaterStatusThreeHeading.setText(headingName);
        llCurrentQuaterStatusThree.setVisibility(View.VISIBLE);
    }

    private void currentQuarterStatusTableFourth(String headingName,
        ArrayList<HashMap<String, String>> arrayList) {
        tableCurrentQuaterStatusFour.removeAllViews();
        LayoutInflater inFla = getActivity().getLayoutInflater();
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {

                Map<String, String> filterData = arrayList.get(i);
                Set<String> keySet = filterData.keySet();

                String value = arrayList.get(i).get(keyName(keySet));

                View tableRow = inFla.inflate(R.layout.table_current_quarter_status_three, null);

                TextView tvKey = (TextView) tableRow.findViewById(R.id.tvKey);
                TextView tvValue = (TextView) tableRow.findViewById(R.id.tvValue);

                tvKey.setText(keyName(keySet).replace("_", " "));
                tvValue.setText(value);

                tableCurrentQuaterStatusFour.addView(tableRow);
            }
        }
        tvCurrentQuaterStatusFourHeading.setText(headingName);
        llCurrentQuaterStatusFour.setVisibility(View.VISIBLE);
    }

    private void incentiveData(
        List<com.mosl.saathi.api.model.dashboard.incentive.TableItem> tableItems) {
        arrayListIncentive.clear();
        for (int i = 0; i < tableItems.size(); i++) {
            HashMap<String, String> fieldData = new HashMap<String, String>();
            com.mosl.saathi.api.model.dashboard.incentive.TableItem item = tableItems.get(i);
            String Parameters = stringOrEmpty(item.parameters);
            String q1 = stringOrZero(String.valueOf(item.q1));
            String q2 = stringOrZero(String.valueOf(item.q2));
            String q3 = stringOrZero(String.valueOf(item.q3));
            String q4 = stringOrZero(String.valueOf(item.q4));
            String yTD = stringOrZero(String.valueOf(item.yTD));
            fieldData.put(PARAMETERS, Parameters);
            fieldData.put(Q1, q1.equalsIgnoreCase("0.0") ? "0" : q1);
            fieldData.put(Q2, q2.equalsIgnoreCase("0.0") ? "0" : q2);
            fieldData.put(Q3, q3.equalsIgnoreCase("0.0") ? "0" : q3);
            fieldData.put(Q4, q4.equalsIgnoreCase("0.0") ? "0" : q4);
            fieldData.put(YTD, yTD.equalsIgnoreCase("0.0") ? "0" : yTD);
            arrayListIncentive.add(fieldData);
        }
        incentiveView("Q1");
    }

    private void incentiveView(String value) {
        //clear data from table
        tableMData.removeAllViews();
        //Adding head
        LayoutInflater inFla = getActivity().getLayoutInflater();
        View tableRowH = inFla.inflate(R.layout.item_table_row, null);
        TextView tvMTDCOUNT = (TextView) tableRowH.findViewById(R.id.tvMTDCOUNT);
        TextView tvYTDCOUNT = (TextView) tableRowH.findViewById(R.id.tvYTDCOUNT);
        TextView tvParameters = (TextView) tableRowH.findViewById(R.id.tvParameters);
        // LinearLayout llFamiMappedCount = (LinearLayout) tableRowH.findViewById(R.id.llFamiMappedCount);
        tvParameters.setText("Parameters");
        tvMTDCOUNT.setText(value);
        tvYTDCOUNT.setText(YTD);
        tableMData.addView(tableRowH);
        //adding head end

        if (arrayListIncentive.size() > 0) {
            for (int i = 0; i < arrayListIncentive.size(); i++) {

                String filterData = arrayListIncentive.get(i).get(value);
                String parameter = arrayListIncentive.get(i).get(PARAMETERS);
                String ytd = arrayListIncentive.get(i).get(YTD);
                View tableRow = inFla.inflate(R.layout.item_table_row, null);
                LinearLayout linearLayout = (LinearLayout) tableRow.findViewById(R.id.linearLayout);
                TextView tvMTDCOUNTData = (TextView) tableRow.findViewById(R.id.tvMTDCOUNT);
                TextView tvYTDCOUNTData = (TextView) tableRow.findViewById(R.id.tvYTDCOUNT);
                TextView tvParametersData = (TextView) tableRow.findViewById(R.id.tvParameters);

                tvParametersData.setText(parameter);
                tvMTDCOUNTData.setText(filterData);
                tvYTDCOUNTData.setText(ytd);
                linearLayout.setBackgroundResource(i % 2 == 0 ? R.color.white : R.color.white);
                tableMData.addView(tableRow);
            }
        }
        llIncentive.setVisibility(View.VISIBLE);
        tvIncentives.setText("Incentive");
    }

    private void createFloatingButtons() {

        if (dashboardList != null && dashboardList.size() > 1) {
            for (final DashboardModel dashboardModel : dashboardList) {
                FloatingActionButton button = new FloatingActionButton(getContext());
                button.setTitle(dashboardModel.getName());
                button.setSize(FloatingActionButton.SIZE_MINI);
                button.setColorNormal(getResources().getColor(R.color.white));
                button.setIconDrawable(getResources().getDrawable(R.drawable.ic_create_lead));
                floatingActionsMenu.addButton(button);
                dashboardBtnList.add(button);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(v);
                        tvMain.requestFocus();
                        floatingActionsMenu.collapse();
                        currentDashboard = dashboardModel;

                        List<WidgetModel> widgetModelList =
                            currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
                        if (advisoryActionableInsightAdapter == null) {
                            advisoryActionableInsightAdapter =
                                new AdvisoryActionableInsightAdapter(getActivity(),
                                    widgetModelList);
                        } else {
                            advisoryActionableInsightAdapter.addItems(widgetModelList);
                        }
                        advisoryActionableInsightAdapter.notifyDataSetChanged();

                        renderDashboard();
                    }
                });
            }
        }
    }

    private void renderDashboard() {
        setGroupVisibility();
        updateActionableCount();
    }

    public void toggleSectionExpansion(View parent, ImageView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            parent.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.plus);
        } else {
            parent.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.minus);
        }
    }

    public void setIcon(View parent, ImageView icon) {
        if (parent.getVisibility() == View.VISIBLE) {
            icon.setImageResource(R.drawable.minus);
        } else {
            icon.setImageResource(R.drawable.plus);
        }
    }

    private void renderIncentivePolicy(UiModel<IncentivePolicyItem> uiModel) {
        toggleProgress(uiModel.inProgress);
        Throwable error = uiModel.error;
        if (error != null) {
            showOnFailurePopup(error);
        }

        IncentivePolicyItem data = uiModel.data;
        if (data != null) {
            try {
                String filePath = stringOrEmpty(data.filePath);
                if (filePath.isEmpty()) {
                    alertDialog(getMainActivity(), 0,
                        getResources().getString(
                            R.string.motilal_oswal), MISSING_FILE, OK,
                        true, null);
                } else {
                    new DownloadFileFromURL().execute(
                        filePath.replaceAll(" ", "%20"));
                }
            } catch (Exception e) {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        }
    }

    public void webServiceInceniveSlab(String dashboardId) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            IncentivePolicyRequest request = new IncentivePolicyRequest(
                businessProfileIdForDashboard,
                AccountDetails.getEmployeeCode(),
                dashboardId
            );
            Disposable subscribe = saathiApi.getIncentivePolicy(request)
                .compose(applyUiModel())
                .subscribe(this::renderIncentivePolicy);
        } else {
            noInternetConnectionDialog();
        }
    }

    public void showDownloadFile(File localFilePath) {
        try {
            Uri uri = FileProvider.getUriForFile(
                getActivity(),
                "com.mosl.tabapp.provider",
                localFilePath);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION);

            intent.setDataAndType(
                uri,
                getActivity().getContentResolver().getType(uri));

            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                MISSING_FILE_VIEWER + localFilePath.getAbsolutePath(), OK, true, null);
        } catch (Exception ex) {
            MSFDialog.alertDialog(getMainActivity(), 0,
                getResources().getString(R.string.motilal_oswal),
                SOME_ERROR_OCCURED_PLEASE_RETRY, OK, true, null);
        }
    }

    /**
     * Showing Dialog
     */
    public void showDialog() {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private class ChartDataModel {

        private LinkedHashMap<String, Float> monthData = new LinkedHashMap<>();

        public LinkedHashMap<String, Float> getMonthData() {
            return monthData;
        }

        public void setMonthData(LinkedHashMap<String, Float> monthData) {
            this.monthData = monthData;
        }
    }

    public class AdvisoryActionableInsightAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        Context mContext;
        List<WidgetModel> advisoryActionableInsightModelList;
        //private ArrayList<AdvisoryActionableInsightModel> arraylist;

        public AdvisoryActionableInsightAdapter(Context c,
            List<WidgetModel> advisoryActionableInsightModelList) {
            inflater = LayoutInflater.from(c);
            this.mContext = c;
            this.advisoryActionableInsightModelList = advisoryActionableInsightModelList;
        }

        public void showCount(boolean show, String systemName) {
            for (WidgetModel model : advisoryActionableInsightModelList) {
                if (model.getSystemName().equals(systemName)) {
                    if (show) {
                        model.setShowActionableCount(show);
                        break;
                    }
                }
            }
            notifyDataSetChanged();
        }

        public void clear() {
            advisoryActionableInsightModelList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return advisoryActionableInsightModelList.size();
        }

        @Override
        public Object getItem(int i) {
            return advisoryActionableInsightModelList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final Holder holder = new Holder();
            view = inflater.inflate(R.layout.item_list_actionable_insights, viewGroup, false);

            final WidgetModel advisoryActionableInsightModel =
                advisoryActionableInsightModelList.get(i);
            holder.ivActionableInsights = (ImageView) view.findViewById(R.id.ivActionableInsights);
            holder.pbLoadingIndicator = (ProgressBar) view.findViewById(R.id.pbLoadingIndicator);
            holder.tvActionableInsights = (TextView) view.findViewById(R.id.tvActionableInsights);
            holder.tvActionableCount = (TextView) view.findViewById(R.id.tvActionableCount);
            holder.rlActionable = (RelativeLayout) view.findViewById(R.id.rlActionable);

            final Map<String, String> addtionalParameterMap = new HashMap<>();
            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
            Glide.with(getContext())
                .load(advisoryActionableInsightModel.getIconUrl2().replaceAll(" ", "%20"))
                //remove Chache
                .asBitmap()
                .placeholder(R.drawable.ic_create_meeting)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(holder.ivActionableInsights) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        holder.ivActionableInsights.setImageDrawable(circularBitmapDrawable);
                    }
                });
            holder.tvActionableInsights.setText(advisoryActionableInsightModel.getName());
            holder.tvActionableCount.setText(advisoryActionableInsightModel.getActionableCount());
            final DashboardGroupModel groupModel = currentDashboard.getGroupModel(ACTIONABLE);

            holder.rlActionable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(SYSTEM_NAME, advisoryActionableInsightModel.getSystemName());
                    args.putString(LABEL, ACTIONABLE);
                    args.putString(WIDGETNAME, advisoryActionableInsightModel.getName());
                    args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                    List<WidgetModel> widgetList = groupModel.getWidgetList();
                    if (widgetList != null && widgetList.size() > 0) {
                        for (WidgetModel model1 : widgetList) {
                            if (advisoryActionableInsightModel.getSystemName()
                                .equalsIgnoreCase(model1.getSystemName())) {
                                ArrayList<WidgetFilterModel> filterList =
                                    (ArrayList<WidgetFilterModel>) model1.getWidgetFilterList();
                                ArrayList<TypeActionableFilterModel> spinnerFilter =
                                    (ArrayList<TypeActionableFilterModel>) model1.getSpinnerFilterList();
                                if (!filterList.isEmpty()) {
                                    args.putParcelableArrayList(FILTER_LIST, filterList);
                                }
                                if (!spinnerFilter.isEmpty()) {
                                    args.putParcelableArrayList(SPINNER_LIST, spinnerFilter);
                                }
                            }
                        }
                    }
                    if (!advisoryActionableInsightModel.getActionableCount()
                        .equalsIgnoreCase("0")) {
                        navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
                        dialog.dismiss();
                    }
                }
            });

            if (advisoryActionableInsightModel.isShowActionableCount()) {
                holder.tvActionableCount.setVisibility(View.VISIBLE);
                holder.pbLoadingIndicator.setVisibility(View.GONE);
            } else {
                holder.rlActionable.setOnClickListener(null);
                holder.tvActionableCount.setVisibility(View.GONE);
                holder.pbLoadingIndicator.setVisibility(View.VISIBLE);
            }

            return view;
        }

        public void addItems(List<WidgetModel> widgetModelList) {
            advisoryActionableInsightModelList = widgetModelList;
            notifyDataSetChanged();
        }

        class Holder {
            ImageView ivActionableInsights;
            TextView tvActionableInsights, tvActionableCount;
            RelativeLayout rlActionable;
            ProgressBar pbLoadingIndicator;
        }
    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, Boolean> {

        String fileName, remoteFilePath;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected Boolean doInBackground(String... params) {
            int count;
            try {
                remoteFilePath = params[0];
                fileName = CRMAppUtil.getFileNameFromPath(remoteFilePath);

                java.net.URL url = new URL(remoteFilePath);

                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                File baseFolder = new File(context.getFilesDir(), "crmdocs");

                if (!baseFolder.exists()) {
                    baseFolder.mkdir();
                }

                File localFile = new File(baseFolder, fileName);

                OutputStream output = new FileOutputStream(
                    localFile.getAbsolutePath()); //Environment.getExternalStorageDirectory().toString()+"/"+fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Boolean response) {
            pDialog.dismiss();
            if (response) {
                File baseFolder = new File(context.getFilesDir(), "crmdocs");
                final File localFile = new File(baseFolder, fileName);
                showDownloadFile(localFile);
            } else {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal),
                    SOME_SERVER_ERROR_OCCURRED, OK, true, null);
            }
        }
    }
}
