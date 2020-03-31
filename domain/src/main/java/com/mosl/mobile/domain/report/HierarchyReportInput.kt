package com.mosl.mobile.domain.report

data class HierarchyReportInput(
  @JvmField val totalCount: Int = 0,

  @JvmField val periodFrom: String?,

  @JvmField val isExportToExcel: Boolean,

  @JvmField val employeeCode: String?,

  @JvmField val pageSize: String?,

  @JvmField val calculatedPage: Int = 0,

  @JvmField val inputTotalCount: Int = 0,

  @JvmField val page: Int = 0,

  @JvmField val reportTypeSystemName: String?,

  @JvmField val businessChannel: String = "-1",

  @JvmField val periodTo: String?,

  @JvmField val branchCode: String?
)