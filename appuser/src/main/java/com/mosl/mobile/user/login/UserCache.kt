package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.splash.ChildMenu
import com.mosl.saathi.api.model.splash.DigitalSalesKitData

/**
 * Created by lalkishore on 3/6/2017.
 */

interface UserCache {

  fun saveUser(user: User)
  fun getUser(): User?
  fun clearUser()
  fun saveCredentials(loginInfo: LoginInfo)
  fun getCredentials(): LoginInfo?
  fun setReportMenuList(reportMenuList: List<ChildMenu>)
  fun getReportMenuList(): List<ChildMenu>

  fun setDigitalSalesKitMenuList(reportMenuList: List<DigitalSalesKitData>)
  fun getDigitalSalesKitMenuList(): List<DigitalSalesKitData>
  fun fnSetisOtpEnable(isOtpRequired:Boolean)
  fun isOtpEnable():Boolean
}
