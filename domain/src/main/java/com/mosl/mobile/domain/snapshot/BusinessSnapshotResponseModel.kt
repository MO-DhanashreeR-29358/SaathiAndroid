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

data class BusinessSnapshotCombo(
  val headers: List<String>,
  val data: List<BusinessSnapshotData>
)

data class ColHeader(
  val label: String,
  val index: Int = 0
)