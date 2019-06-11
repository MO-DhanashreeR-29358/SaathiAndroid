package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class BrokingGainLossItem(
  @JvmField @SerializedName("ASSET_SUB_TYPE_ID") val assetSubTypeId: String,

  @JvmField @SerializedName("PRODUCT") val product: String,

  @JvmField @SerializedName("ST_GAIN") val stGain: String,

  @JvmField @SerializedName("LT_GAIN") val ltGain: String,

  @JvmField @SerializedName("TOTAL_GAIN") val totalGain: String
)