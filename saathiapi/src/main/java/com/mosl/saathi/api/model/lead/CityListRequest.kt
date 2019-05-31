package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class CityListRequest(
  @SerializedName("StateId")
  @JvmField val stateId: String
)