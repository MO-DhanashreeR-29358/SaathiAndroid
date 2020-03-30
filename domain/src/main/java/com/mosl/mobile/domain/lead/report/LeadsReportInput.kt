package com.mosl.mobile.domain.lead.report

data class LeadsReportInput(
  @JvmField val businessProfileId: String?,
  @JvmField val addtionalParameters: Map<String, String>,
  @JvmField val widgetSystemName: String?,
  @JvmField val productId: String?,
  @JvmField val employeeCode: String?
)
