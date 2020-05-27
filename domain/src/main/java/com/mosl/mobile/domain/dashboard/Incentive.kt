package com.mosl.mobile.domain.dashboard

data class Incentive(
  @JvmField val tableData: List<IncentiveTableData>,
  @JvmField val tableData1: List<IncentiveTableData1>
)

data class IncentiveTableData(
  @JvmField val q1: Float,
  @JvmField val q2: Float,
  @JvmField val q3: Float,
  @JvmField val q4: Float,
  @JvmField val parameters: String,
  @JvmField val ytd: Float
)

data class IncentiveTableData1(
  @JvmField val q1: String,
  @JvmField val q2: String,
  @JvmField val q3: String,
  @JvmField val q4: String,
  @JvmField val name: String,
  @JvmField val widgetType: String,
  @JvmField val ytd: String?
)

data class IncentivePolicy(
  @JvmField val filePath: String
)