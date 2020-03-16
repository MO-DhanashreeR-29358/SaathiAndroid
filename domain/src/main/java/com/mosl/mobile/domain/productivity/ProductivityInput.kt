package com.mosl.mobile.domain.productivity

data class ProductivityInput(
  @JvmField val businessProfileId: String?,
  @JvmField val additionalParameters: ProductivityAdditionalParametersInput,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)

data class ProductivityAdditionalParametersInput(
  @JvmField val fltHierarchy: String?
)