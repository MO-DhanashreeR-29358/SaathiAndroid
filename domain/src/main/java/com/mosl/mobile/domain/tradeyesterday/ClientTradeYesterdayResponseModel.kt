package com.mosl.mobile.domain.tradeyesterday

data class ClientTradeYesterdayResponseModel(
  @JvmField val table: List<ClientTradeYesterdayTableItemModel>
)

data class ClientTradeYesterdayTableItemModel(
  @JvmField val revenueGenerated: Double?,
  @JvmField val noOfClients: Int?,
  @JvmField val revenueType: String?
)