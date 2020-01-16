package com.mosl.mobile.user.model

data class LoginResponse(
  @JvmField val smtEnabled: String,
  @JvmField val user: User
)