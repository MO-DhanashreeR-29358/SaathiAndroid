package com.mosl.tabapp.dashboard.report.hierarchy

data class DashboardHierarchyReportData(
  @JvmField val additionalParametersMap: Map<String, String>,
  @JvmField val label: String?,
  @JvmField val systemsName: String?

)