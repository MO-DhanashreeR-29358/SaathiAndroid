package com.mosl.tabapp.login

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.editorActions
import com.mosl.app.core.error.getErrorOnFailure
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.base.BaseActivity
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.LoginRepositoryImpl
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.navigator.AppTitle
import com.mosl.navigator.AuthorizationState
import com.mosl.navigator.FragmentNavigationEntry
import com.mosl.navigator.Navigator
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.shared.util.isEmulator
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.common.FusedLocation.LatLongDispatcher
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.databinding.ActivityLoginNewBinding
import com.mosl.tabapp.helper.FirebaseHelper
import com.mosl.tabapp.helper.shortcuts.ShortcutHelper
import com.mosl.tabapp.permission.PermissionCheckHelper
import com.mosl.validator.exception.ValidationException
import dagger.android.AndroidInjection
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject


class LoginActivity : BaseActivity(), Navigator, LoginView, LatLongDispatcher {

  private val PERMISSIONS_REQUEST_ENABLE_GPS: Int = 1005
  private val ERROR_DIALOG_REQUEST: Int = 203
  private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1004
  var mLocationPermissionGranted = false
  lateinit var binding: ActivityLoginNewBinding

  @Inject
  lateinit var presenter: LoginPresenter

  @Inject
  lateinit var userManager: UserManager

  @Inject
  lateinit var shortcutHelper: ShortcutHelper

  @Inject
  lateinit var permissionCheckHelper: PermissionCheckHelper

  @Inject
  lateinit var fusedLocation: FusedLocation

  lateinit var sharedPreferences: SharedPreferences
  lateinit var editor: SharedPreferences.Editor
  private lateinit var digitalSalesKitParentList : List<DigitalSalesKitData>

  @Inject
  lateinit var firebaseHelper: FirebaseHelper

  lateinit var disposables: CompositeDisposable
  @Inject
  lateinit var loginRepository: LoginRepositoryImpl
  override fun getLayout(): Int {
    return R.layout.activity_login_new
  }

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
    disposables = CompositeDisposable()
    AndroidInjection.inject(this)
    setUpView()
    fusedLocation.initDispatcher(this)
    binding = ActivityLoginNewBinding.bind(findViewById(R.id.login_root))
    presenter.start()
  }

  private fun setUpView() {
    sharedPreferences = this.getSharedPreferences(
        resources.getString(R.string.consent_check),
        MODE_PRIVATE
    )
    editor = sharedPreferences.edit()

    if (!sharedPreferences.getBoolean(getString(R.string.consent_value), false)) {
      showDiscloserDilog()
    }
  }

  override fun onLoginClicked(): Observable<Unit> {
    return binding.btnLogin.clicks()
  }

  override fun onDoneClicked(): Observable<Int> {
    return binding.edPassword.editorActions { event -> event == EditorInfo.IME_ACTION_DONE }
  }

  override fun getEmpCode(): String {
    return binding.edEmpCode.text?.trim()
        .toString()
  }

  override fun getPassword(): String {
    return binding.edPassword.text.toString()
  }

  override fun renderLogin(uiModel: UiModel<User>) {
    toggleProgressBar(uiModel.inProgress)
//    Timber.d("User cache %s", userManager.user)

    uiModel.onFailure { error ->
      if (error is ValidationException) {
        error.error.let { alert(getString(it.error!!)) }
      } else {
        error.showOnFailurePopup()
      }
    }
  }

  private fun showDiscloserDilog() {
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setTitle(resources.getString(R.string.alert_dialog_title))
    alertDialog.setMessage(resources.getString(R.string.alert_dialog_discloser_message))
    alertDialog.setCancelable(false)
    alertDialog.setPositiveButton("GOT IT") { dialog: DialogInterface, which: Int ->
      dialog.dismiss()
      showConsentDialog()
    }
    alertDialog.show()
  }

  private fun showConsentDialog() {
    val lp = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    lp.setMargins(10, 0, 10, 0)
    val message = TextView(this)
    val s = SpannableString(this.getText(R.string.alert_dialog_location_message))
    Linkify.addLinks(s, Linkify.WEB_URLS)
    message.text = s
    message.setTextColor(resources.getColor(R.color.black))
    message.textSize = 18f
    message.setPadding(26, 10, 10, 0)
    message.movementMethod = LinkMovementMethod.getInstance()
    message.layoutParams = lp
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setTitle(resources.getString(R.string.alert_dialog_title))
    //        alertDialog.setMessage(getResources().getString(R.string.alert_dialog_location_message));
    alertDialog.setCancelable(false)
    alertDialog.setPositiveButton("I Agree") { dialog: DialogInterface, which: Int ->
      editor.putBoolean(resources.getString(R.string.consent_value), true)
      editor.commit()
      dialog.dismiss()
      if (VERSION.SDK_INT >= VERSION_CODES.M) {
        if(VERSION.SDK_INT > VERSION_CODES.Q) {
            if(checkMapServices()) {
                if(mLocationPermissionGranted) {
                    //Toast.makeText(this@LoginActivity, "Location permitted", Toast.LENGTH_SHORT).show()
                } else {
                    getLocationPermission()
                }
            }
        }
        permissionCheckHelper.requestAllPermissions()
      }
    }
    alertDialog.setNegativeButton(
        "Cancel"
    ) { dialog: DialogInterface?, which: Int ->
      Toast.makeText(this, getString(R.string.terms_condition_msg), Toast.LENGTH_LONG)
          .show()
      finish()
    }
    alertDialog.setView(message)
    alertDialog.create()
    alertDialog.show()
  }

  private fun checkMapServices(): Boolean {
    if (isServicesOK()) {
      if (isMapsEnabled()) {
        return true
      }
    }
    return false
  }

  private fun buildAlertMessageNoGps() {
    val builder = AlertDialog.Builder(this)
    builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
      .setCancelable(false)
      .setPositiveButton("Yes") { dialog, id ->
        val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS)
      }
    val alert = builder.create()
    alert.show()
  }

  fun isMapsEnabled(): Boolean {
    val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
      buildAlertMessageNoGps()
      return false
    }
    return true
  }

  private fun getLocationPermission() {
    /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
    if (ContextCompat.checkSelfPermission(
        this.applicationContext,
        Manifest.permission.ACCESS_FINE_LOCATION
      )
      === PackageManager.PERMISSION_GRANTED
    ) {
      mLocationPermissionGranted = true
    } else {
      ActivityCompat.requestPermissions(
        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
      )
    }
  }

  fun isServicesOK(): Boolean {
    Log.d("TAG", "isServicesOK: checking google services version")
    val available =
      GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@LoginActivity)
    if (available == ConnectionResult.SUCCESS) {
      //everything is fine and the user can make map requests
      Log.d("TAG", "isServicesOK: Google Play Services is working")
      return true
    } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
      //an error occured but we can resolve it
      Log.d("TAG", "isServicesOK: an error occured but we can fix it")
      val dialog: Dialog = GoogleApiAvailability.getInstance()
        .getErrorDialog(this@LoginActivity, available, ERROR_DIALOG_REQUEST)
      dialog.show()
    } else {
      Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show()
    }
    return false
  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    mLocationPermissionGranted = false
    when (requestCode) {
      PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

        // If request is cancelled, the result arrays are empty.
        if (grantResults.size > 0
          && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
          mLocationPermissionGranted = true
        }
      }
    }
  }


  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    Log.d("TAG", "onActivityResult: called.")
    when (requestCode) {
      PERMISSIONS_REQUEST_ENABLE_GPS -> {
        if (mLocationPermissionGranted) {

        } else {
          getLocationPermission()
        }
      }
    }
  }

  override fun setUserProperty(user: User?) {
    user?.apply {
      if (!isEmulator()) {
        firebaseHelper.setUserProperty(user.employeeCode)
      }
    }
  }

  override fun renderReportsData(uiModel: UiModel<List<ReportType>>) {
    uiModel.onSuccess {
      if (it.isNotEmpty()) {
        for (reportTypeResponse in it) {
          if (reportTypeResponse.isPreDefinedReports()) {
            val childMenuList = reportTypeResponse.childMenuList
            for (childMenu in childMenuList) {
              if (childMenu.isBasicReports()) {
                userManager.setReportMenuList(childMenu.ChildMenuList)
                fetchDigitalSalesKitParentMenu()
              }
            }
          }
        }
      }
    }
    uiModel.onFailure {
      openCommonActivity()
    }
  }

  private fun fetchDigitalSalesKitParentMenu() {
    disposables.add(
      loginRepository.fetchDigitalSalesKitParentMenu().compose(applyUiModel())
        .subscribe {
          renderDigitalSalesKitParentData(it)
        }
    )
  }

  private fun renderDigitalSalesKitParentData(uiModel: UiModel<List<DigitalSalesKitData>>) {
    uiModel.onSuccess {
      digitalSalesKitParentList = it.filter { data -> !data.parentId.equals("0") }
      loginRepository.fetchDigitalSalesKitChildMenu().compose(applyUiModel())
        .subscribe {
          renderDigitalSalesKitChildData(it)
        }
    }
    uiModel.onFailure {
      openCommonActivity()
    }
  }

  private fun renderDigitalSalesKitChildData(uiModel: UiModel<List<DigitalSalesKitData>>) {
    uiModel.onSuccess { data->
      digitalSalesKitParentList.map{
        it.menuList = data.filter { objoption ->
          objoption?.parentId.equals(it?.id.toString())
        }.map{
          DigitalSalesKitData(
            id = it.id,
            parentId = it.parentId,
            systemName = it.systemName,
            pdfPath = it.pdfPath
          )
        }
      }
      userManager.setDigitalSalesKitMenuList(digitalSalesKitParentList)

      openCommonActivity()
    }
    uiModel.onFailure {
      openCommonActivity()
    }
  }

  private fun openCommonActivity() {
    shortcutHelper.setLoggedInUserShortcuts()
    val intent = Intent(this, CommonActivity::class.java)
    startActivity(intent)
    finish()
  }

  fun Throwable.showOnFailurePopup() {
    val errorOnFailure = this.getErrorOnFailure()
    alert(message = errorOnFailure.getResolvedMessage(applicationContext))
  }

  fun toggleProgressBar(inProgress: Boolean) {
    if (inProgress) {
      showProgress()
    } else {
      hideProgress()
    }
  }

  override fun isDataEmpty(): Boolean {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun onDataAction(): Observable<DataAction> {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun authorizationDialog(listener: (AuthorizationState) -> Unit) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearTitles() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun getContainerViewId(): Int {
    return 0
  }

  override fun getFragment(pageId: String): Fragment? {
    TODO("Not yet implemented")
  }

  override fun gotoPage(fragmentEntry: FragmentNavigationEntry) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun hideAppTitle() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun hideProgress() {
    binding.progressBar.visibility = View.GONE
  }

  override fun setAppTitle(
    page: String,
    appTitle: AppTitle?
  ) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun showProgress() {
    binding.progressBar.visibility = View.VISIBLE
  }

  override fun isConnected(): Boolean {
    return true
  }

  override fun onLatLongChanged(location: Location?) {
  }
}