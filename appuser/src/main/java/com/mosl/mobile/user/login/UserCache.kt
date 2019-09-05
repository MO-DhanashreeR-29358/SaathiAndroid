package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.User

/**
 * Created by lalkishore on 3/6/2017.
 */

interface UserCache {

  fun saveUser(user: User)
  fun getUser(): User?
  fun clearUser()
  fun saveCredentials(loginInfo: LoginInfo)
  fun getCredentials(): LoginInfo?
}
