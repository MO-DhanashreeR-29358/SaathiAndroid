package com.mosl.tabapp.navigation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mosl.annotations.ActivityScoped
import com.mosl.app.core.utils.toast
import com.mosl.mobile.user.login.UserManager
import com.mosl.navigator.ActivityNavigationEntry
import com.mosl.navigator.BaseNavigator
import com.mosl.navigator.FragmentNavigationEntry
import com.mosl.navigator.NavigationEntry
import com.mosl.navigator.NavigationHelper
import com.mosl.navigator.NavigationRequest
import com.mosl.navigator.Navigator
import com.mosl.navigator.asFragmentEntry
import com.mosl.tabapp.common.EkycSecurityUtil
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.ekyc.NEW
import com.mosl.tabapp.ekyc.NEWEKYC_PACKAGE_NAME
import com.mosl.tabapp.ekyc.OUTPUT
import com.mosl.tabapp.ekyc.PLAY_STORE_URL
import com.mosl.tabapp.ekyc.RESUME
import com.mosl.tabapp.ekyc.getDataForEkyc
import javax.inject.Inject

@ActivityScoped
class ScreenNavigatorImpl @Inject constructor(
  val activity: AppCompatActivity,
  protected val navigationHelper: NavigationHelper,
  val userManager: UserManager,
  val navigator: Navigator
) : BaseNavigator, EkycNavigator {
  override fun openNewEkycApp(
    isExistingUser: Boolean,
    userId: String
  ) {
    val commandType = if (isExistingUser) RESUME else NEW
    try {
      val launchIntentForPackage =
        activity.packageManager.getLaunchIntentForPackage(NEWEKYC_PACKAGE_NAME)
      launchIntentForPackage?.setFlags(
          Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      )
      launchIntentForPackage?.putExtra(
          OUTPUT, EkycSecurityUtil.encrypt(userManager.getDataForEkyc(commandType, userId))
      )
      activity.startActivity(launchIntentForPackage)
    } catch (e: Exception) {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.setData(Uri.parse(PLAY_STORE_URL + NEWEKYC_PACKAGE_NAME))
      activity.startActivity(intent)
    }
  }

  override fun goBack() {
    openPage(FragmentNavigationEntry.goBack())
  }

  override fun goBack(result: Bundle) {
    openPage(FragmentNavigationEntry.goBackWithResult(result))
  }

  override fun openHome() {
    openPage(NavigationConstants.NAVIGATE_TO_DASHBOARD, false)
  }

  override fun openPage(entry: NavigationEntry) {
    processNavigation(entry)
  }

  override fun openPage(request: NavigationRequest) {
    handleNavigationRequest(request)
  }

  protected fun handleNavigationRequest(request: NavigationRequest) {
    val pageId = request.pageId
    when {
      /**
       * (pageId < -1) represents pages in debug variant.
       */
      pageId < -1 -> processNavigation(request.asFragmentEntry())
      /**
       * Handles case where work is in progress.
       */
      pageId == NavigationConstants.NAVIGATE_TO_NONE -> activity.toast("Work In Progress")
      /**
       * Handles back navigation on fragments.
       */
      pageId == FragmentNavigationEntry.GoBackPageId -> processNavigation(request.asFragmentEntry())
      else -> processNavigation(request.asFragmentEntry())
      /* LoginFragmentGetter.isHandled(pageId) -> {
         processNavigation(request.createActivityNavigationEntry())
       }
       else -> {
         processNavigation(request.asFragmentEntry())
       }*/
    }
  }

  protected fun processNavigation(entry: NavigationEntry?) {
    entry ?: return
    when (entry) {
      is ActivityNavigationEntry -> {
        activity.navigate(entry)
      }
      is FragmentNavigationEntry -> {
        /**
         * Setting isAddToStack to false if currentFragment is null,
         * This prevents crash for on first fragment loaded on activity
         */
        val navEntry = if (navigationHelper.currentFragment == null) {
          entry.copy(isAddToStack = false)
        } else entry
        navigator.gotoPage(navEntry)
      }
    }
  }

  private fun AppCompatActivity.navigate(entry: ActivityNavigationEntry?) {
    entry ?: return
    val intent = Intent(this, entry.activity).apply {
      putExtras(entry.bundle)
    }
    val requestCode = entry.requestCode
    if (requestCode != null) {
      startActivityForResult(intent, requestCode)
    } else {
      startActivity(intent)
    }
  }

  override fun openPage(
    pageId: Int,
    args: Bundle,
    addToStack: Boolean
  ) {
    openPage(FragmentNavigationEntry(pageId, args, addToStack))
  }

  override fun openPage(
    pageId: Int,
    addToStack: Boolean
  ) {
    openPage(FragmentNavigationEntry(pageId, Bundle.EMPTY, addToStack))
  }

  override fun pageNotAccessible() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }
}