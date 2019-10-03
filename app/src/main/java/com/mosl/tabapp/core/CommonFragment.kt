package com.mosl.tabapp.core

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.mosl.app.core.data.MOSLResponseWrapper.SERVER_ERROR_OCCURRED
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.coreui.base.BaseFragment
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ws.CRMWebServiceHandler
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class CommonFragment : BaseFragment() {
  @Inject
  internal lateinit var userManager: UserManager
  @Inject
  internal lateinit var navigator: ScreenNavigatorImpl
  val saathiApi = CRMWebServiceHandler.getSaathiApi()
  val compositeDisposable = CompositeDisposable();
  protected fun showToast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
  }

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

  fun noInternetConnectionDialog() {
    MSFDialog.alertDialog(
        requireContext(), 0,
        resources.getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION, "OK", true,
        null
    )
  }
}