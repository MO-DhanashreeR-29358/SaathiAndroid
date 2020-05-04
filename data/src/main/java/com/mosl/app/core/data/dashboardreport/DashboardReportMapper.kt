package com.mosl.app.core.data.dashboardreport

import com.mosl.mobile.domain.dashboardreport.DashboardReportRequest
import com.mosl.saathi.api.model.dashboard.dashboardsecondlevel.DashboardSecondLevelRequest

fun DashboardReportRequest.requestTransform(): DashboardSecondLevelRequest {
  return DashboardSecondLevelRequest(
    addtionalParameters = addtionalParameters,
    pageSize = pageSize,
    page = page,
    widgetSystemName = widgetSystemName ?: ""

  )

}

