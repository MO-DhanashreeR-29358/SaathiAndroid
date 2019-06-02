package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LookupRequest(
  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
  @JvmField @SerializedName("LookupTypeId") val lookupTypeId: Int
)