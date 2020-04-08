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
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import com.mosl.navigator.BaseNavigator
import com.mosl.navigator.NavigationRequest
import com.mosl.navigator.Navigator
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.prospect.ProspectRequest
import com.mosl.saathi.api.model.prospect.ProspectResponse
import com.mosl.saathi.api.model.prospect.SuggestMeRequest
import com.mosl.saathi.api.model.search.DiyLinkResponse
import com.mosl.saathi.api.model.search.DiyRequest
import com.mosl.shared.util.Utility
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.core.CommonActivity.Companion.ARG_PAGE_ID
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.ui.MSFDialog
import com.mosl.tabapp.util.adapters.AutoCompleteAdapter.DiyLinkClickListner
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import timber.log.Timber
import java.util.Objects
import javax.inject.Inject

@ActivityScoped
class ActivityNavigationHelper @Inject constructor(
  internal val screenNavigator: BaseNavigator,
  internal val ekycNavigator: EkycNavigator,
  internal val userManager: UserManagerImpl,
  val navigator: Navigator,
  val saathiApi: SaathiApi

) {
  private var pendingMenuAction: AppMenu? = null
  private val disposable: CompositeDisposable = CompositeDisposable()

  fun loadIntent(intent: Intent?) {
    if (intent == null) return
    Timber.d("Intent Received %s ", intent.toString())
    Timber.d("extras %s", intent.extras?.toString())

    if (intent.isNotification()) {
      val category = intent.getStringExtra(ARG_NOTIFICATION_CATEGORY)
      val target = NotificationData(category)
      if (target.page.equals(LEADVIEW)) {
        screenNavigator.openPage(
          NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, openLeadViewDetail(target), false
        )
      } else {
        screenNavigator.openPage(
          notificationNavigation(category)
        )
      }
    } else if (userManager.user.isAuBankSalesSupport()) {
      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_DASHBOARD, true)
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

  fun loadFragment(@NavigationConstants pageId: Int) {
    screenNavigator.openPage(NavigationRequest(pageId, Bundle.EMPTY, false))
  }

  fun onMenuClicked(appMenu: AppMenu) {
    pendingMenuAction = appMenu
  }

  fun handlePendingMenuAction(context: Context) {
    pendingMenuAction?.let {
      handleNavigation(it, context)
    }
    pendingMenuAction = null
  }

  fun handleNavigation(
    appMenu: AppMenu,
    context: Context
  ) {
    val target = appMenu.pageId
    when (target) {
      NavigationConstants.NAVIGATE_TO_FRESH_REG -> openEkycApp(false)
      NavigationConstants.NAVIGATE_TO_EXISTING_APP -> openEkycApp(true)
      NavigationConstants.NAVIGATE_TO_SUGGEST_ME_MENU -> getSuggestMe(context)
      NavigationConstants.NAVIGATE_TO_REPORTS -> navigateToReport(appMenu)
      else -> loadFragment(target)
    }
  }

  fun notificationNavigation(category: String): Int {
    val target = NotificationData(category)
    return when (target.page) {
      PENDINGAPPROVAL -> NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL
      BIRTHDAY -> NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY
      DASHBOARD -> NavigationConstants.NAVIGATE_TO_DASHBOARD
      else -> NavigationConstants.NAVIGATE_TO_DASHBOARD
    }
  }

  private fun getSuggestMe(context: Context) {
    if (context.isNetworkConnected()) {
      val request = SuggestMeRequest(0, 0)
      disposable.add(
        saathiApi
          .suggestMe(request)
          .compose(
            applyUiModel()
          )
          .subscribe(
            Consumer { this.renderSuggestMe(it, context) })
      )
    } else {
      noInternetConnectionDialog(context)
    }
  }

  fun getDiyLink(context: Context, leadId: String, callBack: DiyLinkClickListner?) {
    if (context.isNetworkConnected()) {
      val request = DiyRequest(leadId)
      disposable.add(saathiApi
        .createDiy(request)
        .compose(applyUiModel())
        .subscribe { this.renderDiyLink(it, context, callBack) })

    } else {
      noInternetConnectionDialog(context)
    }
  }

  private fun renderSuggestMe(
    uiModel: UiModel<SaathiCommonResponse<ProspectResponse>>,
    context: Context
  ) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error, context)
    }


    uiModel.onSuccess { data ->
      if (data.response != null) {
        val encryptedUrl = Utility.stringOrEmpty(data.response!!.encryptedUrl)
        val bundle = Bundle()
        bundle.putString(MOSLConstants.URL_PATH, encryptedUrl)
        screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_SUGGEST_ME, bundle, false)

      }
    }

  }

  private fun renderDiyLink(
    uiModel: UiModel<SaathiCommonResponse<DiyLinkResponse>>,
    context: Context, callBack: DiyLinkClickListner?
  ) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error, context)
    }


    uiModel.onSuccess { data ->
      if (data.response != null) {
        callBack?.getDiyUrl(data.response!!.url)
        MSFDialog.alertDialog(
          context, 0,
          context.getResources().getString(R.string.motilal_oswal),
          data.message, "OK", true, null
        )
      } else
        MSFDialog.alertDialog(
          context, 0,
          context.getResources().getString(R.string.motilal_oswal),
          data.errors?.get(0)?.message, "OK", true, null
        )
    }

  }

  fun convertEkycApi(
    context: Context,
    ekycModule: EkycModule
  ) {
    if (context.isNetworkConnected()) {
      val request = ProspectRequest(
        ekycModule.mobile, ekycModule.email, ekycModule.branchEmpCode, ekycModule.source,
        ekycModule.prospectSource, ekycModule.prospectSourceDetails, ekycModule.interestedIn
      )

      disposable.add(saathiApi
        .registerProspectOnline(request)
        .compose(applyUiModel())
        .subscribe { this.renderProspect(it, context) })

    } else {
      noInternetConnectionDialog(context)
    }
  }

  private fun renderProspect(
    uiModel: UiModel<SaathiCommonResponse<List<ProspectResponse>>>,
    context: Context
  ) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onFailure { error ->
      showOnFailurePopup(error, context)
    }

    uiModel.onSuccess { data ->
      val prospectResponse =
        Objects.requireNonNull<List<ProspectResponse>>(data.response)[0]
      if (prospectResponse.isRegistrationAllowed()) {
        val oaoUserId = prospectResponse.ekycUserID
        ekycNavigator.openNewEkycApp(true, oaoUserId)
      } else {
        MSFDialog.alertDialog(
          context, 0,
          context.getResources().getString(R.string.motilal_oswal),
          data.message, "OK", true, null
        )
      }
    }
  }

  private fun navigateToReport(appMenu: AppMenu) {
    val bundle = Bundle()
    bundle.putString(MOSLConstants.SYSTEM_NAME, appMenu.navigateKey)
    bundle.putString(MOSLConstants.NAME, appMenu.labelStr)
    screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_REPORTS, bundle, false)
  }

  private fun noInternetConnectionDialog(context: Context) {
    MSFDialog.alertDialog(
      context, 0,
      context.getResources().getString(R.string.motilal_oswal), MOSLConstants.INTERNET_CONNECTION,
      MOSLConstants.OK, true,
      null
    )
  }

  fun showOnFailurePopup(
    t: Throwable,
    context: Context
  ) {
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

  private fun openLeadViewDetail(data: NotificationData): Bundle {
    val bundle = Bundle()
    val leadId = data.paramMap
      ?.get(MOSLConstants.LEAD_ID)
    val ucid = data.paramMap
      ?.get(MOSLConstants.UCID)
    bundle.putString(MOSLConstants.UCID, ucid)
    bundle.putString(MOSLConstants.LEAD_ID, leadId)
    bundle.putParcelable(MOSLConstants.NOTIFICATION_DATA, data)

    return bundle
//    screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER, bundle, false)
  }

  companion object {
    //Notification
    const val ARG_NOTIFICATION_CATEGORY = "category"

    const val LEADVIEW = "LEADVIEW"
    const val PENDINGAPPROVAL = "PENDINGAPPROVAL"
    const val BIRTHDAY = "BIRTHDAY"
    const val DASHBOARD = "DASHBOARD"
  }
}