package com.mosl.saathi.api.model.dashboard.client

import com.google.gson.annotations.SerializedName

data class AddtionalParameters(
  @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String,
  @JvmField @SerializedName("flt_period") val fltPeriod: String
)