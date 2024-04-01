package com.mosl.mobile.user.model

import java.util.*


data class ProfileInfo(
  @JvmField val inTime: Date?,
  @JvmField val attendanceCount: Int,
  @JvmField val attendanceType: Int,
  @JvmField val webviewUrl: String,
  @JvmField val joiningDate: String,
  @JvmField val isInitialDeclaration: Boolean
)