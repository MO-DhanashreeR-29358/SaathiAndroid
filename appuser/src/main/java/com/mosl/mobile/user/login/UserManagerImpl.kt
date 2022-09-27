package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.User
import com.mosl.saathi.api.model.splash.ChildMenu
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by lalkishore on 3/6/2017.
 */
@Singleton
class UserManagerImpl @Inject constructor(
  private val userCache: UserCache
) : UserManager {

  private var user: User?
  private var reportMenulist: List<ChildMenu>
  private lateinit var digitalSalesKitMenuList: List<DigitalSalesKitData>

  init {
    val user = userCache.getUser()
    this.user = user
    val reportMenuList = userCache.getReportMenuList()
    val digitalSalesKitMenuList = userCache.getDigitalSalesKitMenuList()
    this.reportMenulist = reportMenuList
    this.digitalSalesKitMenuList = digitalSalesKitMenuList
  }

  fun saveUser(user: User) {
    userCache.saveUser(user)
    this.user = user
  }

  fun saveCredentials(loginInfo: LoginInfo) {
    userCache.saveCredentials(loginInfo)
  }

  fun logout() {
    this.user = null
    userCache.clearUser()
  }

  override fun getUser(): User? {
    return user
  }

  override fun isLoggedIn(): Boolean {
    return user != null
  }

  override fun getCredentials(): LoginInfo? {
    return userCache.getCredentials()
  }

  override fun setReportMenuList(reportMenuList: List<ChildMenu>) {
    userCache.setReportMenuList(reportMenuList)

    this.reportMenulist = reportMenuList
  }

  override fun getReportMenuList(): List<ChildMenu> {
    return reportMenulist
  }

  override fun setDigitalSalesKitMenuList(list: List<DigitalSalesKitData>) {
    userCache.setDigitalSalesKitMenuList(list)
    this.digitalSalesKitMenuList = list
  }

  override fun getDigitalSalesKitMenuList(): List<DigitalSalesKitData> {
    return digitalSalesKitMenuList
  }
  /*override fun isLoggedIn(): Boolean {
    return user != null
  }*/
}