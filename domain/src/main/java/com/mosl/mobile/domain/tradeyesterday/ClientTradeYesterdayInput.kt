package com.mosl.mobile.domain.tradeyesterday

import com.mosl.mobile.domain.clients.APClientsInput

data class ClientTradeYesterdayInput(
  @JvmField val businessProfileId: String?,
  @JvmField val additionalParameters: APClientsInput,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)