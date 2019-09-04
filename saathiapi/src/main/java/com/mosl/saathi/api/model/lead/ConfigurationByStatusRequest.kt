package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class ConfigurationByStatusRequest(
  @JvmField @SerializedName("Id") val id: String
)