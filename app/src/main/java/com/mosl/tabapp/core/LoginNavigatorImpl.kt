package com.mosl.tabapp.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mosl.navigator.FragmentNavigationEntry
import com.mosl.navigator.NavigationEntry
import com.mosl.navigator.NavigationRequest
import com.mosl.navigator.Navigator
import com.mosl.navigator.asFragmentEntry
import timber.log.Timber
import javax.inject.Inject

class LoginNavigatorImpl @Inject constructor(
  val activity: AppCompatActivity,
  val navigator: Navigator
) : LoginNavigator {
  override fun openPage(
    pageId: Int,
    args: Bundle,
    addToStack: Boolean,
    forceReload: Boolean
  ) {
    openPage(NavigationRequest(pageId, args, addToStack))
  }

  override fun finish() {
    activity.finish()
  }

  override fun openDashboard() {
    activity.setResult(1002)
    activity.finish()
  }

  override fun goBack() {
    openPage(FragmentNavigationEntry.goBack())
  }

  override fun goBack(result: Bundle) {
    openPage(FragmentNavigationEntry.goBackWithResult(result))
  }

  override fun openHome() {
  }

  override fun openPage(entry: NavigationEntry) {
    processNavigation(entry)
  }

  override fun openPage(request: NavigationRequest) {
    handleNavigationRequest(request)
  }

  protected fun handleNavigationRequest(request: NavigationRequest) {
    processNavigation(request.asFragmentEntry())
  }

  protected fun processNavigation(entry: NavigationEntry?) {
    entry ?: return
    when (entry) {
      is FragmentNavigationEntry -> {
        navigator.gotoPage(entry)
      }
      else -> {
        Timber.e("IllegalState : LoginNavigator only supports fragment Navigation")
      }
    }
  }


  override fun openPage(
    pageId: Int,
    addToStack: Boolean
  ) {
    openPage(NavigationRequest(pageId, Bundle.EMPTY, addToStack))
  }

  override fun pageNotAccessible() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }
}