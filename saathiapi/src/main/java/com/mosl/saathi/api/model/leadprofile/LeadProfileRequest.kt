package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class LeadProfileRequest(
  @SerializedName("id")
  val id: Int?
)