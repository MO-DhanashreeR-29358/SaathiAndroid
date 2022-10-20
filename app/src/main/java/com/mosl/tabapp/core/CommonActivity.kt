package com.mosl.tabapp.core

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.mosl.annotations.FcmToken
import com.mosl.app.core.data.MOSLResponseWrapper
import com.mosl.app.core.data.fcm.FcmTokenSyncHelper
import com.mosl.app.core.data.util.HttpListenerFactory
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.prefs.BooleanPreference
import com.mosl.app.core.prefs.IntegerPreference
import com.mosl.app.core.prefs.StringPreference
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.base.ActivityDelegate
import com.mosl.app.coreui.base.BaseActivity
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.annotation.BranchCode
import com.mosl.mobile.user.annotation.PreferenceClear
import com.mosl.mobile.user.annotation.RemoteConfigData
import com.mosl.mobile.user.annotation.TwentyFourSevenCheck
import com.mosl.mobile.user.login.LoginRepositoryImpl
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.navigator.*
import com.mosl.shared.BranchSelectNeeded
import com.mosl.shared.util.MSFDialog
import com.mosl.shared.util.MSFDialog.Action
import com.mosl.tabapp.R
import com.mosl.tabapp.banner.BannerScreenData
import com.mosl.tabapp.branch.BranchActivity
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.data.RemoteConfigModel
import com.mosl.tabapp.databinding.ActivityCommonbaseBinding
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.helper.shortcuts.ShortcutHelper
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.service.ForegroundService
import com.mosl.tabapp.service.ForegroundService.LocalBinder
import com.mosl.tabapp.service.location.DataSyncWorker
import com.mosl.tabapp.service.location.LocationWorker
import com.mosl.tabapp.splash.SplashActivity
import com.mosl.tabapp.ui.quickdialog.ActionItem
import com.mosl.tabapp.ui.quickdialog.QuickAction
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.*
import com.mosl.tabapp.webview.WebviewFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import mosl.rx_preferences.Preference
import sdk.DistaSDK
import sdk.interfaces.IDistaSDK
import sdk.interfaces.OnTaskFailureListener
import sdk.interfaces.OnTaskSuccessListener
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MINUTES
import javax.inject.Inject
import kotlin.collections.set

class CommonActivity : BaseActivity(),
    Navigator,
    ActivityDelegate,
    QuickAction.OnActionItemClickListener,
    ConvertToEkyc, MsgDialog {

    lateinit var binding: ActivityCommonbaseBinding

    @Inject
    lateinit var activityNavHelper: ActivityNavigationHelper

    @Inject
    lateinit var navigator: EkycNavigator

    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var loginRepository: LoginRepositoryImpl

    @Inject
    lateinit var fragmentResolver: FragmentResolver
    var globalTitles = HashMap<String, AppTitle>()

    @Inject
    lateinit var screenNavigator: ScreenNavigatorImpl

    @Inject
    lateinit var navigationHelper: NavigationHelper

    @Inject
    @field:FcmToken
    lateinit var tokenPref: Preference<String>

    @field:TwentyFourSevenCheck
    @Inject
    lateinit var twentyFourSevenCheck: BooleanPreference

    @Inject
    @field:PreferenceClear
    lateinit var forcePreference: IntegerPreference

    @Inject
    lateinit var fcmTokenSyncHelper: FcmTokenSyncHelper

    @Inject
    lateinit var shortcutHelper: ShortcutHelper
    private var searchType = SEARCH_TYPE_LEAD

    @Inject
    lateinit var eventListenerFactory: HttpListenerFactory
    private lateinit var locationWorkManager: WorkManager
    private lateinit var dataSyncWorkManager: WorkManager
    private lateinit var compressionLocationWorkId: UUID
    private lateinit var compressionDataSyncWorkId: UUID
    internal var snackbar: Snackbar? = null
    private val REQUEST_CODE_CUSTOM_PERMISSION = 1153
    @Inject
    @field:RemoteConfigData
    lateinit var remoteCongigData: StringPreference

    @Inject
    lateinit var gson: Gson

    @Inject
    @field:BranchCode
    lateinit var branchCodePreference: StringPreference
    lateinit var remoteConfigModel: RemoteConfigModel
    var mService: ForegroundService? = null

    /**/
    /* lateinit var sharedPreferences: SharedPreferences
     var editor: SharedPreferences.Editor? = null
     var twentyFourSevenCheck = false*/
    /**/

    private lateinit var mDistaSdk : IDistaSDK

    override fun getLayout(): Int {
        return R.layout.activity_commonbase
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder
        ) {
            val binder = service as LocalBinder
            mService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonbaseBinding.bind(findViewById(R.id.drawerLayout))
        binding.actionBar.setHomeAction(OnClickListener { openMenuDrawer() })
        binding.actionBar.setNotification(OnClickListener {
            screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_NOTIFICATION.toString(), true)
        })
        binding.actionBar.srchImg.setOnClickListener { v -> openSearchOptions(v) }
        binding.actionBar.autoCompleteAdapter.setScreenNavigator(screenNavigator)
        binding.actionBar.autoCompleteAdapter.setConvertToEkycListener(this)
        binding.actionBar.autoCompleteAdapter.setErrorMsgDialogListener(this)
        hideAppTitle()
        setupMenuDrawer()
        if (savedInstanceState == null) {
            activityNavHelper.loadIntent(intent)
        } else {
            globalTitles =
                savedInstanceState.getSerializable(PAGE_TITLES) as HashMap<String, AppTitle>
        }

        /*sharedPreferences =
            getSharedPreferences(getString(R.string.preff_24_7_key), Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        twentyFourSevenCheck = sharedPreferences.getBoolean(TWENTY_FOUR_SEVEN_CHECK, false)*/

        locationWorkManager = WorkManager.getInstance(this)
        dataSyncWorkManager = WorkManager.getInstance(this)

        /*userManager.getUser()?.apply {
            if (!this.isAufsb()) {
                if (twentyFourSevenCheck.get()) {
                    startService(Intent(this@CommonActivity, ForegroundService::class.java))
                }
                // startWorkManager()
            }
        }*/
        if (twentyFourSevenCheck.get()) {
            startService(Intent(this@CommonActivity, ForegroundService::class.java))
        }
        setupObservables()
        setBanner()
        if (branchCodePreference.get().isNotEmpty()) {
            menuTopSection(branchCodePreference.get())
        } else {
            branchCodePreference.set(userManager.user.defaultBusinessProfile.branchCode)
            menuTopSection(userManager.user.defaultBusinessProfile.branchCode)
        }

        mDistaSdk = DistaSDK.getInstance()

        //ActivityCompat.requestPermissions(
         //   this, arrayOf(android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.CALL_PHONE,android.Manifest.permission.ACCESS_FINE_LOCATION),
         //   REQUEST_CODE_CUSTOM_PERMISSION
        //)

        mDistaSdk.initOrganization( "MOSL",
            object : OnTaskSuccessListener {
                override fun onSuccess() {
                    Toast.makeText(
                        applicationContext,
                        "initialize organization successfully !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            object : OnTaskFailureListener {
                override fun onFailure(message: String?) {
                    Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })


            mDistaSdk.doLoginCheckIn(
                userManager.getUser()?.employeeCode,
                object : OnTaskSuccessListener {
                    override fun onSuccess() {
                        Toast.makeText(
                            applicationContext,
                            "User logged in successfully !",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                object : OnTaskFailureListener {
                    override fun onFailure(message: String?) {
                        Toast.makeText(
                            applicationContext,
                            message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })




    }


    private fun isForegroundServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun stopService() {
        val serviceIntent = Intent(this, ForegroundService::class.java)
        stopService(serviceIntent)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode == com.mosl.ekycexpress.core.meta.constants.LoginSuccess) {
            val intent = Intent(this, com.mosl.ekycexpress.core.CommonActivity::class.java)
            intent.putExtra(CommonActivity.IS_LOGGED_IN_SUCESS, true)
            startActivity(intent)
        }
        if (resultCode == BranchSelectNeeded) {
            menuTopSection(data?.getStringExtra(BranchActivity.BRANCHCODE))
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initBackgroundState() {
        val intent = Intent()
        val manufacturer = android.os.Build.MANUFACTURER
        when (manufacturer) {
            "xiaomi" ->
                intent.component =
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
            "oppo" ->
                intent.component =
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros.safecenter.permission.startup.StartupAppListActivity"
                    )
            "vivo" ->
                intent.component =
                    ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"
                    )
            "asus" ->
                intent.component =
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus.mobilemanager.MainActivity"
                    )
            "samsung" ->
                intent.component =
                    ComponentName(
                        "com.samsung.android.lool",
                        "com.samsung.android.sm.ui.battery.BatteryActivity"
                    )

        }
        val list =
            getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (list.size > 0) {
            startActivity(intent)
        }
    }

    /*override fun onStart() {
        super.onStart()
        userManager.getUser()?.apply {
            if (!this.isAufsb()) {
                bindService(
                    Intent(this@CommonActivity, ForegroundService::class.java), mServiceConnection,
                    Context.BIND_AUTO_CREATE
                )
            }
        }
    }*/
    private fun menuTopSection(branchCode: String?) {

        binding.drawerMenu.refreshHeader(userManager, branchCode)
    }

    override fun onResume() {
        super.onResume()
        if (twentyFourSevenCheck.get() && !isForegroundServiceRunning(ForegroundService::class.java)) {
            bindService(
                Intent(this, ForegroundService::class.java),
                mServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        }
    }

    /* override fun onStop() {
         super.onStop()
         userManager.getUser()?.apply {
             if (!this.isAufsb()) {
                 unbindService(mServiceConnection)
             }
         }
     }*/

    private fun setBanner() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(100)
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val saathiInit =
                        remoteConfig.getString(getString(R.string.saathi_init))
                    if (saathiInit != null && !saathiInit.isEmpty()) {
                        remoteConfigModel =
                            gson.fromJson(saathiInit, RemoteConfigModel::class.java)

                        userManager.getUser()
                            ?.apply {
                                if (remoteConfigModel.isConfigured && !this.isAufsb() && !this.isAuBankSalesSupport()) {
                                    openBanner(remoteConfigModel)
                                }
                            }
                        remoteCongigData.set(saathiInit)

                    }
                }
            }

    }

    private fun openBanner(remoteConfigModel: RemoteConfigModel) {

        val bannerScreenData: BannerScreenData = mapToBannerScreenData(remoteConfigModel)

        screenNavigator.openBannerDialog(bannerScreenData)

    }

    private fun mapToBannerScreenData(banner: RemoteConfigModel): BannerScreenData {
        return BannerScreenData(
            banner.isConfigured, banner.imageUrl, banner.actionUrl, banner.target

        )
    }

    //quick dialog
    private fun openSearchOptions(v: View) {
        val acLead = ActionItem(ID_LEAD, "Lead", getChecedStateIcon(SEARCH_TYPE_LEAD), this)
        val acCleint = ActionItem(ID_CLEINT, "Client", getChecedStateIcon(SEARCH_TYPE_CLIENT), this)
        val acDadhBoard =
            ActionItem(ID_EMP, "Dashboard for", getChecedStateIcon(SEARCH_TYPE_EMP), this)

        val quickAction = QuickAction(this, QuickAction.VERTICAL)
        quickAction.addActionItem(acLead)
        quickAction.addActionItem(acCleint)
        userManager.getUser()
            ?.apply {
                if (!this.isAufsb()) {
                    quickAction.addActionItem(acDadhBoard)
                }
            }
        quickAction.setOnActionItemClickListener(this)
        quickAction.show(v)
    }

    private fun getChecedStateIcon(data: String): Int {
        return if (searchType.equals(data, ignoreCase = true)) {
            R.drawable.ic_check_black
        } else {
            R.drawable.ic_check_blank
        }
    }

    //search option listen
    override fun onItemClick(
        source: QuickAction,
        pos: Int,
        actionId: Int
    ) {

        when (actionId) {
            ID_LEAD -> {
                searchType = SEARCH_TYPE_LEAD
                binding.actionBar.setSearchHint(SEARCH_TYPE_LEAD_HINT)
            }
            ID_CLEINT -> {
                searchType = SEARCH_TYPE_CLIENT
                binding.actionBar.setSearchHint(SEARCH_TYPE_CLIENT_HINT)
            }
            ID_EMP -> {
                searchType = SEARCH_TYPE_EMP
                binding.actionBar.setSearchHint(SEARCH_TYPE_DASHBOARD_HINT)
            }
        }
    }

    private fun networkConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    }

    private fun startWorkManager() {
        val periodicDataSyncWork =
            PeriodicWorkRequestBuilder<DataSyncWorker>(
                32,
                MINUTES
            ).setConstraints(networkConstraints())
                .build()

        val periodicLocationWork = PeriodicWorkRequestBuilder<LocationWorker>(
            16,
            MINUTES
        ).build()

        locationWorkManager.enqueueUniquePeriodicWork(
            "MyLocationWork",
            ExistingPeriodicWorkPolicy.KEEP, periodicLocationWork
        )
        dataSyncWorkManager.enqueueUniquePeriodicWork(
            "MyDataWork",
            ExistingPeriodicWorkPolicy.KEEP, periodicDataSyncWork
        )

        compressionDataSyncWorkId = periodicDataSyncWork.id
        compressionLocationWorkId = periodicLocationWork.id
    }

    private fun setupObservables() {
        val mainThread = dispatchers.main
        disposable.add(
            tokenPref.asObservable()
                .distinctUntilChanged()
                .map { tokenPref.get() }
                .filter { token -> token != "0" }
                .switchMap { token ->
                    fcmTokenSyncHelper.saveToken(token)
                        .compose(applyUiModel())
                }
                .subscribe())

        val Threshold = TimeUnit.SECONDS.toNanos(10)
        eventListenerFactory.getApiStatsObservable()
            .filter { callTime -> callTime > Threshold }
            .observeOn(mainThread)
            .subscribe {
                val snackbar1 = snackbar
                if (snackbar1 == null || snackbar1.isShown) {
                    snackbar = showConnectivityIssueSnackbar()
                }
            }
            .addTo(disposable)
    }

    private fun openMenuDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setupMenuDrawer() {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(
                drawerView: View,
                slideOffset: Float
            ) {
            }

            override fun onDrawerOpened(drawerView: View) {}

            @SuppressLint("SwitchIntDef")
            override fun onDrawerClosed(drawerView: View) {
                activityNavHelper.handlePendingMenuAction(this@CommonActivity)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }
        })
        binding.drawerMenu.onMenuClicked()
            .subscribe { menu ->
                if (menu.pageId == NavigationConstants.NAVIGATE_TO_LOGOUT) {
                    logoutCustomer()
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_FRESH_REG) {
                    gotoEkycExpressPage(false, WebviewFragment.FRESH_REGISTRATION_ID)
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_EXISTING_APP) {
                    gotoEkycExpressPage(true, WebviewFragment.EXISTING_APPLICATIONS_ID)
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_PENDING_ESIGN) {
                    navigator.openEkyc()
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_E_MODIFICATION) {
                    gotoEkycExpressPage(true, WebviewFragment.E_MODIFICATION_ID)
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_SETTING) {
                    screenNavigator.openSettingActivity()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_LAS_NEW_APPLICATION) {
                    gotoEkycExpressPage(true, WebviewFragment.NEW_LAS_APPLICATION_ID)
                    binding.drawerLayout.closeDrawers()
                } else if (menu.pageId == NavigationConstants.NAVIGATE_TO_LAS_RESUME_APPLICATION) {
                    gotoEkycExpressPage(true, WebviewFragment.RESUME_LAS_APPLICATION_ID)
                    binding.drawerLayout.closeDrawers()
                }

                activityNavHelper.onMenuClicked(menu)

                binding.drawerMenu.selectItem(menu)
                binding.drawerLayout.closeDrawers()
            }
            .addTo(disposable)

        binding.drawerMenu.onCloseClicked()
            .subscribe { _ ->
                binding.drawerLayout.closeDrawers()
            }
            .addTo(disposable)
    }

    private fun logoutCustomer() {
        MSFDialog.alertDialog(
            this@CommonActivity, 0, getString(R.string.alert_dialog_title),
            getString(R.string.MOSL_MENU_LOGOUT_TXT), "Yes", "No", true
        ) { action, _ ->
            if (action === Action.OK) {

                doLogout()
            }
        }
    }

    private fun doLogout() {
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.cancelAll()
        disposable.add(
            loginRepository.logout()
                .toSingleDefault(true)
                .toObservable()
                .compose(applyUiModel())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { uiModel ->
                    toggleProgressBar(uiModel.inProgress)

                    uiModel.onSuccess {
                        Timber.d("Logout Successfully")
                        forcePreference.set(100)
                        stopService()
                        twentyFourSevenCheck.set(false)
                        distaLogoutCheckout()
                        /*editor?.putBoolean(TWENTY_FOUR_SEVEN_CHECK, false)
                        editor?.commit()*/
                        openLogin()
                    }

                    uiModel.onFailure {
                        showOnFailurePopup(it)
                    }
                })
    }

    private fun openLogin() {
        //open image slider instead of splash

//  locationWorkManager.cancelWorkById(compressionLocationWorkId)
//  dataSyncWorkManager.cancelWorkById(compressionDataSyncWorkId)
        shortcutHelper.setLogoutUserShortcuts()
        val login = Intent(this, SplashActivity::class.java)
        startActivity(login)
        finish()
    }

    fun toggleProgressBar(show: Boolean) {
        if (show) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    fun showOnFailurePopup(t: Throwable) {
        val message: String = if (t is ServiceFailureException) {
            t.errorMessage
        } else {
            MOSLResponseWrapper.SERVER_ERROR_OCCURRED
        }
        MSFDialog.alertDialog(
            applicationContext, 0,
            resources.getString(R.string.app_name),
            message, "OK", true, null
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PAGE_TITLES, globalTitles)
    }

    override fun authorizationDialog(listener: (AuthorizationState) -> Unit) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun clearTitles() {
        globalTitles.clear()
    }

    override fun getContainerViewId(): Int {
        return R.id.activityFrameLayout
    }

    override fun getFragment(pageId: String): Fragment? {
        return fragmentResolver.resolve(pageId)
    }

    override fun gotoPage(fragmentEntry: FragmentNavigationEntry) {
        fragTransitionHelper.navigateTo(fragmentEntry)
    }

    override fun hideAppTitle() {
        binding.actionBar.setTitleAndOverflow(AppTitle.EMPTY)
    }

    override fun hideProgress() {
        binding.progressBar.visibility = View.GONE
    }

    override fun isConnected(): Boolean {
        return true
    }

    override fun setAppTitle(
        page: String,
        appTitle: AppTitle?
    ) {
        Timber.d("ActionBarNew Set Title for %s, to %s", page, appTitle)
        val pageTitle: AppTitle = appTitle ?: globalTitles[page] ?: AppTitle.EMPTY

        globalTitles[page] = pageTitle
        binding.actionBar.setTitleAndOverflow(pageTitle)
    }

    override fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideOverflowMenu() {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectMenu(label: Int) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun setOverflowClickListener(listener: OnClickListener?) {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun showOverflowMenu() {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
            return
        }

        if (binding.progressBar.visibility == View.VISIBLE) {
            binding.progressBar.visibility = View.GONE
            return
        }

        if (navigationHelper.onBackPressed()) {
            return
        }

        if (navigationHelper.canGoBack()) {
            screenNavigator.goBack()
            return
        }
        exitPopup()
    }

    internal fun showConnectivityIssueSnackbar(): Snackbar {
        val snackbar =
            Snackbar.make(binding.rootCoordinator, R.string.network_connection_slow, 10000)
        snackbar.setAction(R.string.button_label_ok) { snackbar.dismiss() }
        snackbar.show()
        return snackbar
    }

    override fun gotoEkycPage(ekycModule: EkycModule) {
        Timber.d("ekyc module %s", ekycModule.toString())
        activityNavHelper.convertEkycApi(this, ekycModule)
    }

    override fun gotoEkycExpressPage(
        isExistingOrNewUser: Boolean,
        id: String,
        oaoUserID: String
    ) {
        if (userManager.user.isUseUrlEnable() || id == WebviewFragment.E_MODIFICATION_ID || id == WebviewFragment.NEW_LAS_APPLICATION_ID || id == WebviewFragment.RESUME_LAS_APPLICATION_ID) {
            activityNavHelper.gotoEkycExpressPage(this, id, oaoUserID, branchCodePreference.get())
        } else {
            activityNavHelper.openEkycApp(isExistingOrNewUser, oaoUserID)
        }
    }

    override fun renderDiyLink(
        leadId: String,
        callBack: DiyLinkClickListner?
    ) {
        activityNavHelper.getDiyLink(this, leadId, callBack)
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        globalTitles.clear()
        disposable.clear()
    }

    private fun exitPopup() {
        MSFDialog.alertDialog(
            this, 0, getString(R.string.motilal_oswal),
            "Do you want to exit the application?", "Yes",
            "No", true
        ) { action, _ ->
            if (action == Action.OK) {
                finish()
            }
        }
    }

    private fun distaLogoutCheckout(){
        mDistaSdk.logout(
            object : OnTaskSuccessListener {
                override fun onSuccess() {
                    Toast.makeText(
                        applicationContext,
                        "User logout successfully !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            object : OnTaskFailureListener {
                override fun onFailure(message: String?) {
                    Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        mDistaSdk.checkout(
            object : OnTaskSuccessListener {
                override fun onSuccess() {
                    Toast.makeText(
                        applicationContext,
                        "User check out successfully !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            object : OnTaskFailureListener {
                override fun onFailure(message: String?) {
                    Toast.makeText(
                        applicationContext,
                        message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    override fun erroMsgDialog(msg: String) {
        com.mosl.tabapp.ui.MSFDialog.alertDialog(
            this, 0,
            resources.getString(R.string.motilal_oswal), msg, MOSLConstants.OK, true,
            null
        )
    }

    companion object {
        const val IS_LOGGED_IN_SUCESS = "isLoggedIn"
        const val PAGE_TITLES = "pageTitles"
        const val ARG_PAGE_ID = "pageId"
        const val SEARCH_TYPE_LEAD = "Lead"
        const val SEARCH_TYPE_CLIENT = "Client"
        const val SEARCH_TYPE_EMP = "Employee"
        const val SEARCH_TYPE_LEAD_HINT = "Search Lead"
        const val SEARCH_TYPE_CLIENT_HINT = "Search Client"
        const val SEARCH_TYPE_DASHBOARD_HINT = "Dashboard for"
        const val ID_LEAD = 1
        const val ID_CLEINT = 2
        const val ID_EMP = 3
    }
}