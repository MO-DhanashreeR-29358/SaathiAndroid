package com.mosl.app.core.data.teamtarget

import com.mosl.mobile.domain.teamtarget.*
import com.mosl.saathi.api.model.dashboard.targetteam.*

fun TeamTargetInput.requestTransform(): TeamTargetRequest {
  return TeamTargetRequest(
    businessProfileId = businessProfileId,
    addtionalParameters = addtionalParameters?.apTransform(),
    employeeCode = employeeCode,
    productId = productId,
    widgetSystemName = widgetSystemName

  )
}

fun AditionalParametersTeamTarget.apTransform(): APTeamTargetRequest {
  return APTeamTargetRequest(
    fltBankName, fltBankSubBrokerCode, fltBankBranchCode,
    fltHierarchy, fltPeriod
  )
}

fun TeamTargetResponse.responseTransform(): TeamTargetItemModel {
  return TeamTargetItemModel(
    table = table.itemTransform1(),
    table1 = table1.itemTransform1(),
    table2 = table2.itemTransform1(),
    table4 = table4?.itemTransform1() ?: emptyList(),
    table3 = table3.itemTransform()

  )
}

fun List<TeamTable3Item>.itemTransform(): List<TeamTableItem3Model> {
  return map {
    TeamTableItem3Model(
      ord = it.ord,
      displayText = it.displayText,
      value = it.value ?: "0"

    )
  }
}

fun List<TableItem>.itemTransform1(): List<TeamTableItemModel> {
  return map {
    TeamTableItemModel(
      formatted = it.formatted, label = it.label,
      value = it.value,
      widgetType = it.widgetType

    )
  }
}
