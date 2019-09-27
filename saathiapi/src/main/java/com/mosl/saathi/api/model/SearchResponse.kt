package com.mosl.saathi.api.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
  @JvmField @SerializedName("MetaData") val metaData: String
)