package com.mosl.saathi.api.model.dashboard.snapshot

import com.google.gson.annotations.SerializedName

data class SnapshotTableItem(
  @JvmField @SerializedName("MTD_CNT") val mTDCNT: String?,
  @JvmField @SerializedName("Parameter") val parameter: String?,
  @JvmField @SerializedName("YTD_CNT") val yTDCNT: String?
)