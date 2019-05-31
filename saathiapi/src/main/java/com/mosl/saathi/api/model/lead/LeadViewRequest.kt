package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LeadViewRequest(
  @SerializedName("id")
  @JvmField val id: Int
)