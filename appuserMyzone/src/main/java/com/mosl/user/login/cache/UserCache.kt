package com.mosl.user.login.cache

import com.mosl.user.domain.model.User

interface UserCache {
  fun saveUser(user: User)
  fun getUser(): User?
  fun clearUser()
}