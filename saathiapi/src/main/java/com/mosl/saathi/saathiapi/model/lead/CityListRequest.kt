package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class CityListRequest(
  @SerializedName("StateId")
  @JvmField val stateId: String
)