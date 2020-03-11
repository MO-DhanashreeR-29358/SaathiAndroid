package com.mosl.mobile.domain.snapshot

import com.mosl.mobile.domain.clients.APClientsInput

data class BusinessSnapInput(
  @JvmField val businessProfileId: String?,
  @JvmField val additionalParameters: APClientsInput,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)