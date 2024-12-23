package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class BackgroundItem(

  @SerializedName("BG_ID")
  @JvmField val backgroundId: String?,

  @SerializedName("BG_NAME")
  @JvmField val backgroundName: String,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("InApproval")
  @JvmField val inApproval: Boolean,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String

)