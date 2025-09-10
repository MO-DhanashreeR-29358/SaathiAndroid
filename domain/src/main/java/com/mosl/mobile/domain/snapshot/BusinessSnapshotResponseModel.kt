package com.mosl.mobile.domain.snapshot

data class BusinessSnapshotResponseModel(
  @JvmField val table: List<BusinessSnapshotTableItemModel>
)

data class BusinessSnapshotTableItemModel(
  @JvmField val mtdCount: String?,
  @JvmField val parameter: String?,
  @JvmField val ytdCount: String?
)

data class BusinessSnapshotData(
  @JvmField val data: List<String>,
  @JvmField val headers: List<String>,
  @JvmField val hasMap: Map<String, String>
)

data class AdvanceSearchData(
  @JvmField val data: List<Any>,
  @JvmField val headers: List<String>,
  @JvmField val hasMap: Map<String, Any>
)


data class BusinessSnapshotCombo(
  val headers: List<String>,
  val data: List<BusinessSnapshotData>
)

data class AdvanceSearchDataCombo(
  val headers: List<Any>,
  val data: List<AdvanceSearchData>
)
data class ColHeader(
  val label: String,
  val index: Int = 0
)