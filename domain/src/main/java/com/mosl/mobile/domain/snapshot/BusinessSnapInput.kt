package com.mosl.mobile.domain.snapshot

data class BusinessSnapInput(
  @JvmField val businessProfileId: String?,
  @JvmField val additionalParameters: Map<String, String>,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)