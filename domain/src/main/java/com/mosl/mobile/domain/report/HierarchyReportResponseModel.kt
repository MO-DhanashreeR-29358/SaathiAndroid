package com.mosl.mobile.domain.report

data class HierarchyReportData(
  @JvmField val data: List<String>,
  @JvmField val headers: List<String>,
  @JvmField val hasMap: Map<String, String>
)

data class HierarchyReportCombo(
  val headers: List<String>,
  val data: List<HierarchyReportData>
)

data class ColHeader(
  val label: String,
  val index: Int = 0
)