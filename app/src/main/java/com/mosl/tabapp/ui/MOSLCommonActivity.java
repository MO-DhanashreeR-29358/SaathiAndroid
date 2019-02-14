package com.mosl.tabapp.ui;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.mosl.mobile.ekyc.common.MOSLCommon;
import com.mosl.mobile.ekyc.model.RoleSelectionModel;
import com.mosl.mobile.ekyc.model.RowData;
import com.mosl.mobile.ekyc.register.RegisterActivity;
import com.mosl.mobile.ekyc.service.WebServiceHandler;
import com.mosl.mobile.ekyc.ui.BranchResumeApplicationActivity;
import com.mosl.mobile.ekyc.ui.CustomDialog;
import com.mosl.mobile.ekyc.ui.PostLoginActivity;
import com.mosl.tabapp.R;
import com.mosl.tabapp.actionbar.CRMAppActionBar;
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryFragment;
import com.mosl.tabapp.calendar.CalendarFragment;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.CRMMOSLCommon;
import com.mosl.tabapp.common.CRMSecurityUtil;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.common.NetworkStateReceiver;
import com.mosl.tabapp.constants.MOSLConstants;
import com.mosl.tabapp.dashboard.DashboardFragment;
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment;
import com.mosl.tabapp.leadViewDetail.LeadViewMainFragment;
import com.mosl.tabapp.leadViewDetail.SetAppointmentFragment;
import com.mosl.tabapp.leadcreation.LeadCreationFragment;
import com.mosl.tabapp.menu.DrawerMenu;
import com.mosl.tabapp.menu.MenuAdapter;
import com.mosl.tabapp.menu.MenuGetter;
import com.mosl.tabapp.menu.ScreenDetails;
import com.mosl.tabapp.menu.SubMenu;
import com.mosl.tabapp.model.AccountDetails;
import com.mosl.tabapp.model.NotificationData;
import com.mosl.tabapp.model.SearchResultModel;
import com.mosl.tabapp.notification.NotificationFragment;
import com.mosl.tabapp.pendingApproval.PendingApprovalFragment;
import com.mosl.tabapp.service.DataSyncWorker;
import com.mosl.tabapp.service.ForegroundService;
import com.mosl.tabapp.smt.SMTFragment;
import com.mosl.tabapp.ui.quickdialog.ActionItem;
import com.mosl.tabapp.ui.quickdialog.QuickAction;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.mosl.tabapp.ws.WebServiceDirectCall;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import cz.msebera.android.httpclient.Header;

import static com.mosl.mobile.ekyc.common.MOSLCommon.commonMsgDialog;
import static com.mosl.mobile.ekyc.common.MOSLCommon.getAppVersionCode;
import static com.mosl.mobile.ekyc.common.MOSLCommon.isBlank;
import static com.mosl.mobile.ekyc.common.MOSLCommon.loadCountryArray;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.APP_LOGIN_ID;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.BINDCOUNTRY;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.BOBRANCH;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.BOSUBBROKER;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.BOTRADERS;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRM;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.CRMBRANCH;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.GETCRMROLEDETAILS;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.LOGIN;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.LOGINUSERID;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.OK;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.ROLEID;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.ROLEMAPPINGID;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.ROLE_MAPPING_ID;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.SELECT_ROLE;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.SIGNUP;
import static com.mosl.mobile.ekyc.constants.MOSLConstants.TEAMID;
import static com.mosl.mobile.ekyc.model.CommonModel.getAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppLoginID;
import static com.mosl.mobile.ekyc.model.CommonModel.setAppType;
import static com.mosl.mobile.ekyc.model.RoleSelectionModel.roleID;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;
import static com.mosl.tabapp.ui.MSFDialog.alertDialog;

public class MOSLCommonActivity extends AppCompatActivity
        implements MOSLConstants, View.OnClickListener, QuickAction.OnActionItemClickListener {
    private static final int ID_LEAD = 1;
    private static final int ID_CLEINT = 2;
    private static final int ID_EMP = 3;
    private static final String SEARCH_TYPE_LEAD = "Lead";
    private static final String SEARCH_TYPE_CLIENT = "Client";
    private static final String SEARCH_TYPE_EMP = "Employee";
    private final HashMap<String, String> globalTitles = new HashMap<>();
    private final Bundle args = new Bundle();
    Boolean value = false;
    private DrawerLayout drawerLayout;
    //setting default
    private String searchType = SEARCH_TYPE_LEAD;
    private String ekycMenuType = SEARCH_TYPE_LEAD;
    private ArrayList<RowData> arrRoleSel;
    private ArrayAdapter<RowData> arrAdapRoleSel;
    private Spinner spRoleSel;
    private RoleSelectionModel roleSelectionModel;

    private boolean mBound = false;
    private ForegroundService mService = null;
    UUID compressionWorkId;
    private WorkManager mWorkManager;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ForegroundService.LocalBinder binder = (ForegroundService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private final View.OnClickListener menuslide = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            hideKeyboard(v);
            openMenuDrawer();
        }
    };
    private ExpandableListView menuList;
    private CRMAppActionBar actionBar;
    private RelativeLayout progressLayout;
    private MenuAdapter menuAdapter;
    private int lastExpandedPosition = -1;
    private String currentPage = "";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    boolean prospect;
    static TextView tvRank;
    static TextView tvPartnerName, tvBusinessProfileName;
    //network monitor
    private NetworkStateReceiver networkStateReceiver;
    private IntentFilter intentFilter;
    private AutoCompleteAdapter autoCompleteAdapter;
    private SetAppointmentFragment setAppointmentFragment;
    private CreateMeetingFragment createMeetingFragment;
    private LeadViewMainFragment leadViewMainFragment;
    //static boolean filterLoadDone = true;
    static ImageView ivPartnerImage, ivCloseImage;
    private boolean searchFoundFlag;
    private boolean gotNotificationIntent = false;

    //Ekyc Country
    private static ArrayList<String> array = new ArrayList();
    private static ArrayList<String> fatcaArray = new ArrayList<>();

    private void loginAndInitView() {
        gotNotificationIntent = true;
        //TODO handlel [active,background,killed]
        if (AccountDetails.getToken() == null || AccountDetails.getToken().length() <= 0) {
            //Case when app is killed
            handleStartupPage();
        } else {
            //Case active,background
            initView();
        }
    }

    private AdapterView.OnItemSelectedListener roleListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    roleID = ((RowData) spRoleSel.getSelectedItem()).getKey();
                    getRoleDetails(roleID);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            actionBar.updateMsgCount(1);
        }
    };

    public String data(String key) {
        return key;
    }

    public void setAppTitle(String key, String title) {
        if (key.equals(currentPage)) {
            globalTitles.put(key, title);
            actionBar.setTitle(title);
            actionBar.setTitleVisibility(View.VISIBLE);
        } else {
            currentPage = key;
            globalTitles.put(key, title);
            actionBar.setTitle(title);
            actionBar.setTitleVisibility(View.VISIBLE);
        }

        if (key.contains("Margin") || key.contains("Old Margin Summary")) {
            value = false;
        }
    }

    public void hideAppTitle(String key) {
        if (actionBar != null) {
            actionBar.setTitleVisibility(View.GONE);
        }
    }

    public static Map getHeader(Context context) {
        Map headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=UTF-8");
        headerMap.put("token", AccountDetails.getToken());
        headerMap.put("userid", AccountDetails.getUserId());
        headerMap.put("clientinfo", "android");
        headerMap.put("ipaddress", CRMAppUtil.getLocalIpAddress());
        headerMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
        headerMap.put("macaddress",
                CRMMOSLCommon.getDeviceID(context, context.getContentResolver()));
        headerMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
        headerMap.put("AppId", "2");
        return headerMap;
    }

    //notification
    public static void sendRegistrationToServer(Context context, ContentResolver resolver,
                                                boolean isLogout) throws Exception {
        boolean isAuthenticated = getPrefs(context).getBoolean("AUTHENTICATED", false);

        if (isLogout) {
            AccountDetails.setUserId("");
            sendToken(context, resolver);
        } else {
            sendToken(context, resolver);
        }
    }

    /**
     * This is use to send fcm push notification token on first run
     */

    private static void sendToken(Context context, ContentResolver resolver) throws Exception {
        String token = getPrefs(context).getString("TOKEN_ID", null);

        String deviceId = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);

        Map requestMap = new HashMap();
        Map headerMap = getHeader(context);
        String serviceMethod = "Auth/UpdateFCMToken";
        requestMap.put("DeviceToken", token);
        requestMap.put("DeviceId", deviceId);

        CRMWebServiceHandler.post(context, serviceMethod, headerMap, requestMap,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("Send Token Response: ", response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                          JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                                          Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.e("Failure", responseString);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isBlank(getPrefs(this).getString("PASSWORD", ""))) {
            Intent intentLogin = new Intent(MOSLCommonActivity.this, LoginActivity.class);
            startActivity(intentLogin);
            finish();
        }
        setContentView(R.layout.crm_activity_main);
        mWorkManager = WorkManager.getInstance();
        startWorkManager();
        startService(new Intent(this, ForegroundService.class));
        //region Get intent
        Intent intent = getIntent();

        //Replace here
        actionBar = (CRMAppActionBar) findViewById(R.id.actionBarCommon);
        if (intent != null) {
            Bundle bundel = intent.getExtras();
            if (bundel != null && !bundel.isEmpty()) {
                String source = bundel.getString(SOURCE, "");

                if (source.equalsIgnoreCase(FCM_NOTIFICATION_SOURCE)) {
                    actionBar.updateMsgCount(-1);
                    loginAndInitView();
                } else {
                    loginAndInitView();
                }
            } else {
                loginAndInitView();
            }
        } else {
            initView();
        }
    }

    /**
     * this Method does Init of activity
     */
    private void initView() {
        setupMenuDrawer();
        progressLayout = (RelativeLayout) findViewById(R.id.customProgress);
        actionBar.setHomeAction(menuslide);

        networkStateReceiver = new NetworkStateReceiver();
        intentFilter = new IntentFilter("mobile.mosl.com.crm.common.SOME_ACTION");

        Intent intent = new Intent("mobile.mosl.com.crm.common.SOME_ACTION");
        sendBroadcast(intent);

        setupDefaultFragment(getIntent());
        autoCompleteAdapter = new AutoCompleteAdapter(getApplicationContext());
        actionBar.setAdapter(autoCompleteAdapter);
        actionBar.onClickSearch(this);
        actionBar.showNotification(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNotificationVisible()) {
                    NotificationFragment frag = new NotificationFragment();
                    addFragment(R.id.activityFrameLayout, frag, true, false);
                    //clearing badge
                    actionBar.updateCnt(0);
                }
            }
        });

    }

    public void showProgress() {
        if (progressLayout != null) progressLayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
    }

    public boolean isDashBoardVisible() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    if (fragment instanceof DashboardFragment) return true;
                }
            }
        }
        return false;
    }

    private void getLastTitle(String currentPage) {
        String title = globalTitles.get(currentPage);
        if (title != null && !title.isEmpty()) {
            actionBar.setTitle(title);
            actionBar.setTitleVisibility(View.VISIBLE);
        } else {
            hideAppTitle(currentPage);
        }
    }

    public void hideAppTitle() {
        actionBar.setTitle("");
        actionBar.setTitleVisibility(View.GONE);
    }

    public boolean isNotificationVisible() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible()) {
                    if (fragment instanceof NotificationFragment) return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d("Called", "on Back Press Also" + keyCode);
            value = false;
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }

            if (progressLayout.getVisibility() == View.VISIBLE) {
                progressLayout.setVisibility(View.GONE);
                return false;
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count == 0) {
                if (isDashBoardVisible()) {
                    //Show back
                    MSFDialog.alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
                            "Do you want to exit the application?", "Yes", "No", true,
                            new MSFDialog.DialogListener() {

                                @Override
                                public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                    if (action == MSFDialog.Action.OK) {
                                        finish();
                                    }
                                }
                            });
                } else {
                    Intent intent = new Intent(MOSLCommonActivity.this, MOSLCommonActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            } else {
                goBackOnce();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clearTitles() {
        globalTitles.clear();

    }

    private void openMenuDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void setupDefaultFragment(Intent data) {
        int isProceed = data.getIntExtra("isProceed", 0);

        String source = data.getStringExtra(SOURCE);
        String strAction = data.getAction();

        if (source != null && source.equalsIgnoreCase(FCM_NOTIFICATION_SOURCE)) {
            String nData = getIntent().getExtras().getString(FCM_NOTIFICATION_CATEGORY, "");

            navigateToFromNotification(nData);
            setIntent(new Intent());
        } else if (strAction != null) {
            if (strAction.trim().equalsIgnoreCase("com.mosl.tabapp.ADD_QUERY")) {
                setupFragment(new QueryFragment());
            } else if (strAction.trim()
                    .equalsIgnoreCase("com.mosl.tabapp.VIEW_CALE")) {//goto callendar
                setupFragment(new CalendarFragment());
            } else if (strAction.trim()
                    .equalsIgnoreCase("com.mosl.tabapp.VIEW_BA")) {//goto birthday
                setupFragment(new BirthdayAnniversaryFragment());
            } else if (strAction.trim()
                    .equalsIgnoreCase("com.mosl.tabapp.CREATE_LEAD")) {//create lead
                setupFragment(new LeadCreationFragment());
            } else {
                setupFragment(new DashboardFragment());
            }
        } else {
            setupFragment(new DashboardFragment());

        }
    }

    public void setupFragment(MOSLCommonFragment fragment) {
        addFragment(R.id.activityFrameLayout, fragment, false, getSupportFragmentManager(), true);
    }

    private void performMenuAction(String details) {
        clearTitles();
        MOSLCommonFragment fragment = (MOSLCommonFragment) MenuGetter.getFragment(details);
        if (fragment != null) {
            setupFragment(fragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.srchImg:
                //Search image on click
                openSearchOptions(v);
                hideKeyboard(v);
                break;
        }
    }

    public void goBackOnce() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        MOSLCommonFragment currentFragment;
        FragmentManager.BackStackEntry backEntry =
                fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
        String tag = backEntry.getName();
        MOSLCommonFragment closingFragment =
                (MOSLCommonFragment) fragmentManager.findFragmentByTag(tag);
        closingFragment.onFragmentPause();
        fragmentManager.popBackStackImmediate();

        if (fragmentManager.getBackStackEntryCount() != 0) {
            backEntry =
                    fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            tag = backEntry.getName();
            currentFragment = (MOSLCommonFragment) fragmentManager.findFragmentByTag(tag);
        } else {
            currentFragment =
                    (MOSLCommonFragment) fragmentManager.findFragmentById(R.id.activityFrameLayout);
            if (currentFragment instanceof PendingApprovalFragment) {

                ((PendingApprovalFragment) currentFragment).getAprovalCoutSummary();
                ((PendingApprovalFragment) currentFragment).getAprovalList();
            }
        }
        getLastTitle(currentFragment.getClass().toString());
    }

    public void addFragment(int containerViewId, Fragment fragment, boolean addStack,
                            FragmentManager fragmentManager, boolean isReplace) {
        addFragment(containerViewId, fragment, addStack, isReplace);
    }


    public void addFragment(int containerViewId, Fragment fragment, boolean addStack,
                            boolean isReplace) {
        if (fragment == null) {
            Log.e("MOSLCommonActivity", "Fragment is null.");
            return;
        }

        if (!addStack) clearTitles();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String tag = fragment.getClass().toString();

        if (fragmentManager.getBackStackEntryCount() != 0) {
            FragmentManager.BackStackEntry backEntry =
                    fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            MOSLCommonFragment currentFragment =
                    (MOSLCommonFragment) fragmentManager.findFragmentByTag(backEntry.getName());

            currentFragment.onFragmentPause();
        } else {
            MOSLCommonFragment currentFragment =
                    (MOSLCommonFragment) fragmentManager.findFragmentById(containerViewId);
            if (currentFragment != null) currentFragment.onFragmentPause();
        }

        if (!addStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.executePendingTransactions();
        }

        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {

            if (addStack) {
                fragmentTransaction.add(containerViewId, fragment, tag);
            } else {
                fragmentTransaction.replace(containerViewId, fragment, tag);
            }
        }

        if (addStack) {
            fragmentTransaction.addToBackStack(tag);
        } else {

        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        if (!addStack) ((MOSLCommonFragment) fragment).onFragmentResume();
    }

    private void setupMenuDrawer() {
        menuList = (ExpandableListView) findViewById(R.id.homeGridView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLay);
        LinearLayout pageHeaderLayout = (LinearLayout) findViewById(R.id.pageHeaderLayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        pageHeaderLayout.setOnClickListener(null);
        tvPartnerName = (TextView) findViewById(R.id.tvPartnerName);
        tvBusinessProfileName = (TextView) findViewById(R.id.tvBusinessProfileName);
        ivPartnerImage = (ImageView) findViewById(R.id.ivPartnerImage);
        ivCloseImage = (ImageView) findViewById(R.id.ivCloseImage);
        ivCloseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                drawerLayout.closeDrawers();
            }
        });
        Glide.with(this)
                .load(AccountDetails.getImageUrl().toLowerCase())
                .asBitmap()
                .placeholder(R.drawable.menu_profile_pic)
                .centerCrop()
                .into(new BitmapImageViewTarget(ivPartnerImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivPartnerImage.setImageDrawable(circularBitmapDrawable);
                    }
                });
        String name = AccountDetails.getFirstName() + " " + AccountDetails.getLastName();
        if (!CRMAppUtil.isBlank(AccountDetails.getEmployeeCode())) {
            tvBusinessProfileName.setText(AccountDetails.getBusinessProfileName());
        }
        tvPartnerName.setText(name);

        setMenuController();
    }

    private void setMenuController() {
        final List<ScreenDetails> groupList = DrawerMenu.createGroupList(this);
        final Map<String, Vector<SubMenu>> subMenuCollections =
                DrawerMenu.createCollection(this, groupList);
        menuAdapter = new MenuAdapter(this, groupList, subMenuCollections);
        menuList.setAdapter(menuAdapter);
        menuList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    menuList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
        menuList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, View view,
                                        final int position, final int childPosition, long id) {
                value = false;
                drawerLayout.closeDrawers();
                final boolean[] setMenuSelection = {false};
                String clickedPosDetails = "";
                Vector<SubMenu> sub = subMenuCollections.get(groupList.get(position).getTitle());
                if (sub.size() > 0) {
                    clickedPosDetails = sub.get(childPosition).getDetails();
                }
                menuAdapter.setChildMenuSelection(lastExpandedPosition, childPosition);
                if (position == MOSL_MENU_DASHBOARD) {
                        setMenuSelection[0] = true;
                        performMenuAction(clickedPosDetails);
                    } else if (position == MOSL_MENU_EKYC) {
                        if (childPosition == MOSL_MENU_FRESH_REGISTRATION) {
                            if (AccountDetails.isNewEkyc()) {
                                Map map = new HashMap();
                                map.put(NEW_USERID, "");
                                map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                                map.put(COMMANDTYPE, "NEW");
                                map.put(APPLICATIONTYPE, "CRM");
                                map.put(OSTYPE, "ANDROID");
                                map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                                map.put(TOKENKEY, AccountDetails.getToken());
                                newEkyc(getInputBody(map));
                            } else {
                                ekycVersionCheck(MOSL_MENU_FRESH_REGISTRATION, new HashMap());
                            }
                        } else if (childPosition == MOSL_MENU_EXISTINGS_APPLICATIONS) {
                            if (AccountDetails.isNewEkyc()) {
                                Map map = new HashMap();
                                map.put(NEW_USERID, "");
                                map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                                map.put(COMMANDTYPE, "RESUME");
                                map.put(APPLICATIONTYPE, "CRM");
                                map.put(OSTYPE, "ANDROID");
                                map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                                map.put(TOKENKEY, AccountDetails.getToken());
                                newEkyc(getInputBody(map));
                            } else {
                                ekycVersionCheck(MOSL_MENU_EXISTINGS_APPLICATIONS, new HashMap());
                            }
                        }
                }

                if (!AccountDetails.isAufsb()) {
                    if (position == MOSL_MENU_MY_REPORTS) {
                        ReportFragment reportFragment = new ReportFragment();
                        Bundle bund = new Bundle();
                        bund.putString(SYSTEM_NAME,
                                SplashActivity.menuReportArrayList.get(childPosition).getSystemsName());
                        bund.putString(NAME,
                                SplashActivity.menuReportArrayList.get(childPosition).getMenuName());
                        reportFragment.setArguments(bund);
                        setupFragment(reportFragment);
                    }
                }

                return false;
            }
        });

        menuList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view,
                                        int position, long l) {
                boolean clickHandled = false;
                value = false;
                if (AccountDetails.isAufsb()) {
                    if (position == MOSL_MENU_EKYC) {
                        clickHandled = false;
                    } else {
                        drawerLayout.closeDrawers();
                        menuAdapter.setGroupMenuSelection(position);
                        if (position == MOSL_MENU_DASHBOARD) {
                            searchType = SEARCH_TYPE_LEAD;
                            actionBar.setSearchHint(SEARCH_TYPE_LEAD_HINT);
                            setupFragment(new DashboardFragment());
                        } else if (position == 2) {
                            logoutCustomer();
                        }

                    }
                } else {
                    if (position == MOSL_MENU_EKYC || position == MOSL_MENU_MY_REPORTS) {
                        clickHandled = false;
                    } else {
                        drawerLayout.closeDrawers();
                        menuAdapter.setGroupMenuSelection(position);

                        if (position == MOSL_MENU_DASHBOARD) {
                            searchType = SEARCH_TYPE_LEAD;
                            actionBar.setSearchHint(SEARCH_TYPE_LEAD_HINT);
                            setupFragment(new DashboardFragment());
                        } else if (position == MOSL_MENU_CALENDAR) {
                            setupFragment(new CalendarFragment());
                        } else if (position == MOSL_MENU_PENDING_APPROVALS) {
                            setupFragment(new PendingApprovalFragment());
                        } else if (position == MOSL_MENU_KNOWLEDGE_CENTRE) {
                            WebViewFragment fragment = new WebViewFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString(URL_PATH, CRMWebServiceHandler.COLLATERAL_URL);
                            fragment.setArguments(bundle);
                            addFragment(R.id.activityFrameLayout, fragment, true, false);
                        } else if (position == MOSL_MENU_BIRTHDAY_ANIVERSARY) {
                            setupFragment(new BirthdayAnniversaryFragment());
                        }
                        if (AccountDetails.isSmtEnabled()) {
                            if (position == MOSL_MENU_SUGGEST_ME) {
                                getSuggestMe();
                            } else if (position == MOSL_MENU_QUERY) {
                                setupFragment(new QueryFragment());
                            } else if (position == MOSL_MENU_LOGOUT) {
                                logoutCustomer();
                            }
                        } else {
                            if (position == 7) {
                                setupFragment(new QueryFragment());
                            } else if (position == 8) {
                                logoutCustomer();
                            }
                        }
                    }

                }

                return clickHandled;
            }
        });
    }


    protected void onDestroy() {
        super.onDestroy();
    }

    private void logoutCustomer() {
        MSFDialog.alertDialog(MOSLCommonActivity.this, LOGOUT_CODE,
                getString(R.string.motilal_oswal), getString(R.string.MOSL_LOGOUT_ALERT_MSG), "Yes",
                "No", true, new MSFDialog.DialogListener() {
                    @Override
                    public void alertDialogAction(MSFDialog.Action action, Object... data) {
                        if (action == MSFDialog.Action.OK) {
                            doLogout(LOGOUT_CODE);
                        }
                    }
                });
    }

    public void doLogout(int code) {
        //LruCacheManager.getInstance().clearCache();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        getPrefs(this).edit().putString("PASSWORD", null).apply();
        getPrefs(this).edit().putString("EmployeeCode", null).apply();
        getPrefs(this).edit().putString("token", null).apply();
        getPrefs(this).edit().putString("userid", null).apply();
        getPrefs(this).edit().putString("ipaddress", null).apply();
        getPrefs(this).edit().putString("BusinessUnitId", null).apply();
        getPrefs(this).edit().putString("macaddress", null).apply();
        closeActivity(code);
    }

    private void closeActivity(int code) {
        setResult(code);
        if (CRMAppUtil.isNetworkEnabled(MOSLCommonActivity.this)) {
            //TODO call progress bar
            Map inputMap = new HashMap<>();
            inputMap.put("id", AccountDetails.getUserId());
            inputMap.put("token", AccountDetails.getToken());
            inputMap.put("clientinfo", "android");
            inputMap.put("ipaddress", CRMAppUtil.getLocalIpAddress());
            inputMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
            inputMap.put("macaddress", CRMMOSLCommon.getDeviceID(this, this.getContentResolver()));

            String serviceMethod = "/Auth/Logout";

            showProgress();
            try {
                CRMWebServiceHandler.post(MOSLCommonActivity.this, serviceMethod,
                        getHeader(getApplicationContext()), inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        hideProgress();
                                        if (status.equalsIgnoreCase("true")) {
                                            stopService(new Intent(MOSLCommonActivity.this,
                                                    ForegroundService.class));
                                            mWorkManager.cancelWorkById(compressionWorkId);
                                            Intent i = new Intent(MOSLCommonActivity.this,
                                                    ImageSliderActivity.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            String message = response.getString(MESSAGE);
                                            MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                    getResources().getString(R.string.motilal_oswal),
                                                    message, "Ok", true, null);
                                        }
                                    } else {
                                        hideProgress();
                                    }
                                    hideProgress();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgress();
                                    MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideProgress();
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideProgress();
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString,
                                                  Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                hideProgress();
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();

                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                hideProgress();
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                    getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                    null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void ConvertEkycApi(final String mobile, final String email,
                                final String branchEmplyeeCode, final String SourceName, final String ProspectSourceName,
                                final String ProspectSourceDetails, final String InterestedIn) {
        if (CRMAppUtil.isNetworkEnabled(MOSLCommonActivity.this)) {
            showProgress();
            final Map inputMap = new HashMap<>();

            inputMap.put("Mobile", mobile);
            inputMap.put("Email", email);
            inputMap.put("BranchRm_EmpCode", branchEmplyeeCode);
            inputMap.put("SourceName", SourceName);
            inputMap.put("ProspectSourceName", ProspectSourceName);
            inputMap.put("ProspectSourceDetails", ProspectSourceDetails);
            inputMap.put("InterestedIn", InterestedIn);
            String serviceMethod = "Prospect/RegisterProspectOnline";

            try {
                CRMWebServiceHandler.post(MOSLCommonActivity.this, serviceMethod,
                        getHeader(MOSLCommonActivity.this), inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                //after success code goes here
                                try {

                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        String message = response.getString(MESSAGE);
                                        if (status.equalsIgnoreCase("true")) {
                                            hideProgress();
                                            JSONArray jsonArray = response.getJSONArray("Response");
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String RegistrationAllowed =
                                                    (jsonObject.isNull("RegistrationAllowed")
                                                            || jsonObject.getString("RegistrationAllowed")
                                                            .equals("")) ? ""
                                                            : jsonObject.getString("RegistrationAllowed");
                                            if (RegistrationAllowed.equalsIgnoreCase("true")) {
                                                String eKYC_UserID = (jsonObject.isNull("eKYC_UserID")
                                                        || jsonObject.getString("eKYC_UserID").equals(""))
                                                        ? "0" : jsonObject.getString("eKYC_UserID");
                                                if (AccountDetails.isNewEkyc()) {
                                                    HashMap<String, String> map = new HashMap();
                                                    map.put(NEW_USERID, eKYC_UserID);
                                                    map.put(BRANCHEMPCODE,
                                                            AccountDetails.getEmployeeCode());
                                                    map.put(COMMANDTYPE, "RESUME");
                                                    map.put(APPLICATIONTYPE, "CRM");
                                                    map.put(OSTYPE, "ANDROID");
                                                    map.put(APPLOGINID,
                                                            AccountDetails.getEmployeeCode());
                                                    map.put(TOKENKEY, AccountDetails.getToken());
                                                    newEkyc(getInputBody(map));
                                                } else {
                                                    HashMap<String, String> hashMap = new HashMap();
                                                    hashMap.put(USERID, eKYC_UserID);
                                                    gotoEkycPage(CALL_PROSPECT_DETAILS, hashMap);
                                                }
                                            } else {
                                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                        getResources().getString(R.string.motilal_oswal),
                                                        message, "OK", true, null);
                                            }
                                        } else {
                                            hideProgress();
                                            String errorMsg =
                                                    CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                    getResources().getString(R.string.motilal_oswal),
                                                    errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        hideProgress();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgress();
                                    MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                hideProgress();
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    hideProgress();
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                UNAUTHORIZE_USER, "OK", false,
                                                new MSFDialog.DialogListener() {
                                                    @Override
                                                    public void alertDialogAction(MSFDialog.Action action,
                                                                                  Object... data) {
                                                        if (action == MSFDialog.Action.OK) {
                                                            Intent i = new Intent(MOSLCommonActivity.this,
                                                                    LoginActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                        return;
                                    } else {
                                        String error =
                                                CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                        if (CRMAppUtil.isBlank(error)) {
                                            error = SOME_SERVER_ERROR_OCCURRED;
                                        }
                                        MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                getResources().getString(R.string.motilal_oswal), error,
                                                "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideProgress();
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString,
                                                  Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                hideProgress();
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                    getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                    null);
        }
    }

    //quick dialog
    public void openSearchOptions(View v) {
        ActionItem acLead =
                new ActionItem(ID_LEAD, "Lead", getChecedStateIcon(SEARCH_TYPE_LEAD), this);
        ActionItem acCleint =
                new ActionItem(ID_CLEINT, "Client", getChecedStateIcon(SEARCH_TYPE_CLIENT), this);
        ActionItem acDadhBoard =
                new ActionItem(ID_EMP, "Dashbord for", getChecedStateIcon(SEARCH_TYPE_EMP), this);

        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
        quickAction.addActionItem(acLead);
        quickAction.addActionItem(acCleint);
        quickAction.addActionItem(acDadhBoard);
        quickAction.setOnActionItemClickListener(this);
        quickAction.show(v);
    }

    private int getChecedStateIcon(String data) {
        if (searchType.equalsIgnoreCase(data)) {
            return R.drawable.ic_check_black;
        } else {
            return R.drawable.ic_check_blank;
        }
    }

    //search option listren
    @Override
    public void onItemClick(QuickAction source, int pos, int actionId) {

        switch (actionId) {
            case ID_LEAD: {
                searchType = SEARCH_TYPE_LEAD;
                actionBar.setSearchHint(SEARCH_TYPE_LEAD_HINT);
            }
            break;
            case ID_CLEINT: {
                searchType = SEARCH_TYPE_CLIENT;
                actionBar.setSearchHint(SEARCH_TYPE_CLIENT_HINT);
               /* OneTimeWorkRequest periodicWork = new OneTimeWorkRequest.Builder(Worker.class)
                        .setConstraints(networkConstraints()).build();
                mWorkManager.enqueue(periodicWork);*/
            }
            break;
            case ID_EMP: {
                searchType = SEARCH_TYPE_EMP;
                actionBar.setSearchHint(SEARCH_TYPE_DASHBOARD_HINT);
            }
            break;
        }
    }

    public void getRoleDetails(String roleID) {
        if (MOSLCommon.isNetworkEnabled(this)) {
            Map inputMap = new HashMap<>();
            inputMap.put(APP_LOGIN_ID, getAppLoginID());
            inputMap.put(ROLE_MAPPING_ID, roleID);

            String flag = GETCRMROLEDETAILS;
            String category = LOGIN;
            progressLayout.setVisibility(View.VISIBLE);
            WebServiceHandler.post(MOSLCommonActivity.this, flag, category, inputMap,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if (response != null) {
                                try {
                                    JSONArray jsonArray = response.getJSONArray("CRMROLE");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject object = jsonArray.getJSONObject(i);

                                            String boBranch =
                                                    (object.isNull("BOBRANCH") || object.getString(
                                                            "BOBRANCH").equals("")) ? ""
                                                            : object.getString("BOBRANCH");
                                            String boSubBroker =
                                                    (object.isNull("BOSUBBROKER") || object.getString(
                                                            "BOSUBBROKER").equals("")) ? ""
                                                            : object.getString("BOSUBBROKER");
                                            String boTraders =
                                                    (object.isNull("BOTRADERS") || object.getString(
                                                            "BOTRADERS").equals("")) ? ""
                                                            : object.getString("BOTRADERS");
                                            String crmBranch =
                                                    (object.isNull("CRMBRANCH") || object.getString(
                                                            "CRMBRANCH").equals("")) ? ""
                                                            : object.getString("CRMBRANCH");
                                            String loginUserID =
                                                    (object.isNull("LOGINUSERID") || object.getString(
                                                            "LOGINUSERID").equals("")) ? ""
                                                            : object.getString("LOGINUSERID");
                                            String roleMappingID =
                                                    (object.isNull("ROLEMAPPINGID") || object.getString(
                                                            "ROLEMAPPINGID").equals("")) ? ""
                                                            : object.getString("ROLEMAPPINGID");
                                            String businessUnitID =
                                                    (object.isNull("BUSINESSUNITID") || object.getString(
                                                            "BUSINESSUNITID").equals("")) ? ""
                                                            : object.getString("BUSINESSUNITID");
                                            String teamID =
                                                    (object.isNull("TEAMID") || object.getString("TEAMID")
                                                            .equals("")) ? "" : object.getString("TEAMID");
                                            String roleID =
                                                    (object.isNull("ROLEID") || object.getString("ROLEID")
                                                            .equals("")) ? "" : object.getString("ROLEID");
                                            SharedPreferences.Editor editor =
                                                    MOSLCommon.getPrefs(MOSLCommonActivity.this).edit();
                                            SharedPreferences.Editor editor1 =
                                                    editor.putString(BOBRANCH, boBranch);
                                            editor.putString(BOSUBBROKER, boSubBroker);
                                            editor.putString(BOTRADERS, boTraders);
                                            editor.putString(CRMBRANCH, crmBranch);
                                            editor.putString(LOGINUSERID, loginUserID);
                                            editor.putString(ROLEMAPPINGID, roleMappingID);
                                            editor.putString(BUSINESSUNITID, businessUnitID);
                                            editor.putString(TEAMID, teamID);
                                            editor.putString(ROLEID, roleID);

                                            editor.commit();
                                            roleSelectionModel =
                                                    new RoleSelectionModel(boBranch, boSubBroker, boTraders,
                                                            crmBranch, loginUserID, roleMappingID,
                                                            businessUnitID, teamID, roleID);
                                            progressLayout.setVisibility(View.GONE);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                progressLayout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString,
                                              Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            progressLayout.setVisibility(View.GONE);
                            commonMsgDialog(MOSLCommonActivity.this, SOME_ERROR_OCCURED_PLEASE_RETRY,
                                    false, "");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                              JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                              JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
        } else {
            MOSLCommon.alertDInternet(this);
        }
    }

    public void showRoleDialog(Context context, ArrayList<RowData> arrRoleSel, final int tab) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(com.mosl.mobile.ekyc.R.layout.dialog_role_selection);
        dialog.getWindow()
                .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCanceledOnTouchOutside(false);
        spRoleSel = (Spinner) dialog.findViewById(com.mosl.mobile.ekyc.R.id.spRoleSel);
        Button btnGoToEkyc = (Button) dialog.findViewById(com.mosl.mobile.ekyc.R.id.btnGoToEkyc);

        arrAdapRoleSel = new ArrayAdapter<RowData>(MOSLCommonActivity.this,
                com.mosl.mobile.ekyc.R.layout.simple_spinner_item, arrRoleSel);
        arrAdapRoleSel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRoleSel.setAdapter(arrAdapRoleSel);
        spRoleSel.setOnItemSelectedListener(roleListener);
        progressLayout.setVisibility(View.GONE);

        btnGoToEkyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoleSelectionModel.getRoleMappingID() != null
                        && !RoleSelectionModel.getRoleMappingID().equals("")) {
                    if (tab == 1) {
                        Intent intent = new Intent(MOSLCommonActivity.this,
                                com.mosl.mobile.ekyc.ui.PostLoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(MOSLCommonActivity.this,
                                com.mosl.mobile.ekyc.register.RegisterActivity.class);
                        startActivity(intent);
                    }
                } else {
                    MOSLCommon.showToastMsg(MOSLCommonActivity.this, SELECT_ROLE);
                }

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //region login methods

    /**
     * this does Login ofuser from stored password
     */
    private void handleStartupPage() {
        String empCode = getPrefs(this).getString("EmployeeCode", null);
        String password = getPrefs(this).getString("PASSWORD", null);

        if (empCode != null && password != null) {
            if (CRMAppUtil.isNetworkEnabled(this)) {

                Map inputMap = new HashMap<>();
                inputMap.put("EmployeeCode", empCode);
                inputMap.put("Password", password);
                inputMap.put("IPAddress", CRMAppUtil.getLocalIpAddress());
                inputMap.put("ClientInfo", CLIENT_INFO);
                inputMap.put("Macaddress",
                        CRMMOSLCommon.getDeviceID(getApplicationContext(), getContentResolver()));
                inputMap.put("AppId", "2");

                Map headerMap = null;
                String serviceMethod = "Auth/Login";

                try {
                    CRMWebServiceHandler.post(this, serviceMethod, headerMap, inputMap,
                            new JsonHttpResponseHandler() {

                                @Override
                                public void onSuccess(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers,
                                                      JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    //after success code goes here
                                    try {
                                        if (statusCode == 200) {

                                            String status = response.getString(STATUS);
                                            if (status.equalsIgnoreCase("true")) {
                                                JSONObject objDetail = response.getJSONObject(RESPONSE);
                                                boolean smtEnabled =
                                                        objDetail.getString(SMT_ENABLED).equals("1");
                                                JSONObject objDetailUser =
                                                        objDetail.getJSONObject(USER);
                                                String userId = objDetailUser.isNull(USER_ID) ?
                                                        "" : objDetailUser.getString(USER_ID);
                                                String employeeCode =
                                                        objDetailUser.isNull(EMPLOYEE_CODE) ?
                                                                "" : objDetailUser.getString(EMPLOYEE_CODE);
                                                String firstName = objDetailUser.isNull(FIRST_NAME) ?
                                                        "" : objDetailUser.getString(FIRST_NAME);
                                                String lastName = objDetailUser.isNull(LAST_NAME) ?
                                                        "" : objDetailUser.getString(LAST_NAME);
                                                String imageUrl = objDetailUser.isNull(IMAGE_URL) ?
                                                        "" : objDetailUser.getString(IMAGE_URL);
                                                String mobile = objDetailUser.isNull(MOBILE) ?
                                                        "" : objDetailUser.getString(MOBILE);
                                                String email = objDetailUser.isNull(EMAIL) ?
                                                        "" : objDetailUser.getString(EMAIL);
                                                String token = objDetailUser.isNull(TOKEN) ?
                                                        "" : objDetailUser.getString(TOKEN);
                                                String userType = objDetailUser.isNull(USER_TYPE_SEQ) ?
                                                        "" : objDetailUser.getString(USER_TYPE_SEQ);
                                                JSONObject objDetailDefaultBusinessProfile =
                                                        objDetailUser.getJSONObject(
                                                                DEFAULT_BUSINESS_PROFILE);
                                                String branchCode =
                                                        objDetailDefaultBusinessProfile.isNull(
                                                                BRANCHCODE_DEFAULT_BUSINESS_PROFILE) ?
                                                                "" : objDetailDefaultBusinessProfile.getString(
                                                                BRANCHCODE_DEFAULT_BUSINESS_PROFILE);
                                                setAppLoginID(employeeCode);
                                                setAppType(CRM);

                                                JSONObject objDetailBusinessProfile =
                                                        objDetailUser.getJSONObject(
                                                                CURRENT_BUSINESS_PROFILE);
                                                String businessProfileId =
                                                        objDetailBusinessProfile.isNull(
                                                                BUSINESS_PROFILE_ID) ?
                                                                "" : objDetailBusinessProfile.getString(
                                                                BUSINESS_PROFILE_ID);
                                                String businessProfileName =
                                                        objDetailBusinessProfile.isNull(
                                                                CURRENT_BUSINESS_PROFILE_NAME) ?
                                                                "" : objDetailBusinessProfile.getString(
                                                                CURRENT_BUSINESS_PROFILE_NAME);
                                                String businessUnitId = objDetailBusinessProfile.isNull(BUSINESS_UNIT_ID) ?
                                                        "" : objDetailBusinessProfile.getString(
                                                        BUSINESS_UNIT_ID);
                                                boolean aufsb = userType.equals("9");

                                                new AccountDetails(userId,
                                                        firstName, lastName,
                                                        imageUrl.replaceAll(" ", "%20"),
                                                        mobile, email,
                                                        businessProfileId,
                                                        businessProfileName,
                                                        employeeCode, token,
                                                        businessUnitId,
                                                        branchCode, userType,
                                                        smtEnabled, aufsb);

                                                initView();
                                            } else {

                                                handleLoginError();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onSuccess(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers,
                                                      JSONArray response) {
                                    super.onSuccess(statusCode, headers, response);
                                }

                                @Override
                                public void onSuccess(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers,
                                                      String responseString) {
                                    super.onSuccess(statusCode, headers, responseString);
                                }

                                @Override
                                public void onFailure(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                      JSONObject errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    handleLoginError();
                                }

                                @Override
                                public void onFailure(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                      JSONArray errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                }

                                @Override
                                public void onFailure(int statusCode,
                                                      cz.msebera.android.httpclient.Header[] headers,
                                                      String responseString, Throwable throwable) {
                                    super.onFailure(statusCode, headers, responseString, throwable);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                MSFDialog.alertDialog(this, 0, getResources().getString(R.string.motilal_oswal),
                        INTERNET_CONNECTION, "OK", false, new MSFDialog.DialogListener() {
                            @Override
                            public void alertDialogAction(MSFDialog.Action action, Object... data) {
                                if (action == MSFDialog.Action.OK) {
                                    finish();
                                }
                            }
                        });
            }
        } else {

            handleLoginError();
        }
    }

    private void handleLoginError() {

        clearLoginPrefData();
        Intent i = new Intent(MOSLCommonActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void clearLoginPrefData() {

        getPrefs(this).edit().remove("EmployeeCode");
        getPrefs(this).edit().remove("PASSWORD");
    }
    //endregion

    /**
     * this method methods is goto
     */
    public void navigateToFromNotification(String dataStr) {
        if (dataStr == null || dataStr.length() < 1) {
            //Noproper data goto dsh board
            setupFragment(new DashboardFragment());
        }
        NotificationData data = new NotificationData(dataStr);
        if (data.getPage().equalsIgnoreCase("LEADVIEW")) {
            //Goto leadView Detail
            MOSLCommonFragment fragment = new LeadViewMainFragment();
            //BackStack
            Bundle bundle = new Bundle();
            String leadId = data.getParamMap().get(LEAD_ID);
            String ucid = data.getParamMap().get(UCID);
            ucid = (ucid == null || ucid.equalsIgnoreCase("")) ? "0" : ucid;
            leadId = (leadId == null || leadId.equalsIgnoreCase("")) ? "0" : leadId;
            bundle.putString(UCID, ucid);
            bundle.putString(LEAD_ID, leadId);
            bundle.putParcelable(NOTIFICATION_DATA, data);
            fragment.setArguments(bundle);
            setupFragment(fragment);
        }
        //PENDINGAPPROVAL
        else if (data.getPage().equalsIgnoreCase("PENDINGAPPROVAL")) {
            MOSLCommonFragment fragment = new PendingApprovalFragment();
            Bundle bundle = new Bundle();
            fragment.setArguments(bundle);
            setupFragment(fragment);
        }
        //BIRTHDAY
        else if (data.getPage().equalsIgnoreCase("BIRTHDAY")) {
            setupFragment(new BirthdayAnniversaryFragment());
        }
        //DASHBOARD
        else if (data.getPage().equalsIgnoreCase("DASHBOARD")) {
            setupFragment(new DashboardFragment());
        } else {
            setupFragment(new DashboardFragment());
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null
                && (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_MOVE)
                && view instanceof EditText
                && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft()
                    || x > view.getRight()
                    || y < view.getTop()
                    || y > view.getBottom()) {
                ((InputMethodManager) this.getSystemService(
                        Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                        (this.getWindow().getDecorView().getApplicationWindowToken()), 0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    ///////NOTIFICATION COUNT /////////////
    @Override
    protected void onStart() {
        if (AccountDetails.getToken() == null) {
            Intent intent = new Intent(MOSLCommonActivity.this, SplashActivity.class);
            startActivity(intent);
        }
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver((mMessageReceiver), new IntentFilter("NotificationCountIntent"));
        bindService(new Intent(this, ForegroundService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        unbindService(mServiceConnection);
        mBound = false;
    }

    public void ekycVersionCheck(final int gotoEkyc, final Map hashMap) {
        Map inputMap = new HashMap<>();
        inputMap.put("_ApplicationType", NEW_CRM);//For UAT
        inputMap.put("_Version", getAppVersionCode(this));

        String flag = "APPVERSIONCHECK";
        String category = "LOGIN";

        WebServiceHandler.post(this, flag, category, inputMap, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                //after success code goes here
                try {
                    if (statusCode == 200) {
                        if (response.length() != 0) {
                            String success = response.getString("_Status");
                            String msg = response.getString("_msg");
                            if (success.equals("0")) {
                                CustomDialog dialogMsg =
                                        new CustomDialog(MOSLCommonActivity.this, msg, true, false,
                                                false, "", "UPDATE NOW", "",
                                                new CustomDialog.CustomDialogListener() {
                                                    @Override
                                                    public void dialogAction(MOSLCommon.Action action,
                                                                             Object... data) {
                                                        if (action == MOSLCommon.Action.OK) {
                                                            Intent i = new Intent(
                                                                    android.content.Intent.ACTION_VIEW);
                                                            i.setData(Uri.parse(
                                                                    "https://play.google.com/store/apps/details?id=com.mosl.tabapp"));
                                                            startActivity(i);
                                                            finish();
                                                        } else if (action == MOSLCommon.Action.CANCEL) {
                                                        }
                                                    }
                                                });
                                dialogMsg.show();
                            } else if (success.equals("2")) {
                                CustomDialog dialogMsg =
                                        new CustomDialog(MOSLCommonActivity.this, msg, true, false,
                                                false, "", "OK", "",
                                                new CustomDialog.CustomDialogListener() {
                                                    @Override
                                                    public void dialogAction(MOSLCommon.Action action,
                                                                             Object... data) {

                                                    }
                                                });
                                dialogMsg.show();
                            } else {
                                SharedPreferences prefs = getSharedPreferences(BINDCOUNTRY, 0);
                                int countrySize = prefs.getInt(BINDCOUNTRY + "_size", 0);
                                if (countrySize != 0) {
                                    gotoEkycPage(gotoEkyc, hashMap);
                                } else {
                                    countryAPI(gotoEkyc, hashMap);
                                }
                            }
                        } else {
                            MOSLCommon.showToastMsg(MOSLCommonActivity.this, EKYC_SERVER_DOWN);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MOSLCommon.commonMsgDialog(MOSLCommonActivity.this, EKYC_SERVER_DOWN, false,
                            "");
                }
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  String responseString) {
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                MOSLCommon.showToastMsg(MOSLCommonActivity.this, responseString);
            }
        });
    }

    public ArrayList countryAPI(final int gotoEkyc, final Map hashMap) {
        SharedPreferences prefs = getSharedPreferences(BINDCOUNTRY, 0);
        int countrySize = prefs.getInt(BINDCOUNTRY + "_size", 0);
        if (countrySize == 0) {
            Map inputMap = new HashMap<>();
            inputMap.put("", "");

            String flag = BINDCOUNTRY;
            String category = SIGNUP;

            if (MOSLCommon.isNetworkEnabled(MOSLCommonActivity.this)) {

                WebServiceHandler.post(MOSLCommonActivity.this, flag, category, inputMap,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        String countryDetails =
                                                (response.isNull("CountryDetails") || response.getString(
                                                        "CountryDetails").equals("")) ? ""
                                                        : response.getString("CountryDetails");
                                        JSONArray jsonArray = new JSONArray(countryDetails);
                                        array.clear();
                                        array.add("Select");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject countryObj = jsonArray.getJSONObject(i);

                                            String country =
                                                    (countryObj.isNull("Country") || countryObj.getString(
                                                            "Country").equals("")) ? ""
                                                            : countryObj.getString("Country");
                                            array.add(country);
                                        }
                                        if (array.size() != 0) {
                                            SharedPreferences prefs =
                                                    getSharedPreferences(BINDCOUNTRY, 0);
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putInt(BINDCOUNTRY + "_size", array.size());
                                            for (int i = 0; i < array.size(); i++) {
                                                editor.putString(BINDCOUNTRY + "_" + i,
                                                        array.get(i).toString());
                                            }
                                            editor.commit();
                                            gotoEkycPage(gotoEkyc, hashMap);
                                        } else {
                                            CustomDialog dialogMsg =
                                                    new CustomDialog(MOSLCommonActivity.this,
                                                            SOME_SERVER_ERROR_OCCURRED, true, false, false, "",
                                                            OK, "", new CustomDialog.CustomDialogListener() {
                                                        @Override
                                                        public void dialogAction(MOSLCommon.Action action,
                                                                                 Object... data) {
                                                            if (action == MOSLCommon.Action.OK) {
                                                                finish();
                                                            } else if (action == MOSLCommon.Action.CANCEL) {
                                                                finish();
                                                            }
                                                        }
                                                    });
                                            dialogMsg.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers,
                                                  String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                CustomDialog dialogMsg = new CustomDialog(MOSLCommonActivity.this,
                                        SOME_SERVER_ERROR_OCCURRED, true, false, false, "", OK, "",
                                        new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action,
                                                                     Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    finish();
                                                } else if (action == MOSLCommon.Action.CANCEL) {
                                                    finish();
                                                }
                                            }
                                        });
                                dialogMsg.show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                                  JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                CustomDialog dialogMsg = new CustomDialog(MOSLCommonActivity.this,
                                        SOME_SERVER_ERROR_OCCURRED, true, false, false, "", OK, "",
                                        new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action,
                                                                     Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    finish();
                                                } else if (action == MOSLCommon.Action.CANCEL) {
                                                    finish();
                                                }
                                            }
                                        });
                                dialogMsg.show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                                                  JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                CustomDialog dialogMsg = new CustomDialog(MOSLCommonActivity.this,
                                        SOME_SERVER_ERROR_OCCURRED, true, false, false, "", OK, "",
                                        new CustomDialog.CustomDialogListener() {
                                            @Override
                                            public void dialogAction(MOSLCommon.Action action,
                                                                     Object... data) {
                                                if (action == MOSLCommon.Action.OK) {
                                                    finish();
                                                } else if (action == MOSLCommon.Action.CANCEL) {
                                                    finish();
                                                }
                                            }
                                        });
                                dialogMsg.show();
                            }
                        });
            } else {
                MOSLCommon.alertDInternet(MOSLCommonActivity.this);
            }
            fatcaArray = new ArrayList<>(array);
            return array;
        } else {
            return loadCountryArray(MOSLCommonActivity.this);
        }
    }

    public void gotoEkycPage(int xyz, Map hashMap) {
        switch (xyz) {
            case MOSL_MENU_FRESH_REGISTRATION:
                Intent registerIntent = new Intent(MOSLCommonActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;
            case MOSL_MENU_EXISTINGS_APPLICATIONS:
                Intent postLoginIntent =
                        new Intent(MOSLCommonActivity.this, PostLoginActivity.class);
                startActivity(postLoginIntent);
                break;
            case CALL_CONVERT_EKYC:
                ConvertEkycApi(hashMap.get(MOBILE).toString(), hashMap.get(EMAIL).toString(),
                        hashMap.get(LEAD_OWNER).toString(), hashMap.get(SOURCENAME).toString(),
                        hashMap.get(PROSPECT_SOURCENAME).toString(),
                        hashMap.get(PROSPECT_SOURCEDETAIL).toString(),
                        hashMap.get(INTRESTEDIN).toString());
                break;
            case CALL_PROSPECT_DETAILS:
                Intent intent =
                        new Intent(MOSLCommonActivity.this, BranchResumeApplicationActivity.class);
                intent.putExtra("UserId", hashMap.get(USERID).toString());
                startActivity(intent);
                break;
        }
    }

    public void newEkyc(final String body) {
        try {
            Intent launchIntent =
                    getPackageManager().getLaunchIntentForPackage(NEWEKYC_PACKAGE_NAME);
            launchIntent.putExtra(OUTPUT, CRMSecurityUtil.encrypt(body));
            startActivity(launchIntent);
        } catch (Exception e) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(
                    Uri.parse("https://play.google.com/store/apps/details?id=" + NEWEKYC_PACKAGE_NAME));
            startActivity(i);
        }
    }

    private Constraints networkConstraints() {
        Constraints constraints =
                new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();
        return constraints;
    }

    public static String getInputBody(Map param) {
        String result = "";
        if (param != null && param.size() > 0) {
            //      result += "{";
            Set<String> keys = param.keySet();
            for (String key : keys) {
                String value = param.get(key) == null ? null : param.get(key).toString();
                if (value == null) {
                    result += key + "=" + "" + "&";
                } else {
                    result += key + "=" + value + "&";
                }
            }
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private void startWorkManager() {
        PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(DataSyncWorker.class, 15,
                TimeUnit.MINUTES).setConstraints(networkConstraints()).build();
        mWorkManager.enqueue(periodicWork);

        compressionWorkId = periodicWork.getId();
    }

    private void getSuggestMe() {

        if (CRMAppUtil.isNetworkEnabled(MOSLCommonActivity.this)) {
            showProgress();
            Map inputMap = new HashMap<>();
            inputMap.put(UCID, ZERO);
            inputMap.put(LEAD_ID, ZERO);
            String serviceMethod = "Prospect/SMT_RegisterProspectOnline/";
            try {
                CRMWebServiceHandler.post(MOSLCommonActivity.this, serviceMethod,
                        getHeader(MOSLCommonActivity.this), inputMap, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                //after success code goes here
                                try {

                                    if (statusCode == 200) {
                                        String status = response.getString(STATUS);
                                        if (status.equalsIgnoreCase("true")) {
                                            hideProgress();
                                            JSONObject objDetail = response.getJSONObject(RESPONSE);
                                            String encryptedUrl = (objDetail.isNull(ENCRYPTED_URL)) ? ""
                                                    : objDetail.getString(ENCRYPTED_URL);
                                            SMTFragment fragment = new SMTFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString(URL_PATH, encryptedUrl);

                                            fragment.setArguments(bundle);
                                            addFragment(R.id.activityFrameLayout, fragment, true, false);
//                                            Intent intent =
//                                                    new Intent(MOSLCommonActivity.this, WebActivity.class);
//                                            intent.putExtra(URL_PATH, encryptedUrl);
//                                            startActivity(intent);
                                        } else {
                                            hideProgress();
                                            String errorMsg =
                                                    CRMMOSLCommon.getErrorMsgFromResponse(response);
                                            alertDialog(MOSLCommonActivity.this, 0,
                                                    getResources().getString(R.string.motilal_oswal),
                                                    errorMsg, "OK", true, null);
                                        }
                                    } else {
                                        hideProgress();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    hideProgress();
                                    alertDialog(MOSLCommonActivity.this, 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
                                }
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                                super.onSuccess(statusCode, headers, response);
                                hideProgress();
                            }

                            @Override
                            public void onSuccess(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString) {
                                super.onSuccess(statusCode, headers, responseString);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                try {
                                    hideProgress();
                                    if (statusCode == UNAUTHORIZE_STATUS_CODE) {
                                        alertDialog(MOSLCommonActivity.this, 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                UNAUTHORIZE_USER, "OK", false,
                                                new MSFDialog.DialogListener() {
                                                    @Override
                                                    public void alertDialogAction(MSFDialog.Action action,
                                                                                  Object... data) {
                                                        if (action == MSFDialog.Action.OK) {
                                                            Intent i = new Intent(MOSLCommonActivity.this,
                                                                    LoginActivity.class);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                        return;
                                    } else {
                                        String error =
                                                CRMMOSLCommon.getErrorMsgFromResponse(errorResponse);
                                        if (CRMAppUtil.isBlank(error)) {
                                            error = SOME_SERVER_ERROR_OCCURRED;
                                        }
                                        MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                getResources().getString(R.string.motilal_oswal), error,
                                                "OK", true, null);
                                        return;
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                            getResources().getString(R.string.motilal_oswal),
                                            SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                                }
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, Throwable throwable,
                                                  JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                hideProgress();
                                alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }

                            @Override
                            public void onFailure(int statusCode,
                                                  cz.msebera.android.httpclient.Header[] headers, String responseString,
                                                  Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                hideProgress();
                                alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        SOME_SERVER_ERROR_OCCURRED, "OK", true, null);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                hideProgress();
                alertDialog(MOSLCommonActivity.this, 0,
                        getResources().getString(R.string.motilal_oswal),
                        SOME_ERROR_OCCURED_PLEASE_RETRY, "OK", true, null);
            }
        } else {
            hideProgress();
            alertDialog(MOSLCommonActivity.this, 0,
                    getResources().getString(R.string.motilal_oswal), INTERNET_CONNECTION, "OK", true,
                    null);
        }
    }

    public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
        Context context;
        private LayoutInflater inflater;
        private List<SearchResultModel> resultList = new ArrayList<>();

        public AutoCompleteAdapter(Context context) {
            this.inflater = LayoutInflater.from(context);
            this.context = context;
        }

        public void clearAll() {
            resultList.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public SearchResultModel getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final HolderView holderView;
            final SearchResultModel searchResultModel = resultList.get(position);
            if (convertView == null) {
                inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.crm_client_search_item, parent, false);
                holderView = new HolderView(convertView);
                convertView.setTag(holderView);
            } else {
                holderView = (HolderView) convertView.getTag();
            }
            String name = resultList.get(position).getName();
            if (CRMAppUtil.isBlank(name)) {
                holderView.llExpand.setVisibility(View.GONE);
            } else {
                holderView.llExpand.setVisibility(View.VISIBLE);
            }

            if (searchResultModel.isExpanded()) {
                holderView.llDropDownMenuLayout.setVisibility(View.VISIBLE);
                ((ListView) parent).smoothScrollToPosition(position);
            } else {
                holderView.llDropDownMenuLayout.setVisibility(View.GONE);
            }

            if (searchType.equalsIgnoreCase(SEARCH_TYPE_EMP)) {// employee

                String empCode = resultList.get(position).getEmpCode();
                String businessProfileName = resultList.get(position).getBusinessProfileName();
                if (name.equalsIgnoreCase(NO_RECORDS_FOUND)) {
                    holderView.ivDropDownArrow.setVisibility(View.GONE);
                    holderView.llExpand.setOnClickListener(null);
                    holderView.tvClientID.setText("");
                    holderView.tvUCID.setText("");
                    holderView.tvClientName.setText("");
                    holderView.tvError.setVisibility(View.VISIBLE);
                } else {
                    holderView.ivDropDownArrow.setVisibility(View.GONE);
                    holderView.llExpand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DashboardFragment mDashBoard = new DashboardFragment();
                            Bundle bund = new Bundle();
                            bund.putString(PROFILE_ID, searchResultModel.getBusinessProfileId());
                            bund.putString(PRODUCT_ID, searchResultModel.getProductId());
                            bund.putString(EMPLOYEE_CODE, searchResultModel.getEmpCode());
                            mDashBoard.setArguments(bund);
                            setupFragment(mDashBoard);
                            autoCompleteAdapter.clearAll();
                            actionBar.clearSearchText();
                        }
                    });
                    holderView.tvClientName.setText("Name :" + name);
                    holderView.tvClientID.setText("Emp Id: " + empCode);
                    holderView.tvUCID.setText("Business Profile: " + businessProfileName);

                    holderView.tvError.setVisibility(View.GONE);
                    searchFoundFlag = false;
                }
            } else {
                String leadID = resultList.get(position).getLeadId();
                String ucid = resultList.get(position).getUCID();

                setAppointmentFragment = new SetAppointmentFragment();
                leadViewMainFragment = new LeadViewMainFragment();
                createMeetingFragment = new CreateMeetingFragment();
                holderView.tvClientName.setText(name);
                if (!resultList.get(position).getClientCode().equalsIgnoreCase("0")) {
                    holderView.tvClientID.setText(
                            "Lead Id/Client Code: " + resultList.get(position).getClientCode());
                } else {
                    holderView.tvClientID.setText("Lead Id/Client Code: " + leadID);
                }
                holderView.tvUCID.setText("UCID: " + ucid);
                if (!resultList.get(position).getUCID().equalsIgnoreCase("0")) {
                    ekycMenuType = SEARCH_TYPE_CLIENT;
                } else {
                    ekycMenuType = SEARCH_TYPE_LEAD;
                }
                if (ekycMenuType.equalsIgnoreCase(SEARCH_TYPE_LEAD)) {
                    Integer oaoUserId = resultList.get(position).getOaoUserId();
                    if (oaoUserId != null && oaoUserId > 0) {
                        holderView.rlOpenAccount.setVisibility(View.GONE);
                        holderView.rlOAOForm.setVisibility(View.VISIBLE);
                    } else {
                        //show OAOForm if oaoUserId exists
                        holderView.rlOpenAccount.setVisibility(View.VISIBLE);
                        holderView.rlOAOForm.setVisibility(View.GONE);
                    }
                } else if (ekycMenuType.equalsIgnoreCase(SEARCH_TYPE_CLIENT)) {
                    Integer oaoUserId = resultList.get(position).getOaoUserId();
                    if (oaoUserId != null && oaoUserId > 0) {
                        holderView.rlOpenAccount.setVisibility(View.GONE);
                        holderView.rlOAOForm.setVisibility(View.VISIBLE);
                    } else {//show OAOForm if oaoUserId exists
                        holderView.rlOpenAccount.setVisibility(View.GONE);
                        holderView.rlOAOForm.setVisibility(View.GONE);
                    }
                }

                holderView.rlSetAppointment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(LEAD_ID, searchResultModel.getLeadId());
                        bundle.putString(UCID, searchResultModel.getUCID());
                        setAppointmentFragment.setArguments(bundle);
                        addFragment(R.id.activityFrameLayout, setAppointmentFragment, true, false);
                        hideKeyboard(actionBar.getSearchBoxView());
                        autoCompleteAdapter.clearAll();
                        actionBar.clearSearchText();
                    }
                });
                holderView.rlCrateMeeting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(LEAD_ID, searchResultModel.getLeadId());
                        bundle.putString(UCID, searchResultModel.getUCID());
                        createMeetingFragment.setArguments(bundle);
                        addFragment(R.id.activityFrameLayout, createMeetingFragment, true, false);
                        hideKeyboard(actionBar.getSearchBoxView());
                        autoCompleteAdapter.clearAll();
                        actionBar.clearSearchText();
                    }
                });
                holderView.rlViewDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(LEAD_ID, searchResultModel.getLeadId());
                        bundle.putString(MOBILE, searchResultModel.getMobileNo());
                        bundle.putString(UCID, searchResultModel.getUCID());
                        leadViewMainFragment.setArguments(bundle);
                        addFragment(R.id.activityFrameLayout, leadViewMainFragment, true, false);
                        hideKeyboard(view);
                        autoCompleteAdapter.clearAll();
                        hideKeyboard(actionBar.getSearchBoxView());
                        actionBar.clearSearchText();
                    }
                });
                holderView.rlOpenAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO call: convert to eKYC
                        autoCompleteAdapter.clearAll();
                        hideKeyboard(actionBar.getSearchBoxView());
                        actionBar.clearSearchText();
                        Map inpuMap = new HashMap();
                        inpuMap.put(MOBILE, searchResultModel.getMobileNo());
                        inpuMap.put(EMAIL, searchResultModel.getGmail());
                        inpuMap.put(LEAD_OWNER, searchResultModel.getLeadOwner());
                        inpuMap.put(SOURCENAME, searchResultModel.getSourceName());
                        inpuMap.put(PROSPECT_SOURCENAME, searchResultModel.getProspectSourceName());
                        inpuMap.put(PROSPECT_SOURCEDETAIL,
                                searchResultModel.getProspectSourceDetails());
                        inpuMap.put(INTRESTEDIN, searchResultModel.getInterestedIn());
                        if (AccountDetails.isNewEkyc()) {
                            gotoEkycPage(CALL_CONVERT_EKYC, inpuMap);
                        } else {
                            ekycVersionCheck(CALL_CONVERT_EKYC, inpuMap);
                        }
                    }
                });
                holderView.rlOAOForm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO call: resume eKYC application (BRANCH)
                        autoCompleteAdapter.clearAll();
                        hideKeyboard(actionBar.getSearchBoxView());
                        actionBar.clearSearchText();
                        Map hashMap = new HashMap();
                        hashMap.put(USERID, searchResultModel.getOaoUserId().toString());
                        if (AccountDetails.isNewEkyc()) {
                            HashMap<String, String> map = new HashMap();
                            map.put(NEW_USERID, searchResultModel.getOaoUserId().toString());
                            map.put(BRANCHEMPCODE, AccountDetails.getEmployeeCode());
                            map.put(COMMANDTYPE, "RESUME");
                            map.put(APPLICATIONTYPE, "CRM");
                            map.put(OSTYPE, "ANDROID");
                            map.put(APPLOGINID, AccountDetails.getEmployeeCode());
                            map.put(TOKENKEY, AccountDetails.getToken());
                            newEkyc(getInputBody(map));
                        } else {
                            ekycVersionCheck(CALL_PROSPECT_DETAILS, hashMap);
                        }
                    }
                });

                holderView.llExpand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(actionBar.getSearchBoxView());
                        if (!searchResultModel.isExpanded()) {
                            searchResultModel.setExpanded(true);
                        } else {
                            searchResultModel.setExpanded(false);
                        }
                        notifyDataSetChanged();
                    }
                });

                if (CRMAppUtil.isBlank(leadID)) {
                    holderView.ivDropDownArrow.setVisibility(View.GONE);
                    holderView.llExpand.setOnClickListener(null);
                    holderView.tvClientID.setText("");
                    holderView.tvUCID.setText("");
                    holderView.tvClientName.setText("");
                } else {
                    holderView.ivDropDownArrow.setVisibility(View.VISIBLE);
                    holderView.tvError.setVisibility(View.GONE);
                }
                if (searchFoundFlag && name.equalsIgnoreCase(NO_RECORDS_FOUND)) {
                    holderView.llExpand.setVisibility(View.GONE);
                    holderView.ivDropDownArrow.setVisibility(View.GONE);
                    holderView.llExpand.setOnClickListener(null);
                    holderView.tvClientID.setText("");
                    holderView.tvUCID.setText("");
                    holderView.tvClientName.setText("");
                    holderView.tvError.setVisibility(View.GONE);
                    searchFoundFlag = false;
                } else if (name.equalsIgnoreCase(NO_RECORDS_FOUND)) {
                    holderView.ivDropDownArrow.setVisibility(View.GONE);
                    holderView.llExpand.setOnClickListener(null);
                    holderView.tvClientID.setText("");
                    holderView.tvUCID.setText("");
                    holderView.tvClientName.setText("");
                    holderView.tvError.setVisibility(View.VISIBLE);
                } else {
                    holderView.tvError.setVisibility(View.GONE);
                    holderView.llExpand.setVisibility(View.VISIBLE);
                }
            }

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults filterResults = new FilterResults();
                    try {
                        if (constraint != null) {
                            String SearchText;
                            int strLen = constraint.toString().trim().length();
                            if (strLen < 3) {
                                return filterResults;
                            }
                            if (URLEncoder.encode(constraint.toString(), "utf-8")
                                    .equalsIgnoreCase("+")) {
                                SearchText = "";
                            } else {
                                SearchText = constraint.toString().trim();
                            }
                            Map requestMap = new HashMap();
                            requestMap.put("SearchText", SearchText);
                            requestMap.put("SearchType", searchType);
                            requestMap.put("ViewAll", "false");
                            Map headerMap = new HashMap();
                            headerMap.put("Content-Type", "application/json; charset=UTF-8");
                            headerMap.put("token", AccountDetails.getToken());
                            headerMap.put("AppId", "2");
                            headerMap.put("userid", AccountDetails.getUserId());
                            headerMap.put("clientinfo", "android");
                            headerMap.put("ipaddress", CRMAppUtil.getLocalIpAddress());
                            headerMap.put("BusinessUnitId", AccountDetails.getBusinessUnitId());
                            headerMap.put("macaddress",
                                    CRMMOSLCommon.getDeviceID(context, context.getContentResolver()));
                            headerMap.put("EmployeeCode", AccountDetails.getEmployeeCode());
                            String serviceMethod = "Lead/SearchCommonMetaData";

                            if (MSFStatistics.isNetworkEnabled(getApplicationContext())) {
                                JSONObject response =
                                        WebServiceDirectCall.getData(serviceMethod, headerMap,
                                                requestMap);
                                ArrayList<SearchResultModel> modelList = new ArrayList<>();
                                if (response == null) {
                                    return null;
                                }
                                String status = response.getString(STATUS);
                                String leadOwner = "";
                                Integer oaoUserId = 0;
                                String sourceName = "";
                                String prospectSourceName = "";
                                String prospectSourceDetails = "";
                                String interestedIn = "";
                                String clientCode = "0";
                                if (status.equalsIgnoreCase("true")) {
                                    JSONObject objDetail = response.getJSONObject(RESPONSE);
                                    JSONArray jsonArray = objDetail.getJSONArray(DATA);
                                    if (jsonArray != null && jsonArray.length() != 0) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String metadata =
                                                    jsonObject.getString(METADATA) == null ? ""
                                                            : jsonObject.getString(METADATA);
                                            JSONObject jsonObjMetatata =
                                                    new JSONObject(metadata.trim());

                                            if (searchType.equalsIgnoreCase(SEARCH_TYPE_EMP)) {

                                                String empCode =
                                                        jsonObjMetatata.getString(EMPLOYEE_CODE) == null
                                                                ? ""
                                                                : jsonObjMetatata.getString(EMPLOYEE_CODE);
                                                String name =
                                                        jsonObjMetatata.getString(NAME) == null ? ""
                                                                : jsonObjMetatata.getString(NAME);
                                                String businessProfileName =
                                                        jsonObjMetatata.getString(BUSINESS_PROFILE)
                                                                == null ? "" : jsonObjMetatata.getString(
                                                                BUSINESS_PROFILE);
                                                String businessProfileId =
                                                        jsonObjMetatata.getString(PROFILE_ID) == null
                                                                ? ""
                                                                : jsonObjMetatata.getString(PROFILE_ID);
                                                String productId =
                                                        jsonObjMetatata.getString(PRODUCT_ID) == null
                                                                ? ""
                                                                : jsonObjMetatata.getString(PRODUCT_ID);

                                                if (jsonArray.length() == 1) {
                                                    modelList.add(
                                                            new SearchResultModel(searchType, empCode,
                                                                    name, businessProfileId,
                                                                    businessProfileName, productId));
                                                    modelList.add(
                                                            new SearchResultModel(searchType, "", "",
                                                                    "", "", ""));
                                                } else {
                                                    modelList.add(
                                                            new SearchResultModel(searchType, empCode,
                                                                    name, businessProfileId,
                                                                    businessProfileName, productId));
                                                    if (jsonArray.length() > 7
                                                            && i == (jsonArray.length() - 1)) {
                                                        modelList.add(
                                                                new SearchResultModel(searchType, "",
                                                                        "", "", "", ""));
                                                    }
                                                }

                                                // Assign the data to the FilterResults
                                                filterResults.values = modelList;
                                                filterResults.count = modelList.size();
                                            } else {
                                                String ucid =
                                                        jsonObjMetatata.getString(UCID) == null
                                                                || jsonObjMetatata.getString(UCID)
                                                                .equals("") ? "0"
                                                                : jsonObjMetatata.getString(UCID);
                                                String leadID =
                                                        jsonObjMetatata.getString(LEADID) == null ? ""
                                                                : jsonObjMetatata.getString(LEADID);
                                                String name =
                                                        jsonObjMetatata.getString(NAME) == null ? ""
                                                                : jsonObjMetatata.getString(NAME);
                                                String mobileNo =
                                                        jsonObjMetatata.getString(MOBILE) == null ? ""
                                                                : jsonObjMetatata.getString(MOBILE);
                                                String email =
                                                        jsonObjMetatata.getString(EMAIL) == null ? ""
                                                                : jsonObjMetatata.getString(EMAIL);
                                                // String clientCode = jsonObjMetatata.getString(CLIENTCODE) == null ? "" : jsonObjMetatata.getString(CLIENTCODE);

                                                if (jsonObjMetatata.has(CLIENTCODE)) {
                                                    clientCode =
                                                            jsonObjMetatata.getString(CLIENTCODE)
                                                                    == null || jsonObjMetatata.getString(
                                                                    CLIENTCODE).equals("") ? "0"
                                                                    : jsonObjMetatata.getString(CLIENTCODE);
                                                }
                                                if (jsonObjMetatata.has("SourceName")) {
                                                    sourceName =
                                                            jsonObjMetatata.getString("SourceName")
                                                                    == null ? ""
                                                                    : jsonObjMetatata.getString(
                                                                    "SourceName");
                                                }
                                                if (jsonObjMetatata.has("ProspectSourceName")) {
                                                    prospectSourceName = jsonObjMetatata.getString(
                                                            "ProspectSourceName") == null ? ""
                                                            : jsonObjMetatata.getString(
                                                            "ProspectSourceName");
                                                }
                                                if (jsonObjMetatata.has("ProspectSourceDetails")) {
                                                    prospectSourceDetails =
                                                            jsonObjMetatata.getString(
                                                                    "ProspectSourceDetails") == null ? ""
                                                                    : jsonObjMetatata.getString(
                                                                    "ProspectSourceDetails");
                                                }
                                                if (jsonObjMetatata.has("InterestedIn")) {
                                                    interestedIn =
                                                            jsonObjMetatata.getString("InterestedIn")
                                                                    == null ? ""
                                                                    : jsonObjMetatata.getString(
                                                                    "InterestedIn");
                                                }
                                                oaoUserId = jsonObjMetatata.has(OAOUSERID)
                                                        && !jsonObjMetatata.isNull(OAOUSERID)
                                                        ? jsonObjMetatata.getInt(OAOUSERID) : null;
                                                if (searchType.equalsIgnoreCase(SEARCH_TYPE_LEAD)) {
                                                    leadOwner =
                                                            jsonObjMetatata.getString(LEAD_OWNER)
                                                                    == null ? ""
                                                                    : jsonObjMetatata.getString(LEAD_OWNER);
                                                }

                                                if (jsonArray.length() == 1) {
                                                    modelList.add(
                                                            new SearchResultModel(searchType, leadID,
                                                                    name, mobileNo, email, ucid, leadOwner,
                                                                    oaoUserId, sourceName,
                                                                    prospectSourceName,
                                                                    prospectSourceDetails, interestedIn,
                                                                    clientCode));
                                                    modelList.add(
                                                            new SearchResultModel(searchType, "", "",
                                                                    "", "", "", "", null, "", "", "", "",
                                                                    ""));
                                                } else {
                                                    modelList.add(
                                                            new SearchResultModel(searchType, leadID,
                                                                    name, mobileNo, email, ucid, leadOwner,
                                                                    oaoUserId, sourceName,
                                                                    prospectSourceName,
                                                                    prospectSourceDetails, interestedIn,
                                                                    clientCode));
                                                    if (jsonArray.length() > 7
                                                            && i == (jsonArray.length() - 1)) {
                                                        modelList.add(
                                                                new SearchResultModel(searchType, "",
                                                                        "", "", "", "", "", null, "", "",
                                                                        "", "", ""));
                                                    }
                                                }

                                                // Assign the data to the FilterResults
                                                filterResults.values = modelList;
                                                filterResults.count = modelList.size();
                                            }
                                        }
                                    } else {
                                        modelList.add(
                                                new SearchResultModel("", "", NO_RECORDS_FOUND, "", "",
                                                        ""));
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();
                                    }
                                } else {
                                    if (URLEncoder.encode(constraint.toString(), "utf-8")
                                            .equalsIgnoreCase("+")) {
                                        searchFoundFlag = true;
                                        modelList.add(
                                                new SearchResultModel("", "", NO_RECORDS_FOUND, "", "",
                                                        ""));
                                        filterResults.values = modelList;
                                        filterResults.count = modelList.size();
                                    } else {
                                        String errorMsg =
                                                CRMMOSLCommon.getErrorMsgFromResponse(response);
                                        MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                                getResources().getString(R.string.motilal_oswal),
                                                errorMsg, "OK", true, null);
                                    }
                                }
                            } else {
                                MSFDialog.alertDialog(MOSLCommonActivity.this, 0,
                                        getResources().getString(R.string.motilal_oswal),
                                        INTERNET_CONNECTION, "OK", true, null);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        resultList = (List<SearchResultModel>) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }

        class HolderView {
            TextView tvClientName, tvClientID, tvUCID, tvError;
            ImageView ivDropDownArrow;
            LinearLayout llDropDownMenuLayout, llExpand;
            RelativeLayout rlSetAppointment, rlCrateMeeting, rlViewDetail, rlOpenAccount, rlOAOForm;

            public HolderView(View parent) {
                tvClientName = (TextView) parent.findViewById(R.id.tvClientname);
                tvClientID = (TextView) parent.findViewById(R.id.tvClientID);
                tvUCID = (TextView) parent.findViewById(R.id.tvUCID);
                ivDropDownArrow = (ImageView) parent.findViewById(R.id.ivDropDownArrow);
                llDropDownMenuLayout =
                        (LinearLayout) parent.findViewById(R.id.llDropDownMenuLayout);
                llExpand = (LinearLayout) parent.findViewById(R.id.llExpand);
                rlSetAppointment = (RelativeLayout) parent.findViewById(R.id.rlSetAppointment);
                rlCrateMeeting = (RelativeLayout) parent.findViewById(R.id.rlCrateMeeting);
                rlViewDetail = (RelativeLayout) parent.findViewById(R.id.rlViewDetail);
                rlOpenAccount = (RelativeLayout) parent.findViewById(R.id.rlOpenAccount);
                rlOAOForm = (RelativeLayout) parent.findViewById(R.id.rlOAOForm);
                tvError = (TextView) parent.findViewById(R.id.tvError);
            }
        }
    }
}
