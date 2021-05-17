package com.mosl.tabapp.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.mosl.ekycexpress.core.meta.constants.LoginRequested
import com.mosl.ekycexpress.login.core.LoginActivity
import com.mosl.ekycexpress.login.nav.LoginConstants
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.mobile.user.login.user
import javax.inject.Inject

class EkycHelper @Inject constructor(
  private val activity: AppCompatActivity,
  private val userManager: UserManagerImpl
) {
  fun navigateToEkyc() {
    val login = Intent(activity, LoginActivity::class.java)
    login.putExtra(
        LoginActivity.ARG_PAGE_ID,
        LoginConstants.NAVIGATE_TO_VERIFY_MOBILE
    )
    login.putExtra(LoginActivity.TRADER_APP, false)
    login.putExtra(LoginActivity.APPLICATION_TYPE, LoginActivity.BR)
    login.putExtra(LoginActivity.APP_LOGIN_ID, userManager.user.employeeCode)
    login.putExtra(LoginActivity.BRANCH_CODE, userManager.user.defaultBusinessProfile.branchCode)
    activity.startActivityForResult(login, LoginRequested)
  }
}