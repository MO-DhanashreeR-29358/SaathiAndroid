package com.mosl.tabapp.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mosl.annotations.ActivityScoped
import com.mosl.app.core.data.MOSLResponseWrapper
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.network.isNetworkConnected
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.navigator.BaseNavigator
import com.mosl.navigator.NavigationRequest
import com.mosl.navigator.Navigator
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.prospect.ProspectResponse
import com.mosl.saathi.api.model.prospect.SuggestMeRequest
import com.mosl.shared.util.Utility
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonActivity.Companion.ARG_PAGE_ID
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.helper.shortcuts.ShortcutConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.ws.CRMWebServiceHandler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class ActivityNavigationHelper @Inject constructor(
  internal val screenNavigator: BaseNavigator,
  internal val ekycNavigator: EkycNavigator,
  internal val userManager: UserManagerImpl,
  val context: Context,
  val navigator: Navigator
) {
  private var pendingMenuAction: AppMenu? = null
  private val disposable: CompositeDisposable = CompositeDisposable()

  fun loadIntent(intent: Intent?) {
    if (intent == null) return
    Timber.d("Intent Received %s ", intent.toString())
    Timber.d("extras %s", intent.extras?.toString())
    val uri = intent.data

    if (intent.isNotification()) {
      val bundle = intent.extras ?: Bundle.EMPTY
//      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_NOTIFICATION, bundle, false)
    } else if (userManager.user.isAuBankSalesSupport()) {
      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_AU_BANK_MEETING, true)
    } else if (intent.isAppShortcuts()) {

      screenNavigator.openPage(intent.getIntExtra(ARG_PAGE_ID, 0), false)

    } else {
      val pageToOpen = intent.getIntExtra(ARG_PAGE_ID, NavigationConstants.NAVIGATE_TO_NONE)
      when {
        pageToOpen == -1 -> screenNavigator.openHome()
        else -> loadFragment(pageToOpen)
      }
    }
  }

  private fun Intent.isNotification(): Boolean {
    val category: String? = getStringExtra(ARG_NOTIFICATION_CATEGORY)
    if (category == null) return false else return true
  }

  private fun Intent.isAppShortcuts(): Boolean {

    return getBooleanExtra(ShortcutConstants.IS_FROM_SHORTCUT, false)
  }

  fun loadFragment(@NavigationConstants pageId: Int) {
    screenNavigator.openPage(NavigationRequest(pageId, Bundle.EMPTY, false))
  }

  fun onMenuClicked(appMenu: AppMenu) {
    pendingMenuAction = appMenu
  }

  fun handlePendingMenuAction() {
    pendingMenuAction?.let {
      handleNavigation(it)
    }
    pendingMenuAction = null
  }

  fun handleNavigation(appMenu: AppMenu) {
    val target = appMenu.pageId
    when (target) {
      NavigationConstants.NAVIGATE_TO_FRESH_REG -> openEkycApp(false)
      NavigationConstants.NAVIGATE_TO_EXISTING_APP -> openEkycApp(true)
      NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU -> getSuggestMe()
      NavigationConstants.NAVIGATE_TO_REPORTS -> navigateToReport(appMenu)
      else -> loadFragment(target)
    }
  }

  private fun getSuggestMe() {
    if (context.isNetworkConnected()) {
      val request = SuggestMeRequest(0, 0)
      disposable.add(
          CRMWebServiceHandler.getSaathiApi()
              .suggestMe(request)
              .compose(
                  applyUiModel()
              )
              .subscribe(
                  Consumer { this.renderSuggestMe(it) })
      )
    } else {
      noInternetConnectionDialog()
    }
  }

  private fun renderSuggestMe(
    uiModel: UiModel<SaathiCommonResponse<ProspectResponse>>
  ) {
    toggleProgressBar(uiModel.inProgress)

    val error = uiModel.error
    if (error != null) {
      showOnFailurePopup(error)
    }

    val data = uiModel.data
    if (data != null && data.response != null) {
      val encryptedUrl = Utility.stringOrEmpty(data.response!!.encryptedUrl)
      val bundle = Bundle()
      bundle.putString(MOSLConstants.URL_PATH, encryptedUrl)
      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_SUGGEST_ME, bundle, false)

    }
  }

  private fun navigateToReport(appMenu: AppMenu) {
    val bundle = Bundle()
    bundle.putString(MOSLConstants.SYSTEM_NAME, appMenu.navigateKey)
    bundle.putString(MOSLConstants.NAME, appMenu.labelStr)
    screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_REPORTS, bundle, false)
  }

  private fun noInternetConnectionDialog() {
    MSFDialog.alertDialog(
        context, 0,
        context.getResources().getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION,
        MOSLConstants.OK, true,
        null
    )
  }

  fun showOnFailurePopup(t: Throwable) {
    val message: String
    if (t is ServiceFailureException) {
      message = t.errorMessage
    } else {
      message = MOSLResponseWrapper.SERVER_ERROR_OCCURRED
    }
    MSFDialog.alertDialog(
        context, 0,
        context.getResources().getString(R.string.motilal_oswal),
        message, "OK", true, null
    )
  }

  fun toggleProgressBar(inProgress: Boolean) {
    if (inProgress) {
      navigator.showProgress()
    } else {
      navigator.hideProgress()
    }
  }

  private fun openEkycApp(isExistingOrNewUser: Boolean) {
    ekycNavigator.openNewEkycApp(isExistingOrNewUser, "")
  }

  companion object {
    //Notification
    const val ARG_NOTIFICATION_CATEGORY = "category"

    //Gamooga Constants
    const val NotificationId = "1"
  }
}