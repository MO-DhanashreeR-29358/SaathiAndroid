package com.mosl.mobile.domain.dashboardreport

data class DashboardReportRequest(
  @JvmField val addtionalParameters: Map<String, String>?,
  @JvmField val widgetSystemName: String?,
  @JvmField val pageSize: String?,
  @JvmField val page: String?
)