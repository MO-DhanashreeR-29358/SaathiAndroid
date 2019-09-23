package com.mosl.tabapp.core

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.f2prateek.rx.preferences2.Preference
import com.mosl.annotations.FcmToken
import com.mosl.app.core.data.MOSLResponseWrapper
import com.mosl.app.core.data.fcm.FcmTokenSyncHelper
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.coreui.base.ActivityDelegate
import com.mosl.app.coreui.base.BaseActivity
import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.login.LoginRepositoryImpl
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.navigator.AppTitle
import com.mosl.navigator.AuthorizationState
import com.mosl.navigator.FragmentNavigationEntry
import com.mosl.navigator.FragmentResolver
import com.mosl.navigator.NavigationHelper
import com.mosl.navigator.Navigator
import com.mosl.shared.util.MSFDialog
import com.mosl.shared.util.MSFDialog.Action
import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ActivityCommonbaseBinding
import com.mosl.tabapp.helper.shortcuts.ShortcutHelper
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.splash.SplashActivityNew
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.HashMap
import javax.inject.Inject

class CommonActivity : BaseActivity(), Navigator, ActivityDelegate {

  lateinit var binding: ActivityCommonbaseBinding
  @Inject
  lateinit var activityNavHelper: ActivityNavigationHelper

  @Inject
  lateinit var userManager: UserManager
  @Inject
  lateinit var loginRepository: LoginRepositoryImpl
  @Inject
  lateinit var fragmentResolver: FragmentResolver
  var globalTitles = HashMap<String, AppTitle>()
  @Inject
  lateinit var screenNavigator: ScreenNavigatorImpl
  @Inject
  lateinit var navigationHelper: NavigationHelper
  @Inject @field:FcmToken lateinit var tokenPref: Preference<String>
  @Inject
  lateinit var fcmTokenSyncHelper: FcmTokenSyncHelper
  @Inject
  lateinit var shortcutHelper: ShortcutHelper
  override fun getLayout(): Int {
    return R.layout.activity_commonbase
  }

  override fun onCreate(
    savedInstanceState: Bundle?
  ) {
    super.onCreate(savedInstanceState)
    binding = ActivityCommonbaseBinding.bind(findViewById(R.id.drawerLayout))
    binding.actionBar.setHomeAction({ openMenuDrawer() })
    binding.actionBar.showNotification({
      screenNavigator.openPage(NavigationConstants.NAVIGATE_TO_NOTIFICATION, true)
      binding.actionBar.updateCnt(0)
    })
    hideAppTitle()
    setupMenuDrawer()
    if (savedInstanceState == null) {
//      screenNavigator.openHome()

      activityNavHelper.loadIntent(intent)
    } else {
      globalTitles = savedInstanceState.getSerializable(PAGE_TITLES) as HashMap<String, AppTitle>
    }

    setupObservables()

  }

  private fun setupObservables() {
    disposable.add(

        tokenPref.asObservable().distinctUntilChanged()
            .map { ignored -> tokenPref.get() }
            .filter { token -> !token.equals("0") }
            .switchMap { token ->
              fcmTokenSyncHelper.saveToken(token)
                  .compose(applyUiModel())
            }
            .subscribe())
  }

  private fun openMenuDrawer() {
    binding.drawerLayout.openDrawer(GravityCompat.START)
  }

  private fun setupMenuDrawer() {
    binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
      override fun onDrawerSlide(
        drawerView: View,
        slideOffset: Float
      ) {
      }

      override fun onDrawerOpened(drawerView: View) {}

      @SuppressLint("SwitchIntDef")
      override fun onDrawerClosed(drawerView: View) {
        activityNavHelper.handlePendingMenuAction()
      }

      override fun onDrawerStateChanged(newState: Int) {

      }
    })
    binding.drawerMenu.onMenuClicked()
        .subscribe { menu ->
          if (menu.pageId == NavigationConstants.NAVIGATE_TO_LOGOUT) {
            logoutCustomer()
            binding.drawerLayout.closeDrawers()
          }

          activityNavHelper.onMenuClicked(menu)

          binding.drawerMenu.selectItem(menu)
          binding.drawerLayout.closeDrawers()
        }
        .addTo(disposable)
  }

  internal fun logoutCustomer() {
    MSFDialog.alertDialog(this@CommonActivity, 0, getString(R.string.alert_dialog_title),
        getString(R.string.MOSL_MENU_LOGOUT_TXT), "Yes", "No", true, { action, data ->
      if (action === Action.OK) {

        doLogout()
      }
    })
  }

  private fun doLogout() {
    val user = userManager.user
    val logoutInfo = LogoutInfo(
        userId = user.userId, token = user.token,
        businessUnitId = user.currentBusinessProfile.businessUnitId ?: "0"
    )
    disposable.add(loginRepository.logout(logoutInfo)
        .toSingleDefault(true).toObservable()
        .compose(applyUiModel())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { uiModel ->
          toggleProgressBar(uiModel.inProgress)

          uiModel.data?.let {
            Timber.d("Logout Successfully")

            openLogin()
          }

          uiModel.error?.let {
            showOnFailurePopup(it)
          }
        })
  }

  fun openLogin() {
    //open image slider instead of splash
    shortcutHelper.setLogoutUserShortcuts()
    val login = Intent(this, SplashActivityNew::class.java)
    startActivity(login)
    finish()
  }

  fun toggleProgressBar(show: Boolean) {
    if (show) {
      showProgress()
    } else {
      hideProgress()
    }
  }

  fun showOnFailurePopup(t: Throwable) {
    val message: String
    if (t is ServiceFailureException) {
      message = t.errorMessage
    } else {
      message = MOSLResponseWrapper.SERVER_ERROR_OCCURRED
    }
    MSFDialog.alertDialog(
        applicationContext, 0,
        resources.getString(R.string.app_name),
        message, "OK", true, null
    )
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putSerializable(PAGE_TITLES, globalTitles)
  }

  override fun authorizationDialog(listener: (AuthorizationState) -> Unit) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearTitles() {
    globalTitles.clear()
  }

  override fun getContainerViewId(): Int {
    return R.id.activityFrameLayout
  }

  override fun getFragment(pageId: Int): Fragment? {
    return fragmentResolver.resolve(pageId)
  }

  override fun gotoPage(fragmentEntry: FragmentNavigationEntry) {
    fragTransitionHelper.navigateTo(fragmentEntry)
  }

  override fun hideAppTitle() {
    binding.actionBar.setTitleVisibility(View.GONE)
  }

  override fun hideProgress() {
    binding.progressBar.visibility = View.GONE
  }

  override fun setAppTitle(
    page: String,
    appTitle: AppTitle?
  ) {
    Timber.d("ActionBarNew Set Title for %s, to %s", page, appTitle)
    val pageTitle: AppTitle = appTitle ?: globalTitles[page] ?: AppTitle.EMPTY

    globalTitles[page] = pageTitle
//    binding.actionBar.setTitleAndOverflow(pageTitle)
  }

  override fun showProgress() {
    binding.progressBar.visibility = View.VISIBLE
  }

  override fun hideOverflowMenu() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun selectMenu(label: Int) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun setOverflowClickListener(listener: OnClickListener?) {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun showOverflowMenu() {
    TODO(
        "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun onBackPressed() {
    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      binding.drawerLayout.closeDrawers()
      return
    }

    if (binding.progressBar.visibility == View.VISIBLE) {
      binding.progressBar.visibility = View.GONE
      return
    }

    if (navigationHelper.onBackPressed()) {
      return
    }

    if (navigationHelper.canGoBack()) {
      screenNavigator.goBack()
      return
    }
    exitPopup()
  }

  fun exitPopup() {
    MSFDialog.alertDialog(
        this, 0, getString(R.string.motilal_oswal),
        "Do you want to exit the application?", "Yes",
        "No", true
    ) { action, _ ->
      if (action == Action.OK) {
        finish()
      }
    }
  }

  companion object {
    const val IS_LOGGED_IN_SUCESS = "isLoggedIn"
    const val PAGE_TITLES = "pageTitles"
    const val ARG_PAGE_ID = "pageId"
  }
}