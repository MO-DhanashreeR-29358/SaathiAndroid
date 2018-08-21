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
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.mikephil.charting.charts.BarChart;
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
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMDecimalValueFormater;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMValueFormatter;
import com.mosl.tabapp.common.FilterRowData;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.AdvisoryActionableInsightModel;
import com.mosl.tabapp.model.TypeActionableFilterModel;
import com.mosl.tabapp.model.dashboard.AvailableWidgetModel;
import com.mosl.tabapp.model.dashboard.DashboardGroupModel;
import com.mosl.tabapp.model.dashboard.DashboardModel;
import com.mosl.tabapp.model.dashboard.DashboardSystemModel;
import com.mosl.tabapp.model.dashboard.TeamQuarterStatusModel;
import com.mosl.tabapp.model.dashboard.WidgetFilterModel;
import com.mosl.tabapp.model.dashboard.WidgetModel;
import com.mosl.tabapp.model.dashboard.WidgetType;
import com.mosl.tabapp.ui.LoginActivity;
import com.mosl.tabapp.ui.MOSLCommonFragment;
import com.mosl.tabapp.ui.MSFDialog;
import com.mosl.tabapp.ui.SplashActivity;
import com.mosl.tabapp.ws.CRMWebServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.mosl.tabapp.common.CRMAppUtil.isBlank;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;


/**
 * Created by raju on 2/7/2017.
 */

public class DashboardFragment extends MOSLCommonFragment implements View.OnClickListener, MOSLConstants {
    private String TAG = DashboardFragment.class.getCanonicalName();

    private String businessProfileIdForDashboard, productId, employeeCode, dashboardId = ""; //will be used to get dashboard

    private View dashAdvisoryView;
    private TextView txtClients, txtTarget, tvtomove, tvcurrently, tvaccAcq, tvacqValue, tvrevGen, tvrevVal,
            tvsipAcq, tvsipVal, tvpmsAcq, tvpmsVal, tvAccReqVal, tvReqVal, tvaccReq, tvpmsReq, tvPmsReqVal, tvsipAcq1, tvsipReqVal, tvrevReq, tvRevReqVal, tvMain;
    private LinearLayout llClient, llTarget, llPerformanceAndIncentives, llBusinessSnapshotHead, llClientsLeads, llTargetAchievementADV, llTeamTargetAchievement, llPerformanceAndIncentivesHeader;
    private ImageView ivActionableInsights, ivClientLeads, ivTargetAchievementADV, ivTeamTargetAchievement, ivMonthOnMonth, ivBusinessSnapshotHead, ivPerformanceAndIncentivesHeader;
    static Context mContext;
    //
    //Subb button
    private Button btnClient, btnLeads, btnBusinessSnapshot, btnClientTraded;

    private AdvisoryActionableInsightAdapter advisoryActionableInsightAdapter;
    private ArrayList<AdvisoryActionableInsightModel> advisoryActionableInsightModelArrayList;
    Bundle args = new Bundle();
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton leadcreation;
    private RelativeLayout llMain, rlClientLeads, rlTargetAchievementADV, rlTeamTargetAchievement, rlMonthOnMonth, rlBusinessSnapshotHead, rlPerformanceAndIncentivesHeader;

    private Dialog dialog;
    //Team
    private LinearLayout llTeamTarget;
    private TextView txtTeamTarget;
    private TableLayout tableMData, tableCurrentQuaterStatusThree, tableCurrentQuaterStatusFour,
            tableTeamPerformance, tableAdvPerformance, tableCurrentQuaterStatusFirst, tableCurrentQuaterStatusSecond;

    //Hierarchy

    private LinearLayout llSnapshot, mTabsLinearLayoutSnapshot;
    private TextView tvSnapshot, txtMonthTrends;
    private ArrayList<Fragment> pageListSnapshot = new ArrayList<>();
    private TextView tvNameFirst, tvBranchNameFirst, tvTopRevenueFirst, tvNameSecond, tvBranchNameSecond, tvTopRevenueSecond, tvNameThird, tvBranchNameThird, tvTopRevenueThird, tvBottomNameFirst, tvBottomBranchNameFirst, tvBottomRevenueFirst, tvBottomNameSecond,
            tvBottomBranchNameSecond, tvBottomRevenueSecond, tvBottomNameThird, tvBottomBranchNameThird, tvBottomRevenueThird;

    private LinearLayout llMonthTrend, llMonthvsTrends;
    private List<AvailableWidgetModel> widgetList;
    private List<DashboardModel> dashboardList = new ArrayList<>();
    private List<FloatingActionButton> dashboardBtnList = new ArrayList<>();
    private DashboardModel currentDashboard;
    //incentive

    private TextView tvIncentives, tvPerformance, tvPerformanceAndIncentivesHeader, tvCurrentQuaterStatusThreeHeading, tvCurrentQuaterStatusFourHeading,
            tvCurrentQuaterStatusFirstHeading, tvCurrentQuaterStatusSecondHeading;
    private Spinner spQuarter;


    //Adviosory Performance
    private BarChart mChartAdv;
    private ArrayList<String> yDataGraphArrAdv = new ArrayList<>();
    private ArrayList<String> xdataYearArrAdv = new ArrayList<>();

    //Team Performance
    private BarChart mChartTeam;
    private ArrayList<String> yDataGraphArrTeam = new ArrayList<>();
    private ArrayList<String> yDataGraphBlankArrTeam = new ArrayList<>();
    private ArrayList<String> xdataYearArrTeam = new ArrayList<>();

    //Hierarchy Performance
    protected HorizontalBarChart mChart;
    private final int itemcount = 4;

    //Mapped BA Snapshot

    private Button btnBASnapshot, btnBACategory, btnBusinessSnapshotOneUp, btnBusinessSnapshotAdv, btnBusinessLead;
    //  private CustomTabsClient mClient;
    private TextView tvDetailIncentive, tvQuarterStatus;
    private String widgetSystemName;
    private ArrayList<HashMap<String, String>> arrayListIncentive = new ArrayList<HashMap<String, String>>();
    private ArrayList<TeamQuarterStatusModel> arrayListCurrentStatus;
    private LinearLayout llIncentive, llCurrentQuaterStatusThree, llCurrentQuaterStatusFour, llCurrentQuaterStatusFirst, llCurrentQuaterStatusSecond, llCurrentQuarterStatusAdviosory, llAdvPerformance, llTeamPerformance, llCurrentQuarterStatusOneUp;
    private ScrollView scrollView;
    private List<WidgetModel> widgetModelList;
    private LinearLayout llReplaceClientFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private XAxis xAxis;

    private void setUpView(View dashAdvisoryView) {
        scrollView = (ScrollView) dashAdvisoryView.findViewById(R.id.scrollView);
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
       /* try {
            CustomTabsServiceConnection mConnection = new CustomTabsServiceConnection() {
                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    mClient = customTabsClient;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mClient = null;
                }
            };

            String packag = CustomTabsHelper.getPackageNameToUse(getContext());

            CustomTabsClient.bindCustomTabsService(getContext(), packag, mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        tvDetailIncentive = (TextView) dashAdvisoryView.findViewById(R.id.tvDetailIncentive);
        tvQuarterStatus = (TextView) dashAdvisoryView.findViewById(R.id.tvQuarterStatus);
        tvDetailIncentive.setOnClickListener(this);
        llMain = (RelativeLayout) dashAdvisoryView.findViewById(R.id.llMain);
        floatingActionsMenu = (FloatingActionsMenu) dashAdvisoryView.findViewById(R.id.floatingActionsMenu);
        leadcreation = (FloatingActionButton) dashAdvisoryView.findViewById(R.id.leadcreation);


        ivActionableInsights = (ImageView) dashAdvisoryView.findViewById(R.id.ivActionableInsights);
        ivClientLeads = (ImageView) dashAdvisoryView.findViewById(R.id.ivClientLeads);
        ivTargetAchievementADV = (ImageView) dashAdvisoryView.findViewById(R.id.ivTargetAchievementADV);
        ivTeamTargetAchievement = (ImageView) dashAdvisoryView.findViewById(R.id.ivTeamTargetAchievement);
        ivMonthOnMonth = (ImageView) dashAdvisoryView.findViewById(R.id.ivMonthOnMonth);
        ivBusinessSnapshotHead = (ImageView) dashAdvisoryView.findViewById(R.id.ivBusinessSnapshotHead);
        ivPerformanceAndIncentivesHeader = (ImageView) dashAdvisoryView.findViewById(R.id.ivPerformanceAndIncentivesHeader);
        txtClients = (TextView) dashAdvisoryView.findViewById(R.id.txtClients);
        txtTarget = (TextView) dashAdvisoryView.findViewById(R.id.txtTarget);
        txtTeamTarget = (TextView) dashAdvisoryView.findViewById(R.id.txtTeamTarget);
        tvPerformanceAndIncentivesHeader = (TextView) dashAdvisoryView.findViewById(R.id.tvPerformanceAndIncentivesHeader);
        tvMain = (TextView) dashAdvisoryView.findViewById(R.id.tvMain);
        txtTeamTarget.setText(ADVISORY_TARGET_TITLE);
        // tvTeamPerformance.setText(ADVISORY_TEAM_PERFORMANCE_TITLE);
        btnLeads = (Button) dashAdvisoryView.findViewById(R.id.btnLeads);
        btnClient = (Button) dashAdvisoryView.findViewById(R.id.btnClient);


        llClient = (LinearLayout) dashAdvisoryView.findViewById(R.id.llClient);
        llClientsLeads = (LinearLayout) dashAdvisoryView.findViewById(R.id.llClientsLeads);
        llTargetAchievementADV = (LinearLayout) dashAdvisoryView.findViewById(R.id.llTargetAchievementADV);
        llTeamTargetAchievement = (LinearLayout) dashAdvisoryView.findViewById(R.id.llTeamTargetAchievement);
        llPerformanceAndIncentivesHeader = (LinearLayout) dashAdvisoryView.findViewById(R.id.llPerformanceAndIncentivesHeader);
        rlClientLeads = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlClientLeads);
        rlTargetAchievementADV = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlTargetAchievementADV);
        rlTeamTargetAchievement = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlTeamTargetAchievement);
        rlMonthOnMonth = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlMonthOnMonth);
        rlBusinessSnapshotHead = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlBusinessSnapshotHead);
        rlPerformanceAndIncentivesHeader = (RelativeLayout) dashAdvisoryView.findViewById(R.id.rlPerformanceAndIncentivesHeader);

        btnClientTraded = (Button) dashAdvisoryView.findViewById(R.id.btnClientTraded);
        btnBusinessSnapshot = (Button) dashAdvisoryView.findViewById(R.id.btnBusinessSnapshot);


        llTarget = (LinearLayout) dashAdvisoryView.findViewById(R.id.llTarget);
        llTeamTarget = (LinearLayout) dashAdvisoryView.findViewById(R.id.llTeamTarget);
        llPerformanceAndIncentives = (LinearLayout) dashAdvisoryView.findViewById(R.id.llPerformanceAndIncentives);
        llMonthTrend = (LinearLayout) dashAdvisoryView.findViewById(R.id.llMonthTrend);
        llMonthvsTrends = (LinearLayout) dashAdvisoryView.findViewById(R.id.llMonthvsTrends);
        llBusinessSnapshotHead = (LinearLayout) dashAdvisoryView.findViewById(R.id.llBusinessSnapshotHead);


        llIncentive = (LinearLayout) dashAdvisoryView.findViewById(R.id.llIncentive);
        llCurrentQuaterStatusThree = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusThree);
        llCurrentQuaterStatusFour = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusFour);
        llCurrentQuaterStatusFirst = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusFirst);
        llCurrentQuaterStatusSecond = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuaterStatusSecond);
        llCurrentQuarterStatusAdviosory = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuarterStatusAdviosory);
        llCurrentQuarterStatusOneUp = (LinearLayout) dashAdvisoryView.findViewById(R.id.llCurrentQuarterStatusOneUp);
        llAdvPerformance = (LinearLayout) dashAdvisoryView.findViewById(R.id.llAdvPerformance);
        llTeamPerformance = (LinearLayout) dashAdvisoryView.findViewById(R.id.llTeamPerformance);


        btnBASnapshot = (Button) dashAdvisoryView.findViewById(R.id.btnBASnapshot);
        btnBACategory = (Button) dashAdvisoryView.findViewById(R.id.btnBACategory);
        btnBusinessSnapshotOneUp = (Button) dashAdvisoryView.findViewById(R.id.btnBusinessSnapshotOneUp);
        btnBusinessSnapshotAdv = (Button) dashAdvisoryView.findViewById(R.id.btnBusinessSnapshotAdv);
        btnBusinessLead = (Button) dashAdvisoryView.findViewById(R.id.btnBusinessLead);
        btnBASnapshot.setOnClickListener(this);
        btnBACategory.setOnClickListener(this);
        btnBusinessSnapshotOneUp.setOnClickListener(this);
        btnBusinessSnapshotAdv.setOnClickListener(this);
        btnBusinessLead.setOnClickListener(this);
        rlClientLeads.setOnClickListener(this);
        rlTargetAchievementADV.setOnClickListener(this);
        rlTeamTargetAchievement.setOnClickListener(this);
        rlMonthOnMonth.setOnClickListener(this);
        rlBusinessSnapshotHead.setOnClickListener(this);
        rlPerformanceAndIncentivesHeader.setOnClickListener(this);
        //Hierarchy


        llSnapshot = (LinearLayout) dashAdvisoryView.findViewById(R.id.llSnapshot);
        tvSnapshot = (TextView) dashAdvisoryView.findViewById(R.id.tvSnapshot);
        txtMonthTrends = (TextView) dashAdvisoryView.findViewById(R.id.txtMonthTrends);
        tvSnapshot.setText(BUSINESS_SNAPSHOT_TITLE);
        txtMonthTrends.setText(TRENDS_TITLE);

        tvNameFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvNameFirst);
        tvBranchNameFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvBranchNameFirst);
        tvTopRevenueFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvTopRevenueFirst);
        tvNameSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvNameSecond);
        tvBranchNameSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvBranchNameSecond);
        tvTopRevenueSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvTopRevenueSecond);
        tvNameThird = (TextView) dashAdvisoryView.findViewById(R.id.tvNameThird);
        tvBranchNameThird = (TextView) dashAdvisoryView.findViewById(R.id.tvBranchNameThird);
        tvTopRevenueThird = (TextView) dashAdvisoryView.findViewById(R.id.tvTopRevenueThird);

        tvBottomNameFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomNameFirst);
        tvBottomBranchNameFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomBranchNameFirst);
        tvBottomRevenueFirst = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomRevenueFirst);
        tvBottomNameSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomNameSecond);
        tvBottomBranchNameSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomBranchNameSecond);
        tvBottomRevenueSecond = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomRevenueSecond);
        tvBottomNameThird = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomNameThird);
        tvBottomBranchNameThird = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomBranchNameThird);
        tvBottomRevenueThird = (TextView) dashAdvisoryView.findViewById(R.id.tvBottomRevenueThird);

        tvTopRevenueFirst.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvNameFirst.setText("Raj");
        tvBranchNameFirst.setText("Mumbai");
        tvTopRevenueSecond.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvNameSecond.setText("Raj");
        tvBranchNameSecond.setText("Mumbai");
        tvTopRevenueThird.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvNameThird.setText("Raj");
        tvBranchNameThird.setText("Mumbai");

        tvBottomRevenueFirst.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvBottomNameFirst.setText("Raj");
        tvBottomBranchNameFirst.setText("Mumbai");
        tvBottomRevenueSecond.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvBottomNameSecond.setText("Raj");
        tvBottomBranchNameSecond.setText("Mumbai");
        tvBottomRevenueThird.setText(getResources().getString(R.string.rupee_symbol) + "20,000");
        tvBottomNameThird.setText("Raj");
        tvBottomBranchNameThird.setText("Mumbai");

        btnClient.setOnClickListener(this);
        btnBusinessSnapshot.setOnClickListener(this);
        btnLeads.setOnClickListener(this);


        ivActionableInsights.setOnClickListener(this);
        leadcreation.setOnClickListener(this);

        btnClientTraded.setOnClickListener(this);


        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
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


        ShapeDrawable drawable = new ShapeDrawable(new OvalShape());
        drawable.getPaint().setColor(getResources().getColor(R.color.black));

        //incentive

        spQuarter = (Spinner) dashAdvisoryView.findViewById(R.id.spQuarter);
        tableMData = (TableLayout) dashAdvisoryView.findViewById(R.id.tableMData);
        tableCurrentQuaterStatusFirst = (TableLayout) dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusFirst);
        tableCurrentQuaterStatusSecond = (TableLayout) dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusSecond);
        tableCurrentQuaterStatusThree = (TableLayout) dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusThree);
        tableCurrentQuaterStatusFour = (TableLayout) dashAdvisoryView.findViewById(R.id.tableCurrentQuaterStatusFour);
        tableTeamPerformance = (TableLayout) dashAdvisoryView.findViewById(R.id.tableTeamPerformance);
        tableAdvPerformance = (TableLayout) dashAdvisoryView.findViewById(R.id.tableAdvPerformance);
        spQuarter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                incentiveView(spQuarter.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        String[] quarterArray = getActivity().getResources().getStringArray(R.array.SelectQuarter);
        ArrayList<String> arrEventType = new ArrayList<String>(Arrays.asList(quarterArray));
        ArrayAdapter<String> arrAdapEventType = new ArrayAdapter<>(getMainActivity(), R.layout.simple_spinner_item, arrEventType);
        arrAdapEventType.setDropDownViewResource(R.layout.mdropdown);
        spQuarter.setAdapter(arrAdapEventType);
        tvIncentives = (TextView) dashAdvisoryView.findViewById(R.id.tvIncentives);
        tvPerformance = (TextView) dashAdvisoryView.findViewById(R.id.tvPerformance);
        tvCurrentQuaterStatusFirstHeading = (TextView) dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusFirstHeading);
        tvCurrentQuaterStatusSecondHeading = (TextView) dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusSecondHeading);
        tvCurrentQuaterStatusThreeHeading = (TextView) dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusThreeHeading);
        tvCurrentQuaterStatusFourHeading = (TextView) dashAdvisoryView.findViewById(R.id.tvCurrentQuaterStatusFourHeading);


        mChartAdv = (BarChart) dashAdvisoryView.findViewById(R.id.chart);
        mChartAdv.getDescription().setEnabled(false);
        //        mChartAdv.setDrawBorders(true);
        // scaling can now only be done on x- and y-axis separately
        mChartAdv.setPinchZoom(false);
        mChartAdv.setDrawBarShadow(false);
        mChartAdv.setDrawGridBackground(false);

        Legend legendAdv = mChartAdv.getLegend();
        legendAdv.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendAdv.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendAdv.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendAdv.setDrawInside(true);
        //l.setTypeface(mTfLight);
        legendAdv.setYOffset(0f);
        legendAdv.setXOffset(10f);
        legendAdv.setYEntrySpace(0f);
        legendAdv.setTextSize(8f);
        legendAdv.setEnabled(false);

        xAxis = mChartAdv.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);// this replaces setStartAtZero(true)

        YAxis leftAxis = mChartAdv.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setSpaceTop(5f);

        mChartAdv.getAxisRight().setEnabled(false);
        mChartAdv.setScaleXEnabled(false);
        mChartAdv.setScaleYEnabled(false);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = new String[]{"Q1", "Q2", "Q3", "Q4", "YTD"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });

        //Team Performance

        mChartTeam = (BarChart) dashAdvisoryView.findViewById(R.id.chartTeam);

        mChartTeam.getDescription().setEnabled(false);

//        mChartPer.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        mChartTeam.setPinchZoom(false);

        mChartTeam.setDrawBarShadow(false);

        mChartTeam.setDrawGridBackground(false);

        Legend legendPer = mChartTeam.getLegend();
        legendPer.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legendPer.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legendPer.setOrientation(Legend.LegendOrientation.VERTICAL);
        legendPer.setDrawInside(true);
        //l.setTypeface(mTfLight);
        legendPer.setYOffset(0f);
        legendPer.setXOffset(10f);
        legendPer.setYEntrySpace(0f);
        legendPer.setTextSize(8f);
        legendPer.setEnabled(false);

        XAxis xAxisPer = mChartTeam.getXAxis();
        xAxisPer.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisPer.setDrawGridLines(false);
        xAxisPer.setCenterAxisLabels(true);
        xAxisPer.setAxisMaximum(0f);
        xAxisPer.setGranularity(1f);
        xAxisPer.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Q1", "Q2", "Q3", "Q4", "YTD"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
               /* if (value == -1) {
                    value = 1;
                } else {
                }*/
                return mActivities[(int) (value) % mActivities.length];
            }
        });
        YAxis leftAxisPer = mChartTeam.getAxisLeft();
        leftAxisPer.setDrawGridLines(false);
        leftAxisPer.setSpaceTop(5f);

        mChartTeam.getAxisRight().setEnabled(false);

        //Hierarchy performance
        mChart = (HorizontalBarChart) dashAdvisoryView.findViewById(R.id.chart1);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.getDescription().setEnabled(false);
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightFullBarEnabled(false);
        mChart.setBackgroundColor(Color.WHITE);

        XAxis xAxisTeam = mChart.getXAxis();
        xAxisTeam.setDrawGridLines(false);
        xAxisTeam.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisTeam.setCenterAxisLabels(true);
        //remove axis line
        xAxisTeam.setDrawAxisLine(true);
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
        xAxisTeam.setValueFormatter(new IAxisValueFormatter() {

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
        llReplaceClientFragment = (LinearLayout) dashAdvisoryView.findViewById(R.id.llReplaceClientFragment);


    }

    private String[] mMonths;

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

    private class ChartDataModel {

        private LinkedHashMap<String, Float> monthData = new LinkedHashMap<>();

        public LinkedHashMap<String, Float> getMonthData() {
            return monthData;
        }

        public void setMonthData(LinkedHashMap<String, Float> monthData) {
            this.monthData = monthData;
        }
    }

    private BarData generateBarData(List<ChartDataModel> barDataList) {

       /* List<ChartDataModel> dataList = arrayList;
        List<ChartDataModel> barDataList = new ArrayList<>();

        for (ChartDataModel model : dataList) {
            generateAxisLabel(model);
            barDataList.add(model);

        }*/
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


    public void advLoadChart(JSONArray jsonArray) {
        String ytd = "";
        float groupSpace = 0.25f;
        float barSpace = 0.05f;
        float barWidth = 0.35f;
        List<ChartDataModel> dataList = new ArrayList<ChartDataModel>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                ChartDataModel model = new ChartDataModel();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Name = (jsonObject.isNull("Name") || jsonObject.getString("Name").equals("")) ? "" : jsonObject.getString("Name");
                String Q1 = (jsonObject.isNull("Q1") || jsonObject.getString("Q1").equals("")) ? "0" : jsonObject.getString("Q1");
                String Q2 = (jsonObject.isNull("Q2") || jsonObject.getString("Q2").equals("")) ? "0" : jsonObject.getString("Q2");
                String Q3 = (jsonObject.isNull("Q3") || jsonObject.getString("Q3").equals("")) ? "0" : jsonObject.getString("Q3");
                String Q4 = (jsonObject.isNull("Q4") || jsonObject.getString("Q4").equals("")) ? "0" : jsonObject.getString("Q4");
                String WidgetType = (jsonObject.isNull("WidgetType") || jsonObject.getString("WidgetType").equals("")) ? "" : jsonObject.getString("WidgetType");
                if (jsonObject.has("YTD")) {
                    ytd = (jsonObject.isNull("YTD") || jsonObject.getString("YTD").equals("")) ? "0" : jsonObject.getString("YTD");
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

                if (jsonObject.has("YTD")) {
                    model.getMonthData().put("YTD", Float.parseFloat(ytd));

                }
                dataList.add(model);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }





       /*  ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals4 = new ArrayList<BarEntry>();


        yVals1.add(new BarEntry(0, Float.valueOf(yDataGraphArrAdv.get(1))));
        yVals1.add(new BarEntry(1, Float.valueOf(yDataGraphArrAdv.get(2))));
        yVals1.add(new BarEntry(2, Float.valueOf(yDataGraphArrAdv.get(3))));
        yVals1.add(new BarEntry(3, Float.valueOf(yDataGraphArrAdv.get(4))));

        yVals2.add(new BarEntry(0, Float.valueOf(yDataGraphArrAdv.get(7))));
        yVals2.add(new BarEntry(1, Float.valueOf(yDataGraphArrAdv.get(8))));
        yVals2.add(new BarEntry(2, Float.valueOf(yDataGraphArrAdv.get(9))));
        yVals2.add(new BarEntry(3, Float.valueOf(yDataGraphArrAdv.get(10))));

        yVals3.add(new BarEntry(0, Float.valueOf(yDataGraphArrAdv.get(13))));
        yVals3.add(new BarEntry(1, Float.valueOf(yDataGraphArrAdv.get(14))));
        yVals3.add(new BarEntry(2, Float.valueOf(yDataGraphArrAdv.get(15))));
        yVals3.add(new BarEntry(3, Float.valueOf(yDataGraphArrAdv.get(16))));

       BarDataSet set1, set2, set3, set4;

        if (mChartAdv.getData() != null && mChartAdv.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mChartAdv.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChartAdv.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChartAdv.getData().getDataSetByIndex(2);
            //  set4 = (BarDataSet) mChartAdv.getData().getDataSetByIndex(3);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            // set4.setValues(yVals4);
            mChartAdv.getData().notifyDataChanged();
            mChartAdv.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "");
            set1.setColor(Color.rgb(255, 20, 0));
            set2 = new BarDataSet(yVals2, "");
            set2.setColor(Color.rgb(38, 127, 183));
            set3 = new BarDataSet(yVals3, "");
            set3.setColor(Color.rgb(255, 154, 0));
            //  set4 = new BarDataSet(yVals4, "");
            //set4.setColor(Color.rgb(255, 102, 0));

            // BarData data = new BarData(set1, set2, set3, set4);
            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());
            //data.setValueTypeface(mTfLight);

            mChartAdv.setData(data);
        }*/

        // BarData barData = generateBarData(xAxis,dataList);

        if (generateBarData(dataList) != null) {
            //   data.setData(generateBarData(arrayLists.get(i)));
            mChartAdv.setData(generateBarData(dataList));
        }

        // when ytd comes then uncomment

       /* float groupSpace = 0.18f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.25f; // x4 DataSet*/


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
        //adding head end


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
        mChartAdv.getXAxis().setAxisMaximum(startYear + mChartAdv.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mChartAdv.groupBars(startYear, groupSpace, barSpace);
        mChartAdv.invalidate();

        mChartAdv.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null) {
                    return;
                }
                if (e.getY() == 0.0f) return;

                Bundle args = new Bundle();
                Log.d("index", Float.toString(e.getX() + 0.5f));
                // navigateTo(SECOND_CLICK_DASHBORD, args, getFragmentManager(), true);
            }

            private void pieChartDetail(float sliceIndex) {


            }

            @Override
            public void onNothingSelected() {

            }

        });
        llAdvPerformance.setVisibility(View.VISIBLE);
        tvPerformance.setText("Performance");
    }


    public void teamLoadChart(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Name = (jsonObject.isNull("Name") || jsonObject.getString("Name").equals("")) ? "" : jsonObject.getString("Name");
                String Q1 = (jsonObject.isNull("Q1") || jsonObject.getString("Q1").equals("")) ? "0" : jsonObject.getString("Q1");
                String Q2 = (jsonObject.isNull("Q2") || jsonObject.getString("Q2").equals("")) ? "0" : jsonObject.getString("Q2");
                String Q3 = (jsonObject.isNull("Q3") || jsonObject.getString("Q3").equals("")) ? "0" : jsonObject.getString("Q3");
                String Q4 = (jsonObject.isNull("Q4") || jsonObject.getString("Q4").equals("")) ? "0" : jsonObject.getString("Q4");
                String ytd = (jsonObject.isNull("YTD") || jsonObject.getString("YTD").equals("")) ? "0" : jsonObject.getString("YTD");
                yDataGraphArrTeam.add(Name);
                yDataGraphArrTeam.add(Q1);
                yDataGraphArrTeam.add(Q2);
                yDataGraphArrTeam.add(Q3);
                yDataGraphArrTeam.add(Q4);
                yDataGraphArrTeam.add(ytd);


            }
            yDataGraphBlankArrTeam.add("0");
            yDataGraphBlankArrTeam.add("0");
            yDataGraphBlankArrTeam.add("0");
            yDataGraphBlankArrTeam.add("0");
            yDataGraphBlankArrTeam.add("0");

            tableTeamPerformance.removeAllViews();

            //Adding head
            LayoutInflater layoutInflater = getActivity().getLayoutInflater();
            View tableRowHeader = layoutInflater.inflate(R.layout.frag_performance_team_tab_row, null);
            TextView tvTeamAvgHeader = (TextView) tableRowHeader.findViewById(R.id.tvTeamAvg);
            TextView tvNationalAvgHeader = (TextView) tableRowHeader.findViewById(R.id.tvNationalAvg);

            tvTeamAvgHeader.setText(yDataGraphArrTeam.get(0));
            tvNationalAvgHeader.setText(yDataGraphArrTeam.get(6));


            //Boald type face
            tvTeamAvgHeader.setTypeface(null, Typeface.BOLD);
            tvNationalAvgHeader.setTypeface(null, Typeface.BOLD);

            tableTeamPerformance.addView(tableRowHeader);
            //adding head end


            for (int i = 1; i < 6; i++) {
                View tableRow = layoutInflater.inflate(R.layout.frag_performance_team_tab_row, null);
                TextView tvTeamAvg = (TextView) tableRow.findViewById(R.id.tvTeamAvg);
                TextView tvNationalAvg = (TextView) tableRow.findViewById(R.id.tvNationalAvg);

                tvTeamAvg.setText(yDataGraphArrTeam.get(i));
                tvNationalAvg.setText(yDataGraphArrTeam.get(6 + i));

                tableTeamPerformance.addView(tableRow);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        float groupSpace = 0.18f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.25f; // x4 DataSet


        int groupCount = 5;
        int startYear = 2010;
        int endYear = 2015;


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();


        yVals1.add(new BarEntry(0, Float.valueOf(yDataGraphArrTeam.get(1))));
        yVals1.add(new BarEntry(1, Float.valueOf(yDataGraphArrTeam.get(2))));
        yVals1.add(new BarEntry(2, Float.valueOf(yDataGraphArrTeam.get(3))));
        yVals1.add(new BarEntry(3, Float.valueOf(yDataGraphArrTeam.get(4))));
        yVals1.add(new BarEntry(4, Float.valueOf(yDataGraphArrTeam.get(5))));
        // yVals1.add(new BarEntry(5,Float.valueOf(yDataGraphArr.get(10))));

        yVals2.add(new BarEntry(0, Float.valueOf(yDataGraphArrTeam.get(7))));
        yVals2.add(new BarEntry(1, Float.valueOf(yDataGraphArrTeam.get(8))));
        yVals2.add(new BarEntry(2, Float.valueOf(yDataGraphArrTeam.get(9))));
        yVals2.add(new BarEntry(3, Float.valueOf(yDataGraphArrTeam.get(10))));
        yVals2.add(new BarEntry(4, Float.valueOf(yDataGraphArrTeam.get(11))));
        // yVals2.add(new BarEntry(5,Float.valueOf(yDataGraphArr.get(11))));

        try {
            yVals3.add(new BarEntry(0, Float.valueOf(yDataGraphBlankArrTeam.get(0))));
            yVals3.add(new BarEntry(1, Float.valueOf(yDataGraphBlankArrTeam.get(1))));
            yVals3.add(new BarEntry(2, Float.valueOf(yDataGraphBlankArrTeam.get(2))));
            yVals3.add(new BarEntry(3, Float.valueOf(yDataGraphBlankArrTeam.get(3))));
            yVals3.add(new BarEntry(4, Float.valueOf(yDataGraphBlankArrTeam.get(4))));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        BarDataSet set1, set2, set3;

        if (mChartTeam.getData() != null && mChartTeam.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) mChartTeam.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) mChartTeam.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) mChartTeam.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
            set2.setValues(yVals2);
            set3.setValues(yVals3);
            mChartTeam.getData().notifyDataChanged();
            mChartTeam.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(yVals1, "");
            set1.setColor(Color.rgb(255, 20, 0));
            set2 = new BarDataSet(yVals2, "");
            set2.setColor(Color.rgb(38, 127, 183));
            set3 = new BarDataSet(yVals3, "");
            set3.setColor(Color.rgb(255, 255, 255));

            BarData data = new BarData(set1, set2, set3);

            data.setValueFormatter(new LargeValueFormatter());

            mChartTeam.setData(data);
        }


        // specify the width each bar should have
        mChartTeam.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        mChartTeam.getXAxis().setAxisMinimum(startYear);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        mChartTeam.getXAxis().setAxisMaximum(startYear + mChartTeam.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        mChartTeam.groupBars(startYear, groupSpace, barSpace);
        mChartTeam.invalidate();

        mChartTeam.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
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

    private void setIndicatorColor(int position, String page) {
        switch (page) {

            case "Default":

                for (int i = 0; i < mTabsLinearLayoutSnapshot.getChildCount(); i++) {
                    TextView tv = (TextView) mTabsLinearLayoutSnapshot.getChildAt(i);
                    if (i == position) {
                        tv.setTextColor(Color.BLACK);
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.light_black));
                    }
                }


            case "Snapshot":
                for (int i = 0; i < mTabsLinearLayoutSnapshot.getChildCount(); i++) {
                    TextView tv = (TextView) mTabsLinearLayoutSnapshot.getChildAt(i);
                    if (i == position) {
                        tv.setTextColor(Color.BLACK);
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.light_black));
                    }
                }
                break;
        }

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
                //  openWebUrl(Uri.parse(CRMWebServiceHandler.INCENTIVE_PDF_URL));
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
            } else if (id == R.id.btnBASnapshot) {
                hideKeyboard(view);
                loadBASnapshot();
            } else if (id == R.id.btnBACategory) {
                hideKeyboard(view);
                loadBACategory();
            } else if (id == R.id.btnBusinessSnapshotAdv) {
                hideKeyboard(view);
                loadBusinessSnapshotAdv();
            } else if (id == R.id.btnBusinessSnapshotOneUp) {
                hideKeyboard(view);
                loadBusinessSnapshotOneUp();
            } else if (id == R.id.btnBusinessLead) {
                hideKeyboard(view);
                loadBusinessLead();
            } else if (id == R.id.btnClientTraded) {
                hideKeyboard(view);
                loadClientTraded();
            } else if (id == R.id.leadcreation) {
                hideKeyboard(view);
               /* if (AccountDetails.isFranchiseUser()) {
                    floatingActionsMenu.collapse();
                    args.putString("", "");
                    args.putString("NAT_TO", ADVISORY);
                    navigateTo(NAV_TO_LEAD_CREATION, args, getFragmentManager(), true);
                } else {
                    floatingActionsMenu.collapse();
                    args.putString("", "");
                    args.putString("NAT_TO", ADVISORY);
                    navigateTo(NAV_TO_LEAD_CREATION, args, getFragmentManager(), true);
                }*/
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
                toggleSectionExpansion(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            }

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    /* private void loadSnapshot() {
         btnTeamSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
         btnTeamSnapshot.setTextColor(getResources().getColor(R.color.white));
         btnTeamLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
         btnTeamLeads.setTextColor(getResources().getColor(R.color.Button_text_color));
         SnapshotFragment snapshotFragment = SnapshotFragment.newInstance(getContext());
         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceTeamFragment, snapshotFragment).commit();

     }

     private void loadTeamLead() {
         Bundle args = new Bundle();
         btnTeamSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
         btnTeamSnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
         btnTeamLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
         btnTeamLeads.setTextColor(getResources().getColor(R.color.white));
         args.putSerializable(TEAM_LEAD, currentDashboard);
         TeamLeadsFragment leadsFragment = TeamLeadsFragment.newInstance(getContext());
         leadsFragment.setArguments(args);
         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceTeamFragment, leadsFragment).commit();
     }
 */
    private void loadLead() {
        Bundle args = new Bundle();
        btnBusinessSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClient.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClient.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClientTraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClientTraded.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnLeads.setTextColor(getResources().getColor(R.color.white));
        ClientsLeadsFragment leadsFragment = ClientsLeadsFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceClientFragment, leadsFragment).commit();
    }

    private void loadClientTraded() {
        Bundle args = new Bundle();
        btnBusinessSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClient.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClient.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnLeads.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClientTraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnClientTraded.setTextColor(getResources().getColor(R.color.white));
        ClientsTradedYesterdayFragment clientsFragment = ClientsTradedYesterdayFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);

        clientsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceClientFragment, clientsFragment).commit();
    }

    private void loadLeadBusinessSnapShot() {
        btnBusinessSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBusinessSnapshot.setTextColor(getResources().getColor(R.color.white));
        btnClient.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClient.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnLeads.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClientTraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClientTraded.setTextColor(getResources().getColor(R.color.Button_text_color));
        Bundle args = new Bundle();
        BusinessSnapshotFragment leadsFragment = BusinessSnapshotFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceClientFragment, leadsFragment).commit();
    }

    private void loadTargetAchievement() {
        Bundle args = new Bundle();
        TargetAchievementFragment leadsFragment = TargetAchievementFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llTargetAchievementADV, leadsFragment).commit();
    }

    private void loadTeamTargetAchievement() {
        Bundle args = new Bundle();
        TargetAchievementTeamFragment leadsFragment = TargetAchievementTeamFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llTeamTargetAchievement, leadsFragment).commit();
    }

    private void loadMonthTrends() {
        Bundle args = new Bundle();
        TrendsTabFragment leadsFragment = TrendsTabFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llMonthvsTrends, leadsFragment).commit();
    }

    private void loadBusinessSnapShot() {
        Bundle args = new Bundle();
        BusinessSnapshotFragment leadsFragment = BusinessSnapshotFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        leadsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llBusinessSnapshotHead, leadsFragment).commit();
    }

    private void loadClients() {
        btnBusinessSnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClient.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnClient.setTextColor(getResources().getColor(R.color.white));
        btnLeads.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnLeads.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnClientTraded.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnClientTraded.setTextColor(getResources().getColor(R.color.Button_text_color));
        ClientsFragment clientsFragment = ClientsFragment.newInstance(getContext());
        args.putSerializable(DASHBOARD_MODEL, currentDashboard);
        clientsFragment.setArguments(args);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceClientFragment, clientsFragment).commit();
    }

    private void loadBASnapshot() {
        btnBASnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBASnapshot.setTextColor(getResources().getColor(R.color.white));
        btnBACategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBACategory.setTextColor(getResources().getColor(R.color.Button_text_color));
        BASnapshotFragment BASnapshot = BASnapshotFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBASnapshotFragment, BASnapshot).commit();
    }

    private void loadBACategory() {
        btnBASnapshot.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBASnapshot.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBACategory.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBACategory.setTextColor(getResources().getColor(R.color.white));
        BACategoryFragment BACategory = BACategoryFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBASnapshotFragment, BACategory).commit();
    }

    private void loadBusinessSnapshotAdv() {
        btnBusinessSnapshotAdv.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBusinessSnapshotAdv.setTextColor(getResources().getColor(R.color.white));
        btnBusinessSnapshotOneUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshotOneUp.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBusinessLead.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessLead.setTextColor(getResources().getColor(R.color.Button_text_color));
        BusinessSnapshotAdvFragment businessSnapshotAdv = BusinessSnapshotAdvFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBusinessSnapshotFragment, businessSnapshotAdv).commit();
    }

    private void loadBusinessSnapshotOneUp() {
        btnBusinessSnapshotAdv.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshotAdv.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBusinessSnapshotOneUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBusinessSnapshotOneUp.setTextColor(getResources().getColor(R.color.white));
        btnBusinessLead.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessLead.setTextColor(getResources().getColor(R.color.Button_text_color));
        BSDirectOneUpFragment businessSnapshotOneUp = BSDirectOneUpFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBusinessSnapshotFragment, businessSnapshotOneUp).commit();
    }

    private void loadBusinessLead() {
        btnBusinessSnapshotAdv.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshotAdv.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBusinessSnapshotOneUp.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));
        btnBusinessSnapshotOneUp.setTextColor(getResources().getColor(R.color.Button_text_color));
        btnBusinessLead.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange));
        btnBusinessLead.setTextColor(getResources().getColor(R.color.white));
        BusinessLeadFragment businessLead = BusinessLeadFragment.newInstance(getContext());
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.llReplaceBusinessSnapshotFragment, businessLead).commit();
    }


    private void actionableInsightDialog() {
        if (currentDashboard == null) return;

        dialog = new Dialog(getMainActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_actionable_insights);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

        /*List<WidgetModel> widgetModelList = currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
        advisoryActionableInsightAdapter = new AdvisoryActionableInsightAdapter(getActivity(), widgetModelList);
        lvActionableInsights.setAdapter(advisoryActionableInsightAdapter);
        advisoryActionableInsightAdapter.notifyDataSetChanged();*/

        dialog.show();
    }

    public class AdvisoryActionableInsightAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        Context mContext;
        List<WidgetModel> advisoryActionableInsightModelList;
        //private ArrayList<AdvisoryActionableInsightModel> arraylist;

        public AdvisoryActionableInsightAdapter(Context c, List<WidgetModel> advisoryActionableInsightModelList) {
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

            final WidgetModel advisoryActionableInsightModel = advisoryActionableInsightModelList.get(i);
            holder.ivActionableInsights = (ImageView) view.findViewById(R.id.ivActionableInsights);
            holder.pbLoadingIndicator = (ProgressBar) view.findViewById(R.id.pbLoadingIndicator);
            holder.tvActionableInsights = (TextView) view.findViewById(R.id.tvActionableInsights);
            holder.tvActionableCount = (TextView) view.findViewById(R.id.tvActionableCount);
            holder.rlActionable = (RelativeLayout) view.findViewById(R.id.rlActionable);

            final Map<String, String> addtionalParameterMap = new HashMap<>();
            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
            Glide.with(getContext()).load(advisoryActionableInsightModel.getIconUrl2().replaceAll(" ", "%20"))
                    //remove Chache
                    .asBitmap().placeholder(R.drawable.ic_create_meeting).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new BitmapImageViewTarget(holder.ivActionableInsights) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    //circularBitmapDrawable.setCircular(true);
                    holder.ivActionableInsights.setImageDrawable(circularBitmapDrawable);
                }
            });
            holder.tvActionableInsights.setText(advisoryActionableInsightModel.getName());
            holder.tvActionableCount.setText(advisoryActionableInsightModel.getActionableCount());
            final DashboardGroupModel groupModel = currentDashboard.getGroupModel(ACTIONABLE);

            holder.rlActionable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // addtionalParameterMap.put(ROW_FILTER, advisoryActionableInsightModel.getType());
                    //  holder.rlActionable.setBackgroundColor(getResources().getColor(R.color.white));
                    Bundle args = new Bundle();
                    args.putString(SYSTEM_NAME, advisoryActionableInsightModel.getSystemName());
                    args.putString(LABEL, ACTIONABLE);
                    args.putString(WIDGETNAME, advisoryActionableInsightModel.getName());
                    args.putSerializable(MAP, (Serializable) addtionalParameterMap);
                    List<WidgetModel> widgetList = groupModel.getWidgetList();
                    if (widgetList != null && widgetList.size() > 0) {
                        for (WidgetModel model1 : widgetList) {
                            if (advisoryActionableInsightModel.getSystemName().equalsIgnoreCase(model1.getSystemName())) {
                                //    model1.getWidgetTypeList();
                                ArrayList<WidgetFilterModel> filterList = (ArrayList<WidgetFilterModel>) model1.getWidgetFilterList();
                                ArrayList<TypeActionableFilterModel> spinnerFilter = (ArrayList<TypeActionableFilterModel>) model1.getSpinnerFilterList();
                                if (!filterList.isEmpty()) {
                                    args.putParcelableArrayList(FILTER_LIST, filterList);
                                }
                                if (!spinnerFilter.isEmpty()) {
                                    args.putParcelableArrayList(SPINNER_LIST, spinnerFilter);
                                }
                            }
                        }
                    }
                    // args.putStringArrayList(FILTER_LIST, advisoryActionableInsightModel.getSpinnerFilterList());
                    if (!advisoryActionableInsightModel.getActionableCount().equalsIgnoreCase("0")) {
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

    @Override
    public void onResume() {
        super.onResume();
  /*      try {
            if (AccountDetails.getToken() == null) {
                Intent intent = new Intent(getMainActivity(), SplashActivity.class);
                startActivity(intent);
            } else {
                for (FloatingActionButton sFab : dashboardBtnList) {
                    floatingActionsMenu.removeButton(sFab);
                }
                dashboardBtnList.clear();
                dashboardList.clear();
                loadDahboardMetaData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
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
                    loadDahboardMetaData();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDahboardMetaData() {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            dashboardList.clear();

            String serviceMethod = "Dashboard/GetDashboardWithWidgetsWithGroup/";
            Map inputMap = new HashMap<>();
            inputMap.put("BusinessProfileId", businessProfileIdForDashboard);
            inputMap.put(PRODUCT_ID, productId);
            inputMap.put(EMPLOYEE_CODE, employeeCode);

            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();

                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONArray responseJSONArray = response.getJSONArray("Response");
                                    for (int i = 0; i < responseJSONArray.length(); i++) {
                                        JSONObject responseJsonObj = responseJSONArray.getJSONObject(i);
                                        fillAvailableWidgetModel(responseJsonObj);
                                    }
                                    if (dashboardList.size() > 0) {
                                        currentDashboard = dashboardList.get(0);
                                        currentDashboard.setBusinessProfileIdForDashboard(businessProfileIdForDashboard);
                                        currentDashboard.setProductId(productId);
                                        currentDashboard.setEmployeeCode(employeeCode);
                                        createFloatingButtons();
                                        renderDashboard();
                                    }

                                } else {
                                    hideProgress();
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    hideProgress();
                                    return;
                                }

                            } else {
                                //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "Ok", true, null);
                                hideProgress();
                                return;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "Ok", true, null);
                            hideProgress();
                            return;

                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                        hideProgress();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        hideProgress();
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        hideProgress();
                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                        return;
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);

                        MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        hideProgress();
                        return;
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
                return;
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);
            return;
        }
    }

    private Map getAddtionalParameterMapActionable() {

        Map addtionalParameterMap = new HashMap<>();
        try {
            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return addtionalParameterMap;
    }

    private void loadActionableCount(final String systemName) {
        if (currentDashboard == null) return;

        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();
            inputMap.put("WidgetSystemName", systemName);
            inputMap.put("AddtionalParameters", getAddtionalParameterMapActionable());
            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");
                                    ArrayList<String> arrayList = new ArrayList<String>();
                                  /*  "Table1" : -[
                                    -{
                                            "Count" : 0
                                       }
                                       ]*/
                                    String type = "";
                                    ArrayList<TypeActionableFilterModel> typeList = new ArrayList<TypeActionableFilterModel>();


                                    if (responseJsonObj.has("Table")) {
                                        JSONArray tableArray = responseJsonObj.getJSONArray("Table");
                                        for (int i = 0; i < tableArray.length(); i++) {
                                            JSONObject getJsonObject = tableArray.getJSONObject(i);
                                            if (getJsonObject.has("Type")) {
                                                type = (getJsonObject.isNull("Type") || getJsonObject.getString("Type").equals("")) ? "" : getJsonObject.getString("Type");
                                                TypeActionableFilterModel tmpType = new TypeActionableFilterModel(type);
                                                typeList.add(tmpType);
                                            }
                                        }


                                    }

                                    try {
                                        String count = "";
                                        if (responseJsonObj.has("Table1")) {
                                            JSONArray tableArray = responseJsonObj.getJSONArray("Table1");
                                            JSONObject getJsonObject = tableArray.getJSONObject(0);
                                            count = (getJsonObject.isNull("Count") || getJsonObject.getString("Count").equals("")) ? "0" : getJsonObject.getString("Count");
                                        }
                                        currentDashboard.setActionableCount(systemName, count, typeList);

                                        advisoryActionableInsightAdapter.showCount(true, systemName);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        currentDashboard.setActionableCount(systemName, "", new ArrayList<TypeActionableFilterModel>());
                                    }
                                    hideProgress();
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
                                //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
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
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    private void fillAvailableWidgetModel(JSONObject responseJsonObj) throws Exception {
        if (responseJsonObj != null) {
            String dashboardId = responseJsonObj.getString("Id");
            String dashboardName = responseJsonObj.getString("Name");
            String dashboardFor = responseJsonObj.getString("DashboardFor");

            DashboardModel dashboardModel = new DashboardModel();
            dashboardList.add(dashboardModel);
            dashboardModel.setId(dashboardId);
            dashboardModel.setName(dashboardName);
            dashboardModel.setDashboardFor(dashboardFor);

            JSONArray groupArray = responseJsonObj.getJSONArray("Groups");

            List<DashboardGroupModel> mainGroupList = getGroupModelFromJsonArray(groupArray);
            dashboardModel.getGroupList().clear();
            dashboardModel.getGroupList().addAll(mainGroupList);
        }

    }


    private List<DashboardGroupModel> getGroupModelFromJsonArray(JSONArray groupArray) throws Exception {
        List<DashboardGroupModel> groupList = new ArrayList();

        for (int liX = 0; liX < groupArray.length(); liX++) {

            JSONObject groupObj = groupArray.getJSONObject(liX);
            if (groupObj != null) {
                DashboardGroupModel groupModel = new DashboardGroupModel();
                groupList.add(groupModel);

                String dashBordGroupId = groupObj.getString("DashBoardGroupId");
                String groupName = groupObj.getString("GroupName");
                String groupSystemName = groupObj.getString("SystemName");
                String imageUrl1 = groupObj.getString("ImageUrl");
                //  String imageUrl2 = groupObj.getString("ImageUrl2");


                groupModel.setId(dashBordGroupId);
                groupModel.setDisplayName(groupName);
                groupModel.setSystemName(groupSystemName);
                groupModel.setIconUrl1(imageUrl1);
                //  groupModel.setIconUrl2(imageUrl2);
                boolean selected = (liX == 0) ? true : false;
                groupModel.setSelected(selected);


                JSONArray childrenGroupArray = groupObj.getJSONArray("Children");
                if (childrenGroupArray != null && childrenGroupArray.length() > 0) {
                    List<DashboardGroupModel> childrenGroup = getGroupModelFromJsonArray(childrenGroupArray);
                    groupModel.getSubGroupList().addAll(childrenGroup);
                }

                JSONArray availableWidgets = groupObj.getJSONArray("AvailableWidgets");
                List<WidgetModel> widgetModelList = getWidgetModelFromAvailableWidgets(availableWidgets);

                groupModel.getWidgetList().addAll(widgetModelList);


                //filter list
                groupModel.setGroupFilterList(getFilterList(groupObj));
            }
        }
        return groupList;
    }

    /**
     * this method will collect the filter data (dropdown)
     *
     * @param jsonObject
     * @return
     */
    public List<WidgetFilterModel> getFilterList(JSONObject jsonObject) {
        //Timber.v("All " + jsonObject.toString());
        List<WidgetFilterModel> filterList = new ArrayList<>();
        try {

            if (jsonObject.has("AvailableFilter")) {
                JSONArray filterArray = jsonObject.getJSONArray("AvailableFilter");


                for (int filterCount = 0; filterCount < filterArray.length(); filterCount++) {

                    List<FilterRowData> filterRowList = new ArrayList<>();

                    JSONObject filterObject = filterArray.getJSONObject(filterCount);
                    String filterSysName = filterObject.getString("FilterSysName");
                    String keyValue = filterObject.getString("KeyValue");

                    JSONObject filterData = filterObject.getJSONObject("FilterData");

                    if (filterData.has("Table")) {
                        JSONArray filterRowArray = filterData.getJSONArray("Table");
                        for (int filterRowCount = 0; filterRowCount < filterRowArray.length(); filterRowCount++) {
                            JSONObject rowObj = filterRowArray.getJSONObject(filterRowCount);

                            String text = rowObj.getString("text");
                            String value = rowObj.getString("value");
                            FilterRowData rowData = new FilterRowData(filterRowCount, text, value, keyValue);
                            filterRowList.add(rowData);
                        }
                    }

                    WidgetFilterModel filterModel = new WidgetFilterModel(filterSysName, keyValue, filterRowList);
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
     * @param jsonArray
     * @return
     * @throws Exception
     */
    private List<WidgetModel> getWidgetModelFromAvailableWidgets(JSONArray jsonArray) throws Exception {
        List<WidgetModel> widgetModelList = new ArrayList<>();

        for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(ctr);

            if (jsonObject != null) {
                WidgetModel widgetModel = new WidgetModel();

                widgetModelList.add(widgetModel);

                String id = jsonObject.isNull("Id") ? "" : jsonObject.getString("Id");
                String displayName = jsonObject.isNull("Name") ? "" : jsonObject.getString("Name");
                String systemName = jsonObject.isNull("SystemName") ? "" : jsonObject.getString("SystemName");
                String iconUrl1 = jsonObject.isNull("ImageUrl") ? "" : jsonObject.getString("ImageUrl");
                String iconUr2 = jsonObject.isNull("ImageUrl2") ? "" : jsonObject.getString("ImageUrl2");

                JSONArray WidgetType = jsonObject.getJSONArray("WidgetType");

                if (WidgetType != null) {
                    List<WidgetType> childrenGroup = getWidgetTypeFromAvailableWidgets(WidgetType);
                    widgetModel.setWidgetTypeList(childrenGroup);
                }


                boolean selected = (ctr == 0) ? true : false;
                widgetModel.setSelected(selected);
                widgetModel.setId(id);
                widgetModel.setName(displayName);
                widgetModel.setSystemName(systemName);
                widgetModel.setIconUrl1(iconUrl1);
                widgetModel.setIconUrl2(iconUr2);
                widgetModel.setWidgetFilterList(getFilterList(jsonObject));
                widgetModel.setPreviousSelectedPosition(ctr);

            }
        }
        return widgetModelList;
    }


    private List<WidgetType> getWidgetTypeFromAvailableWidgets(JSONArray jsonArray) throws Exception {
        List<WidgetType> widgetModelList = new ArrayList<>();

        for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(ctr);

            if (jsonObject != null) {
                String widgetTypes = jsonObject.isNull("WidgetTypes") ? "" : jsonObject.getString("WidgetTypes");
                String title = jsonObject.isNull("Title") ? "" : jsonObject.getString("Title");
                widgetModelList.add(new WidgetType(widgetTypes, title));

            }
        }
        return widgetModelList;
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

            List<WidgetModel> widgetModelList = currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
            advisoryActionableInsightAdapter = new AdvisoryActionableInsightAdapter(getActivity(), widgetModelList);
        }
        if (currentDashboard.isGroupExists(MyClient_MyLead)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            txtClients.setText(currentDashboard.getGroupModel(MyClient_MyLead).getDisplayName());
            btnClient.setVisibility(View.VISIBLE);
            btnLeads.setVisibility(View.VISIBLE);
            btnBusinessSnapshot.setVisibility(View.GONE);
            btnClientTraded.setVisibility(View.GONE);
            // loadLead();
            loadClients();
            widgetModelList = currentDashboard.getGroupModel(MyClient_MyLead).getWidgetList();
            DashboardSystemModel.setLeadSystemName(MyClient_MyLead);
            DashboardSystemModel.setClientSystemName(MyClient_MyLead);
        } else if (currentDashboard.isGroupExists(MY_LEAD)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            txtClients.setText(currentDashboard.getGroupModel(MY_LEAD).getDisplayName());
            btnClient.setVisibility(View.GONE);
            btnBusinessSnapshot.setVisibility(View.GONE);
            btnClientTraded.setVisibility(View.GONE);
            loadLead();
            widgetModelList = currentDashboard.getGroupModel(MY_LEAD).getWidgetList();
            DashboardSystemModel.setLeadSystemName(MY_LEAD);
        }
        if (currentDashboard.isGroupExists(SYS_LEAD_SNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            txtClients.setText(currentDashboard.getGroupModel(SYS_LEAD_SNAPSHOT).getDisplayName());
            btnClient.setVisibility(View.GONE);
            btnClientTraded.setVisibility(View.GONE);
            btnBusinessSnapshot.setVisibility(View.VISIBLE);
            btnLeads.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            loadLeadBusinessSnapShot();
            //  loadLead();
            widgetModelList = currentDashboard.getGroupModel(SYS_LEAD_SNAPSHOT).getWidgetList();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_LEAD_SNAPSHOT);
            DashboardSystemModel.setLeadSystemName(SYS_LEAD_SNAPSHOT);
        }

        if (currentDashboard.isGroupExists(TEAM_LEAD)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            btnClientTraded.setVisibility(View.GONE);
            btnBusinessSnapshot.setVisibility(View.GONE);
            txtClients.setText(currentDashboard.getGroupModel(TEAM_LEAD).getDisplayName());
            btnClient.setVisibility(View.GONE);
            loadLead();
            DashboardSystemModel.setLeadSystemName(TEAM_LEAD);
        }
        if (currentDashboard.isGroupExists(SYS_MY_CLIENTS_SNAPSHOOT)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            btnBusinessSnapshot.setVisibility(View.GONE);
            btnLeads.setVisibility(View.GONE);
            txtClients.setText(currentDashboard.getGroupModel(SYS_MY_CLIENTS_SNAPSHOOT).getDisplayName());
            btnClient.setVisibility(View.VISIBLE);
            btnClientTraded.setVisibility(View.VISIBLE);
            loadClients();
            // loadClients();
            //      widgetModelList = currentDashboard.getGroupModel(MyClient_MyLead).getWidgetList();
            DashboardSystemModel.setClientSystemName(SYS_MY_CLIENTS_SNAPSHOOT);
            DashboardSystemModel.setClientTradedSystemName(SYS_MY_CLIENTS_SNAPSHOOT);
        }
        if (currentDashboard.isGroupExists(SYS_TEAM_SNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            txtClients.setText(currentDashboard.getGroupModel(SYS_TEAM_SNAPSHOT).getDisplayName());
            btnClient.setVisibility(View.GONE);
            btnBusinessSnapshot.setVisibility(View.VISIBLE);
            btnLeads.setVisibility(View.GONE);
            btnClientTraded.setVisibility(View.GONE);
            loadLeadBusinessSnapShot();
            btnBusinessSnapshot.setText("Team Snapshot");
            //  loadLead();
            widgetModelList = currentDashboard.getGroupModel(SYS_TEAM_SNAPSHOT).getWidgetList();
            DashboardSystemModel.setBusinessSnapshotSystemName(SYS_TEAM_SNAPSHOT);
        }
        if (currentDashboard.isGroupExists(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT)) {
            llClient.setVisibility(View.VISIBLE);
            txtClients.setText(currentDashboard.getGroupModel(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT).getDisplayName());
            btnClient.setVisibility(View.GONE);
            btnClientTraded.setVisibility(View.GONE);
            btnBusinessSnapshot.setVisibility(View.VISIBLE);
            btnLeads.setVisibility(View.VISIBLE);
            setIcon(llClientsLeads, ivClientLeads);
            if (llClientsLeads.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llClientsLeads, ivClientLeads);
            }
            loadLeadBusinessSnapShot();
            //  loadLead();
            widgetModelList = currentDashboard.getGroupModel(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT).getWidgetList();
            DashboardSystemModel.setBusinessSnapshotSystemName(SNAPSHOT_PRODUCTSALESDIRECT1UP_BUSINESSSNAPSHOT);
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
            txtTarget.setText(currentDashboard.getGroupModel(SELF_TARGET_ACHIEVEMNET).getDisplayName());
            setIcon(llTargetAchievementADV, ivTargetAchievementADV);
            if (llTargetAchievementADV.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llTargetAchievementADV, ivTargetAchievementADV);
            }
            llTarget.setVisibility(View.VISIBLE);
            loadTargetAchievement();
            DashboardSystemModel.setTargetAchivementSystemName(SELF_TARGET_ACHIEVEMNET);
        }

        if (currentDashboard.isGroupExists(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT)) {
            txtTeamTarget.setText(currentDashboard.getGroupModel(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT).getDisplayName());
            setIcon(llTeamTargetAchievement, ivTeamTargetAchievement);
            if (llTeamTargetAchievement.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llTeamTargetAchievement, ivTeamTargetAchievement);
            }
            llTeamTarget.setVisibility(View.VISIBLE);
            loadTeamTargetAchievement();
            DashboardSystemModel.setTargetAchivementSystemName(TEAM_TARGETVSACHIEVEMENT_TAB1CONTENT);
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


        //region PerformanceIncentive

        if (currentDashboard.isGroupExists(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
            tvPerformanceAndIncentivesHeader.setText(currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            // setDropdownData(PERFORMANCE_INCENTIVE_ACHIEVEMNET);
            setIncentiveVisibility(PERFORMANCE_INCENTIVE_ACHIEVEMNET);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            }
        } else if (currentDashboard.isGroupExists(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)) {
            tvPerformanceAndIncentivesHeader.setText(currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            // setDropdownData(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES);
            setIncentiveVisibility(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            }
        } else if (currentDashboard.isGroupExists(MY_PERFORMANCE_INCENTIVE)) {
            tvPerformanceAndIncentivesHeader.setText(currentDashboard.getGroupModel(PERFORMANCE_INCENTIVE_ACHIEVEMNET).getDisplayName());
            llPerformanceAndIncentives.setVisibility(View.VISIBLE);
            //  setDropdownData(MY_PERFORMANCE_INCENTIVE);
            setIncentiveVisibility(MY_PERFORMANCE_INCENTIVE);
            setIcon(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
            if (llPerformanceAndIncentivesHeader.getVisibility() != View.VISIBLE) {
                toggleSectionExpansion(llPerformanceAndIncentivesHeader, ivPerformanceAndIncentivesHeader);
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
            /*if (widgetList != null && widgetList.size() > 0) {
                WidgetModel model = widgetList.get(0);
                widgetSystemName = model.getSystemName();
                //set Widget Type
                List<WidgetType> widgetTypeList = model.getWidgetTypeList();
                for (int i = 0; i < widgetTypeList.size(); i++) {
                    String widgetTypes = widgetTypeList.get(i).getWidgetTypes();
                    if ((widgetTypes.equalsIgnoreCase(PERFORMANCE_PARAMETERS) && systemName.equalsIgnoreCase(PERFORMANCE_INCENTIVE_ACHIEVEMNET))
                            || (widgetTypes.equalsIgnoreCase(PERFORMANCE_PARAMETERS) && systemName.equalsIgnoreCase(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES))) {
                        llIncentive.setVisibility(View.VISIBLE);
                        tvPerformanceAndIncentives.setText(widgetTypeList.get(i).getTitle());
                    }
                    if (widgetTypes.equalsIgnoreCase(QUARTER_STATUS) && systemName.equalsIgnoreCase(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
                        llCurrentQuarterStatusAdviosory.setVisibility(View.VISIBLE);
                        tvQuarterStatus.setText(widgetTypeList.get(i).getTitle());
                    }
                    if (widgetTypes.equalsIgnoreCase(PERFORMANCE_COMPARISON) && systemName.equalsIgnoreCase(PERFORMANCE_INCENTIVE_ACHIEVEMNET)) {
                        llAdvPerformance.setVisibility(View.VISIBLE);
                    }
                    if (widgetTypes.equalsIgnoreCase(PERFORMANCE_COMPARISON) && systemName.equalsIgnoreCase(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)) {
                        llTeamPerformance.setVisibility(View.VISIBLE);
                    }
                    if (widgetTypes.equalsIgnoreCase(QUARTER_STATUS) && systemName.equalsIgnoreCase(OAOSALESONEUP_TEAMPERFORMANCEANDINCENTIVES)) {
                        llCurrentQuarterStatusOneUp.setVisibility(View.VISIBLE);
                    }

                }
            }*/
        }
    }

   /* private void setDropdownData(String widgetSystemName) {
        if (currentDashboard != null) {
            DashboardGroupModel groupModel = currentDashboard.getGroupModel(widgetSystemName);

            List<WidgetModel> widgetList = groupModel.getWidgetList();
            if (widgetList != null && widgetList.size() > 0) {
                WidgetModel model = widgetList.get(0);
                List<WidgetFilterModel> filterList = model.getWidgetFilterList();
                if (filterList != null && filterList.size() == 1) {
                    ArrayAdapter<FilterRowData> adapter1 = new ArrayAdapter<FilterRowData>(getActivity(), android.R.layout.simple_spinner_item, filterList.get(0).getFilterData());
                    adapter1.setDropDownViewResource(R.layout.mdropdown);
                    FilterRowData obj = new FilterRowData(0, "YTD", "", "");
                    adapter1.add(obj);
                    spQuarter.setAdapter(adapter1);

                }
            }
        }
    }*/


    private Map getAddtionalParameterMap() {
        Map addtionalParameterMap = new HashMap<>();
        try {
            //  FilterRowData filter1 = (FilterRowData) spQuarter.getSelectedItem();
            //   addtionalParameterMap.put(filter1.getKeyValue(), filter1.getValue());


            addtionalParameterMap.put(SELF_KEY, currentDashboard.getDashboardFor());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return addtionalParameterMap;
    }

    private void updateActionableCount() {
        /*if (advisoryActionableInsightAdapter != null) {
            advisoryActionableInsightAdapter.clear();
        }*/

        if (currentDashboard == null) return;

        if (currentDashboard.isGroupExists(ACTIONABLE)) {
            //draw menu list
            List<WidgetModel> actionableWidgetList = currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
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


    private void incentiveWebServiceCall(final String widgetSystemName) {
        // Timber.v(text);
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);

            //dashboard for
            inputMap.put("BusinessProfileId", businessProfileIdForDashboard);
            inputMap.put(PRODUCT_ID, productId);
            inputMap.put(EMPLOYEE_CODE, employeeCode);


            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(STATUS);

                             /*   "Table1":[{"Name":" My Incentive","Q1":"20000","Q2":"40000","Q3":"15000","Q4":"25000"},
                                {"Name":" Branch Average","Q1":"23000","Q2":"12000","Q3":"30000","Q4":"25000"},
                                {"Name":" National Average","Q1":"20000","Q2":"40000","Q3":"25000","Q4":"30000"}]*/


                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");

                                 /*   yDataGraphArrAdv.clear();
                                    xdataYearArrAdv.clear();
                                    arrayListIncentive.clear();
                                    yDataGraphArrTeam.clear();
                                    xdataYearArrTeam.clear();

                                    xdataYearArrAdv.add("Q1");
                                    xdataYearArrAdv.add("Q2");
                                    xdataYearArrAdv.add("Q3");
                                    xdataYearArrAdv.add("Q4");

                                    xdataYearArrTeam.add("Q1");
                                    xdataYearArrTeam.add("Q2");
                                    xdataYearArrTeam.add("Q3");
                                    xdataYearArrTeam.add("Q4");
                                    xdataYearArrTeam.add("YTD");*/

                                    if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        incentiveData(jsonArray);
                                    }
                                    if (responseJsonObj.has("Table1")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table1");
                                        // perfotmanceData(jsonArray);
                                        advLoadChart(jsonArray);
                                    }
                                  /*  if (responseJsonObj.has("Table2")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table2");
                                        if (widgetSystemName.equalsIgnoreCase("PerformanceAndIncentives")) {
                                            getAdvCurrentQuaterStatus(jsonArray);
                                        } else if (widgetSystemName.equalsIgnoreCase("OaoSalesOneup_TeamPerformanceAndIncentives")) {
                                            getTeamCurrentQuaterStatus(jsonArray);
                                        }

                                    }
                                    if (responseJsonObj.has("Table1")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table1");
                                        if (widgetSystemName.equalsIgnoreCase("PerformanceAndIncentives")) {
                                            advLoadChart(jsonArray);
                                        } else if (widgetSystemName.equalsIgnoreCase("OaoSalesOneup_TeamPerformanceAndIncentives")) {
                                            teamLoadChart(jsonArray);
                                        }

                                    }*/
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
                            //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
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
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }


           /* try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseS) {
                        //     super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                Gson mGson = new Gson();
                                Type type = new TypeToken<HashMap<String, Object>>() {
                                }.getType();
                                HashMap<String, Object> response = mGson.fromJson(responseS, type);
                                if (response.size() != 0) {
                                    //  String status = response.getString(STATUS);
                                    String status = response.get(STATUS).toString();
                                    if (status.equalsIgnoreCase("true")) {
                                        hideProgress();

                                        StringMap responseJSONObject = (StringMap) response.get("Response");


                                        if (responseJSONObject.containsKey("Table")) {
                                            List<StringMap> arrayDetail = (List<StringMap>) responseJSONObject.get("Table");
                                            // JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                            incentiveData(arrayDetail);
                                        }
                                        *//*if (responseJsonObj.has("Table1")) {
                                            JSONArray jsonArray = responseJsonObj.getJSONArray("Table1");
                                            // perfotmanceData(jsonArray);
                                            advLoadChart(jsonArray);
                                        }*//*


                                    } else {
                                        hideProgress();
                                        String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(new JSONObject(responseS));
                                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                    }
                                } else {

                                }

                            } else {

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        hideProgress();
                    }


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


                });
            } catch (Exception e) {
                e.printStackTrace();
            }*/


        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }

    }

  /*  private void WebServiceCall(final String widgetSystemName) {
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);

            //dashboard for
            inputMap.put("BusinessProfileId", businessProfileIdForDashboard);
            inputMap.put(PRODUCT_ID, productId);
            inputMap.put(EMPLOYEE_CODE, employeeCode);

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

                                    hideProgress();

                                    ArrayList<String> columnsYAxis = new ArrayList<String>();
                                    //  ArrayList<String> columnsYAxis = null;
                                    String total = "";
                                    String[] lableString = new String[]{};



                                    final ArrayList<ArrayList<String>> yAxisListFirst = new ArrayList<>();
                                    StringMap responseJsonObj = (StringMap) response.get("Response");
                                  *//*  if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String Table = (jsonObject.isNull("Table") || jsonObject.getString("Table").equals("")) ? "" : jsonObject.getString("Table");
                                        if(Table.equalsIgnoreCase("3")){}}*//*
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

    }*/

    private void currentQuarterStatusWebServiceCall(final String widgetSystemName) {
        // Timber.v(text);
        if (CRMAppUtil.isNetworkEnabled(getMainActivity())) {
            showProgress();
            String serviceMethod = "Dashboard/LoadWidgetAsync";
            Map inputMap = new HashMap<>();

            inputMap.put("AddtionalParameters", getAddtionalParameterMap());
            inputMap.put("WidgetSystemName", widgetSystemName);

            //dashboard for
            inputMap.put("BusinessProfileId", businessProfileIdForDashboard);
            inputMap.put(PRODUCT_ID, productId);
            inputMap.put(EMPLOYEE_CODE, employeeCode);


            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        //after success code goes here
                        try {
                            if (statusCode == 200) {
                                hideProgress();
                                String status = response.getString(STATUS);

                             /*   "Table1":[{"Name":" My Incentive","Q1":"20000","Q2":"40000","Q3":"15000","Q4":"25000"},
                                {"Name":" Branch Average","Q1":"23000","Q2":"12000","Q3":"30000","Q4":"25000"},
                                {"Name":" National Average","Q1":"20000","Q2":"40000","Q3":"25000","Q4":"30000"}]*/


                                if (!CRMAppUtil.isBlank(status) && status.equalsIgnoreCase("true")) {
                                    JSONObject responseJsonObj = response.getJSONObject("Response");


                                    if (responseJsonObj.has("Table")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String Table = (jsonObject.isNull("Table") || jsonObject.getString("Table").equals("")) ? "" : jsonObject.getString("Table");
                                        if (Table.equalsIgnoreCase("3")) {
                                            ArrayList<String> keyList = new ArrayList<String>();
                                            String headingName = (jsonObject.isNull("Heading") || jsonObject.getString("Heading").equals("")) ? "" : jsonObject.getString("Heading");
                                            Iterator iter = jsonObject.keys();
                                            while (iter.hasNext()) {
                                                String columnName = iter.next().toString();
                                                keyList.add(columnName);
                                            }

                                            final ArrayList<HashMap<String, String>> yAxisList = new ArrayList<HashMap<String, String>>();

                                            for (String keySetE : keyList) {
                                                HashMap<String, String> keyValue = new HashMap<>();
                                                if (keySetE.toString().equalsIgnoreCase("Heading") || keySetE.toString().equalsIgnoreCase("Table")) {
                                                } else {
                                                    String columnData = jsonObject.getString(keySetE).toString().equals("") ? "" : jsonObject.getString(keySetE).toString();
                                                    keyValue.put(keySetE.toString(), columnData);
                                                    yAxisList.add(keyValue);
                                                }

                                            }
                                            currentQuarterStatusTableThird(headingName, yAxisList);
                                        } else if (Table.equalsIgnoreCase("1")) {
                                            String headingName = (jsonObject.isNull("Heading") || jsonObject.getString("Heading").equals("")) ? "" : jsonObject.getString("Heading");
                                            currentQuarterStatusTableFirst(headingName, jsonArray);
                                        }
                                        // incentiveData(jsonArray);
                                    }
                                    if (responseJsonObj.has("Table1")) {
                                        JSONArray jsonArray = responseJsonObj.getJSONArray("Table1");
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String Table = (jsonObject.isNull("Table") || jsonObject.getString("Table").equals("")) ? "" : jsonObject.getString("Table");
                                        if (Table.equalsIgnoreCase("4")) {
                                            ArrayList<String> keyList = new ArrayList<String>();
                                            String headingName = (jsonObject.isNull("Heading") || jsonObject.getString("Heading").equals("")) ? "" : jsonObject.getString("Heading");
                                            Iterator iter = jsonObject.keys();
                                            while (iter.hasNext()) {
                                                String columnName = iter.next().toString();
                                                keyList.add(columnName);
                                            }

                                            final ArrayList<HashMap<String, String>> yAxisList = new ArrayList<HashMap<String, String>>();

                                            for (String keySetE : keyList) {
                                                HashMap<String, String> keyValue = new HashMap<>();
                                                if (keySetE.toString().equalsIgnoreCase("Heading") || keySetE.toString().equalsIgnoreCase("Table")) {
                                                } else {
                                                    String columnData = jsonObject.getString(keySetE).toString().equals("") ? "" : jsonObject.getString(keySetE).toString();
                                                    keyValue.put(keySetE.toString(), columnData);
                                                    yAxisList.add(keyValue);
                                                }

                                            }
                                            currentQuarterStatusTableFourth(headingName, yAxisList);
                                        } else if (Table.equalsIgnoreCase("2")) {
                                            String headingName = (jsonObject.isNull("Heading") || jsonObject.getString("Heading").equals("")) ? "" : jsonObject.getString("Heading");
                                            currentQuarterStatusTableSecond(headingName, jsonArray);
                                        }
                                        // incentiveData(jsonArray);
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
                            //   MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
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
                MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
//            customProgress.setVisibility(View.GONE);
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

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
                    String paramValue = jsonRowObject.getString(paramName).equalsIgnoreCase("null") ? "" : jsonRowObject.getString(paramName);
                    if (!paramName.equalsIgnoreCase("Heading") && !paramName.equalsIgnoreCase("Table")) {
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
            headingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
                    headingCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                List<Map> rowList = tableMap.get(column1Heading);
                for (Map map : rowList) {
                    String header = map.keySet().iterator().next().toString();
                    String value = map.values().iterator().next().toString();

                    TextView textView = new TextView(getActivity());
                    tableRow.addView(textView);
                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
                    String paramValue = jsonRowObject.getString(paramName).equalsIgnoreCase("null") ? "" : jsonRowObject.getString(paramName);
                    if (!paramName.equalsIgnoreCase("Heading") && !paramName.equalsIgnoreCase("Table")) {
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
            headingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
                    headingCell.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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
                tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                List<Map> rowList = tableMap.get(column1Heading);
                for (Map map : rowList) {
                    String header = map.keySet().iterator().next().toString();
                    String value = map.values().iterator().next().toString();

                    TextView textView = new TextView(getActivity());
                    tableRow.addView(textView);
                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
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

    private void currentQuarterStatusTableThird(String headingName, ArrayList<HashMap<String, String>> arrayList) {
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

    private void currentQuarterStatusTableFourth(String headingName, ArrayList<HashMap<String, String>> arrayList) {
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


    private CharSequence getTableHead(CharSequence text) {
        try {
            if (text.charAt(0) == 'Q' || text.charAt(0) == 'q') {
                return "Quater " + text.subSequence(1, text.length());
            } else {
                return text;
            }
        } catch (IndexOutOfBoundsException oe) {
            return text;
        }
    }

   /* private void incentiveData(List<StringMap> arrayDetail) {
        arrayListIncentive.clear();
        for (int i = 0; i < arrayDetail.size(); i++) {
            HashMap<String, String> fieldData = new HashMap<String, String>();
            StringMap objDetail = arrayDetail.get(i);
            // JSONObject jsonObject = jsonArray.getJSONObject(i);
            String Parameters = (objDetail.get("Parameters") == null || objDetail.get("Parameters").toString().equals("")) ? "" : objDetail.get("Parameters").toString();
            String q1 = (objDetail.get("Q1") == null || objDetail.get("Q1").toString().equals("")) ? "" : objDetail.get("Q1").toString();
            String q2 = (objDetail.get("Q2") == null || objDetail.get("Q2").toString().equals("")) ? "" : objDetail.get("Q2").toString();
            String q3 = (objDetail.get("Q3") == null || objDetail.get("Q3").toString().equals("")) ? "" : objDetail.get("Q3").toString();
            String q4 = (objDetail.get("Q4") == null || objDetail.get("Q4").toString().equals("")) ? "" : objDetail.get("Q4").toString();
            String yTD = (objDetail.get("YTD") == null || objDetail.get("YTD").toString().equals("")) ? "" : objDetail.get("YTD").toString();
          *//*  String q1 = (jsonObject.isNull("Q1") || jsonObject.getString("Q1").equals("")) ? "0" : jsonObject.getString("Q1");
            String q2 = (jsonObject.isNull("Q2") || jsonObject.getString("Q2").equals("")) ? "0" : jsonObject.getString("Q2");
            String q3 = (jsonObject.isNull("Q3") || jsonObject.getString("Q3").equals("")) ? "0" : jsonObject.getString("Q3");
            String q4 = (jsonObject.isNull("Q4") || jsonObject.getString("Q4").equals("")) ? "0" : jsonObject.getString("Q4");
            String yTD = (jsonObject.isNull("YTD") || jsonObject.getString("YTD").equals("")) ? "0" : jsonObject.getString("YTD");*//*
            fieldData.put(PARAMETERS, Parameters);
            fieldData.put(Q1, q1);
            fieldData.put(Q2, q2);
            fieldData.put(Q3, q3);
            fieldData.put(Q4, q4);
            fieldData.put(YTD, yTD);
            arrayListIncentive.add(fieldData);
        }
        incentiveView("Q1");


    }*/

    private void incentiveData(JSONArray jsonArray) {
        arrayListIncentive.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap<String, String> fieldData = new HashMap<String, String>();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String Parameters = (jsonObject.isNull("Parameters") || jsonObject.getString("Parameters").equals("")) ? "" : jsonObject.getString("Parameters");
                String q1 = (jsonObject.isNull("Q1") || jsonObject.getString("Q1").equals("")) ? "0" : jsonObject.getString("Q1");
                String q2 = (jsonObject.isNull("Q2") || jsonObject.getString("Q2").equals("")) ? "0" : jsonObject.getString("Q2");
                String q3 = (jsonObject.isNull("Q3") || jsonObject.getString("Q3").equals("")) ? "0" : jsonObject.getString("Q3");
                String q4 = (jsonObject.isNull("Q4") || jsonObject.getString("Q4").equals("")) ? "0" : jsonObject.getString("Q4");
                String yTD = (jsonObject.isNull("YTD") || jsonObject.getString("YTD").equals("")) ? "0" : jsonObject.getString("YTD");
                fieldData.put(PARAMETERS, Parameters);
                fieldData.put(Q1, q1.equalsIgnoreCase("0.0") ? "0" : q1);
                fieldData.put(Q2, q2.equalsIgnoreCase("0.0") ? "0" : q2);
                fieldData.put(Q3, q3.equalsIgnoreCase("0.0") ? "0" : q3);
                fieldData.put(Q4, q4.equalsIgnoreCase("0.0") ? "0" : q4);
                fieldData.put(YTD, yTD.equalsIgnoreCase("0.0") ? "0" : yTD);
                arrayListIncentive.add(fieldData);
            }
            incentiveView("Q1");
        } catch (JSONException je) {
            je.printStackTrace();
        }


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
                //Are normal row dat do alternate back ground
           /* if (i % 2 == 0) {
                tdParm.setBackgroundResource(R.drawable.list_alice_blue_background);
                tdVata.setBackgroundResource(R.drawable.list_white_background);

            } else {
                tdParm.setBackgroundResource(R.drawable.list_white_background);
                tdVata.setBackgroundResource(R.drawable.list_alice_blue_background);

            }*/


                tableMData.addView(tableRow);
            }
        }
        llIncentive.setVisibility(View.VISIBLE);
        tvIncentives.setText("Incentive");
    }
    //incentiveData end

    //start region hi
  /*  public void openWebUrl(Uri url) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            String packag = CustomTabsHelper.getPackageNameToUse(getContext());
            if (packag == null) {
                //   openWebview(null, url);
               *//* Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra(URL_PATH, url.toString());
                startActivity(intent);*//*
            } else {
                openCustomTabsIntent(url.toString());
            }
        } else {
            openInBrowser(url);
        }
    }


    private void openCustomTabsIntent(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(getResources().getColor(R.color.secondaryBackground));
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_calendar));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
    }

    private CustomTabsSession getSession() {
        return mClient.newSession(new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(int navigationEvent, Bundle extras) {
                super.onNavigationEvent(navigationEvent, extras);
            }
        });
    }

    public void openInBrowser(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(intent);
        }
    }*/
    //end region
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //   super.onSaveInstanceState(outState);
    }

    private void createFloatingButtons() {
        /**
         <com.getbase.floatingactionbutton.FloatingActionButton
         android:id="@+id/leadcreation"
         android:layout_width="wrap_content"
         android:layout_height="wrap_content"
         app:fab_icon="@drawable/ic_create_lead"
         fab:fab_colorNormal="#ffffff"
         fab:fab_colorPressed="#ffffff"
         fab:fab_size="mini"
         fab:fab_title="Create Lead" />
         */

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

                        List<WidgetModel> widgetModelList = currentDashboard.getGroupModel(ACTIONABLE).getWidgetList();
                        if (advisoryActionableInsightAdapter == null) {
                            advisoryActionableInsightAdapter = new AdvisoryActionableInsightAdapter(getActivity(), widgetModelList);
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

    public void webServiceInceniveSlab(String dashboardId) {
        //login parameters

        if (CRMAppUtil.isNetworkEnabled(getActivity())) {
            showProgress();

            Map inputMap = new HashMap();
            inputMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
            inputMap.put("BusinessProfileId", businessProfileIdForDashboard);
            inputMap.put("DashboardId", dashboardId);


            String serviceMethod = "Dashboard/GetIncentivePolicy";


            try {
                CRMWebServiceHandler.post(getMainActivity(), serviceMethod, getHeader(), inputMap, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        if (statusCode == 200) {
                            try {
                                String status = response.getString(STATUS);
                                hideProgress();
                                if (!isBlank(status) && status.equalsIgnoreCase("true")) {

                                    JSONObject obj = response.getJSONObject(RESPONSE);
                                    String filePath = obj.isNull("FilePath") || obj.get("FilePath").equals("") ? "" : obj.getString("FilePath");
                                    if (filePath.isEmpty()) {
                                        alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), MISSING_FILE, "OK", true, null);
                                    } else {
                                        new DownloadFileFromURL().execute(filePath.replaceAll(" ", "%20"));
                                    }

                                   /* alertDialog(getActivity(), 0, getResources().getString(R.string.motilal_oswal), DOCUMENT_DELETED, "OK", true, new MSFDialog.DialogListener() {
                                        @Override
                                        public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                            if (action == MSFDialog.Action.OK) {

                                            }
                                        }
                                    });*/

                                } else {
                                    String errorMsg = CRMMOSLCommon.getErrorMsgFromResponse(response);
                                    //   alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), errorMsg, "OK", true, null);
                                }
                            } catch (Exception ex) {
                                hideProgress();
                                ex.printStackTrace();
                                //  alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                            }
                        } else {
                            hideProgress();
                            //  alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
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
                                String error = CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                if (CRMAppUtil.isBlank(error)) {
                                    error = SOME_SERVER_ERROR_OCCURRED;
                                }
                                //  MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), error, "OK", true, null);
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            // MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        //  alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        // alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }

        } else {
            hideProgress();
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true, null);

        }
    }

    ////////////////////
    // Progress Dialog
    private ProgressDialog pDialog;

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
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), MISSING_FILE_VIEWER + localFilePath.getAbsolutePath(), "OK", true, null);
        } catch (Exception ex) {
            MSFDialog.alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
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

                OutputStream output = new FileOutputStream(localFile.getAbsolutePath()); //Environment.getExternalStorageDirectory().toString()+"/"+fileName);

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

                // flushing output
                output.flush();

                // closing streams
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

                /* alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), FILE_DOWNLOADED + localFile.getAbsolutePath(), "Show", "Close", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {//showfile
                            showDownloadFile(localFile);
                        }
                    }
                });*/
                showDownloadFile(localFile);
            } else {
                alertDialog(getMainActivity(), 0, getResources().getString(R.string.motilal_oswal), SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
            }

        }

    }



}
