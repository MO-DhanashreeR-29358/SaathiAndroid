package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.splash.ChildMenu

/**
 * Created by lalkishore on 3/6/2017.
 */

interface UserManager {

  fun getUser(): User?
  fun getCredentials(): LoginInfo?
  fun isLoggedIn(): Boolean
  fun setReportMenuList(reportMenuList: List<ChildMenu>)
  fun getReportMenuList(): List<ChildMenu>

}

val UserManager.user: User
  get() {
    return getUser() ?: error("Not Logged In")
  }


