package com.mosl.app.core.data.dashboardreport

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.mobile.domain.dashboardreport.LeadAssignItem
import com.mosl.mobile.domain.dashboardreport.LeadAssignRequest
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest
import com.mosl.saathi.api.model.dashboard.reportassgin.GetLeadAssignStpItemResponse
import com.mosl.saathi.api.model.dashboard.reportassgin.GetLeadAssignStpRequest

fun DashboardReportRequest.requestTransform(businessProfileId: String?): DashboardSecondLevelRequest {
    return DashboardSecondLevelRequest(
        addtionalParameters = addtionalParameters,
        pageSize = pageSize,
        page = page,
        widgetSystemName = widgetSystemName ?: "",
        businessProfileId = businessProfileId

    )

}

fun LeadAssignRequest.mapToRequest(): GetLeadAssignStpRequest {

    return GetLeadAssignStpRequest(
        pageSize = pageSize,
        calculatedPage = calculatedPage,
        pageIndex = pageIndex

    )

}

fun List<GetLeadAssignStpItemResponse>.mapToResponseList(): List<LeadAssignItem> {
    return safeMap { it.mapToDashboardReportAssign() }

}

private fun GetLeadAssignStpItemResponse.mapToDashboardReportAssign(): LeadAssignItem {
    return LeadAssignItem(
        name, prospectId, registrationDate, lastConnected, stage, leadStatus, teamLeader
    )
}

