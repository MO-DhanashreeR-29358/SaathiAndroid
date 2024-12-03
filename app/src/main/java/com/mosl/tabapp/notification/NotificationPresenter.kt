package com.mosl.tabapp.notification

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.model.NotificationData
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NotificationPresenter @Inject constructor(
  view: NotificationView,
  val repository: NotificationRepository,
  val userManager: UserManager,
  private val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<NotificationView>(view) {

  override fun start() {
    Observable.merge(view.onSpinnerSelection(), onRetryCalled())
      .map {
        NotificationRequestInput(view.getNotificationRequestCategory())
      }.switchMap {
        repository.getNotificationData(it)
          .compose(applyUiModel())
      }
      .subscribe {
        view.render(it)
      }
      .addTo(disposable)

    view.onItemClicked()
      .subscribe {
        navigateTo(it.additionalData)
      }
      .addTo(disposable)
  }

  private fun navigateTo(category: String) {
    val target = NotificationData(category)
    if (category.isBlank()) {
      screenNavigator.openHome()
    } else if (target.page.equals(LEADVIEW)) {
      if(userManager.getUser()?.isIndirectUser() == true){
        screenNavigator.openPage(
          NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_DETAIL_MAIN.toString(), openLeadViewDetail(target),
          false
        )
      }else{
        screenNavigator.openPage(
          NavigationConstants.NAVIGATE_TO_LEAD_VIEW_PAGER.toString(), openLeadViewDetail(target),
          false
        )
      }

    } else {
      screenNavigator.openPage(
        notificationNavigation(category)
      )
    }

  }

  private fun openLeadViewDetail(data: NotificationData): Bundle {
    val bundle = Bundle()
    val leadId = data.paramMap
      ?.get(LEAD_ID)
    val ucid = data.paramMap
      ?.get(UCID)
    bundle.putString(UCID, ucid)
    bundle.putString(LEAD_ID, leadId)
    bundle.putParcelable(NOTIFICATION_DATA, data)

    return bundle
  }

  fun notificationNavigation(category: String): String {
    val target = NotificationData(category)
    return when (target.page) {
      PENDINGAPPROVAL -> NavigationConstants.NAVIGATE_TO_PENDING_APPROVAL.toString()
      BIRTHDAY -> NavigationConstants.NAVIGATE_TO_BIRTHDAY_ANNIVERSARY.toString()
      DASHBOARD -> NavigationConstants.NAVIGATE_TO_DASHBOARD.toString()
      else -> NavigationConstants.NAVIGATE_TO_DASHBOARD.toString()
    }
  }

  companion object {
    //Notification

    const val LEADVIEW = "LEADVIEW"
    const val PENDINGAPPROVAL = "PENDINGAPPROVAL"
    const val BIRTHDAY = "BIRTHDAY"
    const val DASHBOARD = "DASHBOARD"
  }
}