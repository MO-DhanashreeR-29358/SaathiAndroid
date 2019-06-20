package com.mosl.saathi.api.model.leadprofile

import com.google.gson.annotations.SerializedName

data class LeadProfileRequest(
  @JvmField @SerializedName("id") val id: String
)