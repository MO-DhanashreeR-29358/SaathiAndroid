package com.mosl.mobile.domain.leadviewdetail.history

data class MappingHistoryData(
  @JvmField var data: List<String>
)

data class MappingHistoryCombo(
  val headers: List<String>,
  val data: List<MappingHistoryData>
)

data class ColHeader(
  val label: String,
  val index: Int = 0
)