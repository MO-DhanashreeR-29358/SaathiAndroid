package com.mosl.tabapp.core

import android.content.Intent
import android.os.Bundle
import com.mosl.annotations.ActivityScoped
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.navigator.BaseNavigator
import com.mosl.navigator.NavigationRequest
import com.mosl.tabapp.core.CommonActivity.Companion.ARG_PAGE_ID
import com.mosl.tabapp.drawermenu.data.AppMenu
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@ActivityScoped
class ActivityNavigationHelper @Inject constructor(
  internal val screenNavigator: BaseNavigator,
  internal val ekycNavigator: EkycNavigator,
  internal val userManager: UserManagerImpl
) {
  private var pendingMenuAction: AppMenu? = null
  private val disposable: CompositeDisposable = CompositeDisposable()

  fun loadIntent(intent: Intent?) {
    if (intent == null) return
    Timber.d("Intent Received %s ", intent.toString())
    Timber.d("extras %s", intent.extras?.toString())
    val uri = intent.data

    if (isNotification(intent)) {
      val bundle = intent.extras ?: Bundle.EMPTY
//      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_NOTIFICATION, bundle, false)
    } else {
      val pageToOpen = intent.getIntExtra(ARG_PAGE_ID, NavigationConstants.NAVIGATE_TO_NONE)
      when {
        pageToOpen == -1 -> screenNavigator.openHome()
        else -> loadFragment(pageToOpen)
      }
    }
  }

  private fun isNotification(intent: Intent): Boolean {
    val category: String? = intent.getStringExtra(ARG_NOTIFICATION_CATEGORY)
    if (category == null) return false else return true
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
      else -> loadFragment(target)
    }
  }

  private fun openEkycApp(isExistingOrNewUser: Boolean) {
    ekycNavigator.openNewEkycApp(isExistingOrNewUser)
  }

  companion object {
    //Notification
    const val ARG_NOTIFICATION_CATEGORY = "category"

    //Gamooga Constants
    const val NotificationId = "1"
  }
}