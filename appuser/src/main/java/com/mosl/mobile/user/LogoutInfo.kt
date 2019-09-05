package com.mosl.mobile.user

data class LogoutInfo(
  @JvmField val userId: String,
  @JvmField val token: String,
  @JvmField val businessUnitId: String
)