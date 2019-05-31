package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class ConfigurationByStatusRequest(
  @SerializedName("Id") @JvmField val id: String
)