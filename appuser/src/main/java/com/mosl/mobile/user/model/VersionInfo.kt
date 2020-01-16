package com.mosl.mobile.user.model

data class VersionInfo(
  @JvmField val applicationType: String = "NEW_CRM",
  @JvmField val appVersion: Int,
  @JvmField val requestSource: String = "Android",
  @JvmField val displayVersion: String = ""

)