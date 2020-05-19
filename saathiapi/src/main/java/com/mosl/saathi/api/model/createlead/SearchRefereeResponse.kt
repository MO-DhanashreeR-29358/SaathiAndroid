package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class SearchRefereeResponse(
  @JvmField @SerializedName("RefereeCode") val code: String?,
  @JvmField @SerializedName("RefereeName") val name: String?,
  @JvmField @SerializedName("RefereeEmail") val email: String?,
  @JvmField @SerializedName("RefereeMobile") val mobile: String?
)