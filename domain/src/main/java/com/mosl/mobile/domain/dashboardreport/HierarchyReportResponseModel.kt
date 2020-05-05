package com.mosl.mobile.domain.dashboardreport

data class DashboardReportData(
  @JvmField val data: List<String>,
  @JvmField val headers: List<String>,
  @JvmField val hasMap: Map<String, String>
)

data class DashboardReportCombo(
  val headers: List<String>,
  val data: List<DashboardReportData>
)

