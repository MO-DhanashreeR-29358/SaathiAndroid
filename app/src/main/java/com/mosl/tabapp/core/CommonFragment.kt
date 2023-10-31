package com.mosl.tabapp.core

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.mosl.app.core.data.MOSLResponseWrapper.SERVER_ERROR_OCCURRED
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.DiyLinkClickListner
import com.mosl.tabapp.ws.CRMWebServiceHandler
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.rxjava3.disposables.CompositeDisposable
import processors.coreProcessors.assetTracker.AssetTracker
import sdk.interfaces.IDistaSDK
import sdk.interfaces.OnTaskFailureListener
import sdk.interfaces.OnTaskSuccessListener
import utils.BaseApplication
import javax.inject.Inject

open class CommonFragment : BaseFragment() {
  @Inject
  internal lateinit var userManager: UserManagerImpl
  @Inject
  internal lateinit var navigator: ScreenNavigatorImpl
  val saathiApi = CRMWebServiceHandler.getSaathiApi()
  val compositeDisposable = CompositeDisposable();
  private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1004
  private val PERMISSIONS_REQUEST_ENABLE_GPS: Int = 1005
  private val BATTARY_PERMISSION_REQUEST_CODE: Int = 1006

  fun stringOrEmpty(input: String?): String {
    return stringOrDefault(input, "")
  }

  protected fun toggleSectionExpansion(
    parent: View,
    icon: TextView
  ) {
    if (parent.visibility == View.VISIBLE) {
      parent.visibility = View.GONE
      icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
    } else {
      parent.visibility = View.VISIBLE
      icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0)
    }
  }

  //openSectionExpansion
  protected fun openSectionExpansion(
    parent: View,
    icon: TextView
  ) {
    parent.visibility = View.VISIBLE
    icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0)
  }

  //closedSectionExpansion
  protected fun closedSectionExpansion(
    parent: View,
    icon: TextView
  ) {
    parent.visibility = View.GONE
    icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
  }

  protected fun setIcon(
    parent: View,
    icon: TextView
  ) {
    if (parent.visibility == View.VISIBLE) {
      icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_minus, 0)
    } else {
      icon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
    }
  }

  //Enable field
  protected fun enableField(layout: ViewGroup) {
    layout.isEnabled = true
    for (i in 0 until layout.childCount) {
      val child = layout.getChildAt(i)
      if (child is ViewGroup) {
        enableField(child)
      } else {
        child.isEnabled = true
      }
    }
  }

  protected fun disableField(layout: ViewGroup) {
    layout.isEnabled = false
    for (i in 0 until layout.childCount) {
      val child = layout.getChildAt(i)
      if (child is ViewGroup) {
        disableField(child)
      } else {
        child.isEnabled = false
      }
    }
  }

  protected fun enableDisableField(layout: ViewGroup, status: Boolean) {
    layout.isEnabled = status
    for (i in 0 until layout.childCount) {
      val child = layout.getChildAt(i)
      if (child is ViewGroup) {
        enableDisableField(child, status)
      } else {
        child.isEnabled = status
      }
    }
  }

  fun stringOrDefault(
    input: String?,
    defaultVal: String
  ): String {
    return input ?: defaultVal
  }

  fun stringOrZero(input: String?): String {
    return if (input != null && input != "" && input != "null") {
      input
    } else {
      "0"
    }
  }

  fun toggleProgress(show: Boolean) {
    if (show) {
      showProgress()
    } else {
      hideProgress()
    }
  }

  fun getErrorOnFailureNew(throwable: Throwable): String {
    return if (throwable is ServiceFailureException) {
      (throwable).errorMessage
    } else {
        SERVER_ERROR_OCCURRED;
    }
  }

    fun gotoEkycPage(
        ekycModule: EkycModule
    ) {
        (requireActivity() as CommonActivity).gotoEkycPage(ekycModule)
    }

    fun gotoEkycExpressPage(
        isExistingOrNewUser: Boolean, id: String, oaoUserId: String
    ) {
        (requireActivity() as CommonActivity).gotoEkycExpressPage(
            isExistingOrNewUser,
            id,
            oaoUserId
        )
    }

    fun renderDiyLink(
        leadId: String,
        callBack: DiyLinkClickListner
    ) {
        (requireActivity() as CommonActivity).renderDiyLink(leadId, callBack)
    }

    fun noInternetConnectionDialog() {
        MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION, "OK", true,
        null
    )
  }

  fun erroMsgDialog(message: String) {
    MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal), message, MOSLConstants.OK, true,
        null
    )
  }

  /* fun checkForAllPermissionsAndInitiateDista(mDistaSdk : IDistaSDK){
    // Check for background location permission
    if (!isGrantedBackgroundLocationPermission()) {
      // Display a warning dialog
      showPermissionWarningDialog();
    }else if(!isBatteryPermissionAllowed() ){
      askForBatteryPermission()
    }else{
      distaLoginApiCall(mDistaSdk)
    }
  }

 private fun distaLoginApiCall(mDistaSdk : IDistaSDK){
    if(mDistaSdk.isLoggedIn) {
      AssetTracker.initialize(BaseApplication.getBaseApplication())
    }else{
      mDistaSdk.doLoginCheckIn(
        userManager.getUser()?.employeeCode,
        object : OnTaskSuccessListener {
          override fun onSuccess() {
            // Toast.makeText(
            //    applicationContext,
            //    "User logged in successfully !",
            //   Toast.LENGTH_LONG
            //).show()
          }
        },
        object : OnTaskFailureListener {
          override fun onFailure(message: String?) {
            // Toast.makeText(
            //    applicationContext,
            //    message,
            //    Toast.LENGTH_LONG
            //).show()
          }
        })
    }

  }

  private fun askForBatteryPermission(){
    val packageName: String = requireContext().packageName
    val pm  = requireContext().getSystemService(DaggerAppCompatActivity.POWER_SERVICE) as PowerManager
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (pm.isIgnoringBatteryOptimizations(packageName)) {
        requireActivity().intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
      } else {
        requireActivity().intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        requireActivity().intent.data = Uri.parse("package:$packageName")
      }
      startActivityForResult(requireActivity().intent, BATTARY_PERMISSION_REQUEST_CODE)
    }
  }



  // added for back ground location permission as DISTA needed this permission
  private fun isGrantedBackgroundLocationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    } else {
      ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED &&
              ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
              ) == PackageManager.PERMISSION_GRANTED
    }
  }
  // added for back ground location permission as DISTA needed this permission
  private fun showPermissionWarningDialog() {
    com.mosl.shared.util.MSFDialog.alertDialog(
      requireContext(), 0, "Permission Required",
      "The app needs Location permission. Please grant Location permission, else you won't be able to use the app.\n\nFor granting location permission, choose Permissions -> Location -> Allow all the time to continue."
      , "OK", false
    ) { action, _ ->
      if (action === com.mosl.shared.util.MSFDialog.Action.OK) {
        requestBackgroundLocationPermission()
      }
    }
  }

  // added for back ground location permission as DISTA needed this permission
  private fun requestBackgroundLocationPermission() {
    // You can request the background location permission here
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      this@CommonFragment.requestPermissions( arrayOf(
          Manifest.permission.ACCESS_BACKGROUND_LOCATION,
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION
        ), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
      )
    }else{
      this@CommonFragment.requestPermissions( arrayOf(
          Manifest.permission.ACCESS_COARSE_LOCATION,
          Manifest.permission.ACCESS_FINE_LOCATION
        ), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
      )

    }
  }

  private fun isBatteryPermissionAllowed() : Boolean{
    if (ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
      )
      == PackageManager.PERMISSION_GRANTED){
      return true
    }else{
      return false
    }
  }

  // added for back ground location permission as DISTA needed this permission
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        if(!isBatteryPermissionAllowed() ){
          askForBatteryPermission()
        }
      }
    }

  }*/
}