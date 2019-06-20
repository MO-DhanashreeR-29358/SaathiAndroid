package com.mosl.saathi.api.model.prospect

import com.google.gson.annotations.SerializedName

data class SuggestMeRequest(
  @SerializedName("UCID") @JvmField val mobile: Int,
  @SerializedName("LEAD_ID") @JvmField val email: Int
)