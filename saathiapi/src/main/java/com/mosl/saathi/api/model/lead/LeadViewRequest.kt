package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadViewRequest(
  @JvmField @SerializedName("id") val id: String,
  @JvmField @SerializedName("IsEdit") val isEdit: Int = 1
)