package com.mosl.app.core.data.teamtarget

import com.mosl.mobile.domain.teamtarget.AditionalParametersTeamTarget
import com.mosl.mobile.domain.teamtarget.TeamTableItem3Model
import com.mosl.mobile.domain.teamtarget.TeamTableItemModel
import com.mosl.mobile.domain.teamtarget.TeamTargetInput
import com.mosl.mobile.domain.teamtarget.TeamTargetItemModel
import com.mosl.saathi.api.model.dashboard.targetteam.APTeamTargetRequest
import com.mosl.saathi.api.model.dashboard.targetteam.TableItem
import com.mosl.saathi.api.model.dashboard.targetteam.TeamTable3Item
import com.mosl.saathi.api.model.dashboard.targetteam.TeamTargetRequest
import com.mosl.saathi.api.model.dashboard.targetteam.TeamTargetResponse

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

    table = table?.itemTransform1(),
    table1 = table1?.itemTransform1(),
    table2 = table2?.itemTransform1(),
    table3 = table3?.itemTransform()

  )
}

fun List<TeamTable3Item>.itemTransform(): List<TeamTableItem3Model> {
  return map {
    TeamTableItem3Model(
      ord = it.ord,
      displayText = it.displayText,
      value = it.value

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
