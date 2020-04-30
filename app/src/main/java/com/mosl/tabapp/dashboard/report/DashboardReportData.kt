package com.mosl.tabapp.dashboard.report

data class DashboardReportData(
  @JvmField val additionalMap: Map<String, String>,
  @JvmField val label: String?,
  @JvmField val systemsName: String?
)