package com.mosl.tabapp.saathiIndirect.dashboard.foco.domain

import com.mosl.ekycexpress.user.model.emptyString
import com.mosl.mobile.domain.target.AditionalParametersTarget
import com.mosl.mobile.domain.target.Table1ItemModel
import com.mosl.mobile.domain.target.TableItemModel
import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.domain.target.TargetItemModel
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponse
import com.mosl.saathi.api.model.dashboard.target.TableItem
import com.mosl.saathi.api.model.dashboard.target.TargetItemResponse
import com.mosl.saathi.api.model.dashboard.target.TargetTable1Item
import com.mosl.saathi.api.model.dashboard.targetnew.APTargetRequest
import com.mosl.saathi.api.model.dashboard.targetnew.TargetRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq

fun FocoPeriodFilterInput.requestTransform(): FOCODashboardFilterpostReq {
    return FOCODashboardFilterpostReq(
        employeeCode = employeeCode
    )
}


fun List<FocoPeriodResponse>.itemTransform(): List<FocoPeriodModel> {
    return map {
        FocoPeriodModel(
            ord = it.ord,
            displayText = it.displayText ?: emptyString,
            value = it.value ?: "0"


        )
    }
}

