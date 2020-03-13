package com.mosl.mobile.domain.trends

data class TrendsInput(
  @JvmField val businessProfileId: String?,
  @JvmField val widgetSystemName: String,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)
