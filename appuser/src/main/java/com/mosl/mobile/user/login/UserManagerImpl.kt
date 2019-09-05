package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.User
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

  init {
    val user = userCache.getUser()
    this.user = user
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

  /*override fun isLoggedIn(): Boolean {
    return user != null
  }*/
}