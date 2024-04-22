package com.mosl.user.helper

import com.mosl.user.domain.model.User

interface UserChangeListener {
  fun onLogin(user: User)
  fun onLogout()
}