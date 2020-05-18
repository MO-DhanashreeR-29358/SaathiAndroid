package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class SearchRefereeResponse(
  @JvmField @SerializedName("RefereeCode") val code: String?,
  @JvmField @SerializedName("RefereeCode") val name: String?,
  @JvmField @SerializedName("RefereeCode") val email: String?,
  @JvmField @SerializedName("RefereeCode") val mobile: String?
)