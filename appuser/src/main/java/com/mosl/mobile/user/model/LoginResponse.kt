package com.mosl.mobile.user.model

data class LoginResponse(
  @JvmField val smtEnabled: String,
  @JvmField val ekycRevamp: String,
  @JvmField val user: User
)