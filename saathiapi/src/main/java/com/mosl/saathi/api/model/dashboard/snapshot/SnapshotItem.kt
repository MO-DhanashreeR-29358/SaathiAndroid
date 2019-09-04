package com.mosl.saathi.api.model.dashboard.snapshot

import com.google.gson.annotations.SerializedName

data class SnapshotItem(
  @JvmField @SerializedName("Table") val table: List<SnapshotTableItem>?
)