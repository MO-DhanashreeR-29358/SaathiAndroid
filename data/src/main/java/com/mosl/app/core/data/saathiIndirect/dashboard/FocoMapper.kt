package com.mosl.app.core.data.saathiIndirect.dashboard

import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodFilterInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq

fun FocoPeriodFilterInput.requestTransform(): FOCODashboardFilterpostReq {
    return FOCODashboardFilterpostReq(
        employeeCode = employeeCode
    )
}

fun FocoDashboardInput.requestTransform(): FOCODashboardFilterpostReq {
    return FOCODashboardFilterpostReq(
        employeeCode = employeeCode,
        frequency = frequency,
        calculatedPage = calculatedPage,
        calculatedPageP = calculatedPageP,
        page = page,
        pageP = pageP,
        pageSize = pageSize,
        totalCountP = totalCountP,
        totalCount = totalCount
    )
}
fun List<FocoPeriodResponse>.itemTransform(): List<FocoPeriodModel> {
    return map {
        FocoPeriodModel(
            ord = it.ord,
            displayText = it.displayText ?: "",
            value = it.value ?: "0"


        )
    }
}

