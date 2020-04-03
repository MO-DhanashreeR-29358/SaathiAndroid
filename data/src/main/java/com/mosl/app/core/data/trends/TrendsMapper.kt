package com.mosl.app.core.data.trends

import com.mosl.mobile.domain.trends.TrendsInput
import com.mosl.saathi.api.model.dashboard.trends.TrendsRequest
import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItem
import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItemModel

fun TrendsInput.requestTransform(): TrendsRequest {
  return TrendsRequest(
    businessProfileId = businessProfileId,
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName

  )
}

fun List<TrendsTableItem>.responseTransform(): List<TrendsTableItemModel> {

  var trendsResponseModel = mutableListOf<TrendsTableItemModel>()
  if (this.size > 0) {
    trendsResponseModel = this.itemTransform().toMutableList()
  }
  return trendsResponseModel
}

fun List<TrendsTableItem>.itemTransform(): List<TrendsTableItemModel> {
  return map {
    TrendsTableItemModel(
      parameters = it.parameters,
      chartType = it.chartType, dec = it.dec,
      oct = it.oct, nov = it.nov, jan = it.jan, feb = it.feb, mar = it.mar
    )
  }
}


