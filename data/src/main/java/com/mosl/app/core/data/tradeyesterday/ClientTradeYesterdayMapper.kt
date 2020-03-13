package com.mosl.app.core.data.tradeyesterday

import com.mosl.app.core.data.clients.additionalParametersTransform
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayInput
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayResponseModel
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayTableItemModel
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradeYesterdayRequest
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.ClientTradedYesterdayItem
import com.mosl.saathi.api.model.dashboard.clienttradedyesterday.TableItem

fun ClientTradeYesterdayInput.requestTransform(): ClientTradeYesterdayRequest {
  return ClientTradeYesterdayRequest(
    businessProfileId = businessProfileId,
    addtionalParameters = additionalParameters.additionalParametersTransform(),
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName
  )
}

fun ClientTradedYesterdayItem.responseTransform(): ClientTradeYesterdayResponseModel {
  var tableList = kotlin.collections.mutableListOf<ClientTradeYesterdayTableItemModel>()
  tables?.apply {
    if (this.isNotEmpty())
      tableList = this.itemTransform()
  }
  return ClientTradeYesterdayResponseModel(
    tableList
  )
}

fun List<TableItem>.itemTransform(): MutableList<ClientTradeYesterdayTableItemModel> {
  return map {
    ClientTradeYesterdayTableItemModel(
      revenueGenerated = it.rEVENUEGENERATED,
      noOfClients = it.nOOFCLIENTS,
      revenueType = it.revenueType
    )
  }.toMutableList()
}