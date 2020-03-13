package com.mosl.mobile.domain.snapshot

import com.mosl.mobile.domain.clients.BusinessSnapAdditionalParametersInput

data class BusinessSnapInput(
  @JvmField val businessProfileId: String?,
  @JvmField val additionalParameters: BusinessSnapAdditionalParametersInput,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)