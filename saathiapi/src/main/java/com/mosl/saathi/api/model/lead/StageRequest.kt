package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class StageRequest(

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String
)