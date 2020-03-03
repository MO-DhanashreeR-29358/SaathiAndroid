package com.mosl.app.core.data.snapshot

import com.mosl.app.core.data.clients.additionalParametersTransform
import com.mosl.mobile.domain.snapshot.BusinessSnapInput
import com.mosl.mobile.domain.snapshot.BusinessSnapshotResponseModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotTableItemModel
import com.mosl.saathi.api.model.dashboard.snapshot.BusinessSnapshotRequest
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotItem
import com.mosl.saathi.api.model.dashboard.snapshot.SnapshotTableItem

fun BusinessSnapInput.requestTransform(): BusinessSnapshotRequest {
  return BusinessSnapshotRequest(
    businessProfileId = businessProfileId,
    addtionalParameters = additionalParameters.additionalParametersTransform(),
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName
  )
}

fun SnapshotItem.responseTransform(): BusinessSnapshotResponseModel {
  var tableList = mutableListOf<BusinessSnapshotTableItemModel>()
  table?.apply {
    if (this.isNotEmpty())
      tableList = this.itemTransform()
  }
  return BusinessSnapshotResponseModel(
    tableList
  )
}

fun List<SnapshotTableItem>.itemTransform(): MutableList<BusinessSnapshotTableItemModel> {
  return map {
    BusinessSnapshotTableItemModel(
      parameter = it.parameter,
      mtdCount = it.mTDCNT,
      ytdCount = it.yTDCNT
    )
  }.toMutableList()
}