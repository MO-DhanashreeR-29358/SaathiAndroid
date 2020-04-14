package com.mosl.tabapp.pendingApproval

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class PendingApprovalPresenter @Inject constructor(
  view: PendingApprovalView,
  val repository: NotificationRepository,
  private val screenNavigator: ScreenNavigatorImpl
) : BasePresenter<PendingApprovalView>(view) {

  override fun start() {

  }

  private fun navigateTo(category: String) {

  }

}