package com.mosl.mobile.domain.snapshot

data class BusinessSnapshotResponseModel(
  @JvmField val table: List<BusinessSnapshotTableItemModel>
)

data class BusinessSnapshotTableItemModel(
  @JvmField val mtdCount: String?,
  @JvmField val parameter: String?,
  @JvmField val ytdCount: String?
)