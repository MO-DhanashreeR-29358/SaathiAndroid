package com.mosl.saathi.api.model.dashboard.client

import com.google.gson.annotations.SerializedName

data class AddtionalParameters(
  @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String,
  @JvmField @SerializedName("flt_period") val fltPeriod: String
)

data class BusinessSnapAdditionalParameterRequest(
  @JvmField @SerializedName("flt_hierarchy") val fltHierarchy: String?,
  @JvmField @SerializedName("flt_period") val fltPeriod: String?,
  @JvmField @SerializedName("flt_prospect") val fltProspect: String?,
  @JvmField @SerializedName("flt_selfbranch") val fltSelfBranch: String?
)