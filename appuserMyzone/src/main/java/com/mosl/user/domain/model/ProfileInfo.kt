package com.mosl.user.domain.model

import java.util.Date

data class ProfileInfo(
  @JvmField val inTime: Date?,
  @JvmField val attendanceCount: Int,
  @JvmField val attendanceType: Int,
  @JvmField val webviewUrl: String,
  @JvmField val joiningDate: String,
  @JvmField val isInitialDeclaration: Boolean
)