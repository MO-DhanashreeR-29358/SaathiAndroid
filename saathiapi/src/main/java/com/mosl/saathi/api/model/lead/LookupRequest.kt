package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LookupRequest(
  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: String,

  @SerializedName("LookupTypeId")
  @JvmField val lookupTypeId: Int
)