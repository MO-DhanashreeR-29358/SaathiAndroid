package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.model.TypeActionableFilterModel

data class FocoDashboardReportData(
  @JvmField val additionalMap: Map<String, String>,
  @JvmField val label: String?,
  @JvmField val systemsName: String?,
  @JvmField val periodFilter: List<FilterRowData>,
  @JvmField val typeFilter: List<TypeActionableFilterModel>,
  @JvmField val isDateFilter: Boolean
)