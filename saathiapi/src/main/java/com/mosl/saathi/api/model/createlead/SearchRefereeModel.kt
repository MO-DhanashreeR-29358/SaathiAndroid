package com.mosl.saathi.api.model.createlead

import com.google.gson.annotations.SerializedName

data class SearchRefereeModel(
  @JvmField @SerializedName("SearchType") val type: String,
  @JvmField @SerializedName("SearchHint") val hint: String
)