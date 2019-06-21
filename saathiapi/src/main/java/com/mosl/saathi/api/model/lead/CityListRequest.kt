package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class CityListRequest(
  @JvmField @SerializedName("StateId") val stateId: String
)