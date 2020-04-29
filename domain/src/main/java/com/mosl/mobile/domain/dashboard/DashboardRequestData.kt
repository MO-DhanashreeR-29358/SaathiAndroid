package com.mosl.mobile.domain.dashboard

data class DashboardRequestData(
  @JvmField val profileId: String?,
  @JvmField val productId: String?,
  @JvmField val empCode: String?
)