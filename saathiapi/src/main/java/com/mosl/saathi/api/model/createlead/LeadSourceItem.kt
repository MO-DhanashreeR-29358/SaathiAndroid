package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class LeadSourceItem(
  @JvmField @SerializedName("LeadSourceID") val leadSourceID: String?,
  @JvmField @SerializedName("LeadSourceName") val leadSourceName: String?
)