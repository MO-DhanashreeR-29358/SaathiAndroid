package com.mosl.tabapp.core

import android.widget.Toast
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ws.CRMWebServiceHandler
import javax.inject.Inject

open class CommonFragment : BaseFragment() {
  @Inject
  internal lateinit var userManager: UserManager
  @Inject
  internal lateinit var navigator: ScreenNavigatorImpl
  val saathiApi = CRMWebServiceHandler.getSaathiApi()

  protected fun showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
  }

  fun stringOrEmpty(input: String?): String {
    return stringOrDefault(input, "")
  }

  fun stringOrDefault(
    input: String?,
    defaultVal: String
  ): String {
    return input ?: defaultVal
  }

  fun stringOrZero(input: String?): String {
    return if (input != null && input != "") {
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

  fun noInternetConnectionDialog() {
    MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION, "OK", true,
        null
    )
  }
}