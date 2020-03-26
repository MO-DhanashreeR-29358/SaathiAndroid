package com.mosl.tabapp.splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.mosl.app.core.data.MOSLResponseWrapper.SERVER_ERROR_OCCURRED
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.network.isNetworkConnected
import com.mosl.app.core.rx.applyUiModel
import com.mosl.app.core.utils.showConfirmation
import com.mosl.app.coreui.base.LeanBaseActivity
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.user.login.LoginRepositoryImpl
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.VersionInfo
import com.mosl.mobile.user.model.VersionResponse
import com.mosl.shared.util.MSFDialog
import com.mosl.shared.util.Utility
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonActivity
import com.mosl.tabapp.databinding.ActivitySplashBinding
import com.mosl.tabapp.helper.FirebaseHelper
import com.mosl.tabapp.helper.shortcuts.ShortcutHelper
import com.mosl.tabapp.login.LoginActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class SplashActivity : LeanBaseActivity() {
  lateinit var disposables: CompositeDisposable
  lateinit var binding: ActivitySplashBinding

  @Inject
  lateinit var loginRepository: LoginRepositoryImpl

  @Inject
  lateinit var userManager: UserManager

  @Inject
  lateinit var shortcutHelper: ShortcutHelper
  private val handler = Handler()
  private var count = 0

  @Inject
  lateinit var firebaseHelper: FirebaseHelper
  override fun getLayout(): Int {
    return R.layout.activity_splash
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivitySplashBinding.bind(findViewById(R.id.splash_root))
    disposables = CompositeDisposable()
    Timber.d("password %s", userManager.getUser()?.password)
    handler.postDelayed({
      checkNetwork()
    }, 2000)
  }

  override fun clearWindowBackground(): Boolean {
    return false
  }

  private fun checkNetwork() {
    if (this.isNetworkConnected()) {
      checkVersion()

    } else {
      // No network
      if (count < 2) {
        count++
        if (!(this).isFinishing) { //show dialog
          showConfirmation(
            message = "Application needs network to proceed",
            cancelable = true,
            title = getString(R.string.alert_dialog_title),
            positiveButton = R.string.button_label_retrieve,
            negativeButton = R.string.button_label_exit
          )
          { positiveClicked ->
            if (positiveClicked) {
              checkNetwork()
            } else {
              finish()
            }
            Unit
          }
        }
      } else {
        actionableAlert(
          message = "Application needs network to proceed",
          cancelable = false,
          buttonText = R.string.button_label_exit
        ) {
          finish()
        }
      }
    }
  }

  private fun checkVersion() {
    val versionRequest = VersionInfo(appVersion = Utility.getAppVersionCode(applicationContext))

    disposables.add(
      loginRepository.versionCheck(versionRequest)
        .compose(applyUiModel())
        .subscribe(this::renderVersionData)
    )

  }

  fun toggleProgressBar(show: Boolean) {
    if (show) {
      binding.progressBar.visibility = View.VISIBLE
    } else {
      binding.progressBar.visibility = View.GONE
    }
  }

  private fun renderVersionData(uiModel: UiModel<VersionResponse>) {
    toggleProgressBar(uiModel.inProgress)

    uiModel.onSuccess {

      val msg = it.msg;
      when (it.statusCode) {
        0 -> MSFDialog.alertDialog(
          this@SplashActivity, 0,
          resources.getString(R.string.MOSL), msg,
          UPDATE, false, object : MSFDialog.DialogListener {
            override fun alertDialogAction(
              action: MSFDialog.Action,
              vararg data: Any
            ) {
              if (action == MSFDialog.Action.OK) {
                val i = Intent(
                  Intent.ACTION_VIEW
                )
                i.data = Uri.parse(GOOGLE_PLAY_URL)
                startActivity(i)
                finish()
              }
            }
          })
        3 -> MSFDialog.alertDialog(
          this@SplashActivity, 0,
          resources.getString(R.string.MOSL), msg,
          UPDATE, CONTINUE, false, object : MSFDialog.DialogListener {
            override fun alertDialogAction(
              action: MSFDialog.Action,
              vararg data: Any
            ) {
              if (action == MSFDialog.Action.OK) {
                val i = Intent(
                  Intent.ACTION_VIEW
                )
                i.data = Uri.parse(GOOGLE_PLAY_URL)
                startActivity(i)
                finish()
              } else if (action === MSFDialog.Action.CANCEL) {
                handleStartup()
              }
            }
          })
        2 -> MSFDialog.alertDialog(
          this@SplashActivity, 0,
          resources.getString(R.string.MOSL), msg,
          "Ok", false
        ) { action, _ ->
          if (action == MSFDialog.Action.OK) {
            finish()
          }

        }
        1 -> handleStartup()
      }

    }

    uiModel.onFailure {
      Timber.e(it, "message %s", it.message)
      infoDialog(it)
    }
    /*uiModel.error?.let {
      Timber.d("message %s", it.message!!)
      infoDialog(uiModel.data!!.msg)
    }*/
  }

  private fun fetchReportTypes() {
    disposables.add(
      loginRepository.fetchReportTypes().compose(applyUiModel())
        .subscribe {
          renderFetchReportData(it)
        }
    )
  }

  private fun renderFetchReportData(uiModel: UiModel<List<ReportType>>) {
    uiModel.onSuccess {
      if (it.size > 0) {
        for (reportTypeResponse in it) {
          if (reportTypeResponse.isPreDefinedReports()) {
            val childMenuList = reportTypeResponse.childMenuList
            for (childMenu in childMenuList) {
              if (childMenu.isBasicReports()) {
                userManager.setReportMenuList(childMenu.ChildMenuList)
//                AccountDetails.setMenuReportList(childMenu.ChildMenuList)
                openCommonActivity()
              }
            }
          }
        }
      }
    }

    uiModel.onFailure {
      openCommonActivity()
    }

  }

  fun handleStartup() {
    if (userManager.isLoggedIn()) {
      shortcutHelper.setLoggedInUserShortcuts()
      login()
    } else {
      shortcutHelper.setLogoutUserShortcuts()
      openLogin()
    }
  }

  fun login() {
    disposables.add(
      Observable.fromCallable {
        LoginInfo(
          userManager.getUser()!!.employeeCode, userManager.getUser()!!.password
        )
      }.switchMap { input ->
        loginRepository.login(input)

      }.compose(applyUiModel()).subscribe(
        this::renderLoginData
      )
    )
  }

  fun renderLoginData(uimodel: UiModel<User>) {

    uimodel.onSuccess {
      setUserPreferenceData(it)
      setUserProperty(it)
    }

    uimodel.onFailure {
      shortcutHelper.setLogoutUserShortcuts()
      openLogin()
    }

  }

  private fun setUserProperty(user: User) {
    user.apply {
      firebaseHelper.setUserProperty(this.employeeCode)
    }
  }

  fun setUserPreferenceData(user: User) {
    if (user.isAufsb()) {
      openCommonActivity()
    } else {
      fetchReportTypes()
    }

  }

  fun infoDialog(t: Throwable) {
    val errorMessage: String
    if (t is ServiceFailureException) {
      errorMessage = (t).errorMessage
    } else {
      errorMessage = SERVER_ERROR_OCCURRED
    }
    actionableAlert(errorMessage, false) {
      finish()
      Unit
    }
  }

  fun openLogin() {
    val login = Intent(this@SplashActivity, LoginActivity::class.java)
    startActivityForResult(login, 1001)
//    finish()
  }

  private fun openCommonActivity() {
    val intent = Intent(this@SplashActivity, CommonActivity::class.java)
    startActivity(intent)
    finish()
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    if (resultCode == 1002) {
      val intent = Intent(this@SplashActivity, CommonActivity::class.java)
      intent.putExtra(CommonActivity.IS_LOGGED_IN_SUCESS, true)
      startActivity(intent)
    }
    finish()
    super.onActivityResult(requestCode, resultCode, data)
  }

  companion object {
    val UPDATE = "Update"
    val CONTINUE = "Continue"
    val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=com.mosl.tabapp"
  }
}

