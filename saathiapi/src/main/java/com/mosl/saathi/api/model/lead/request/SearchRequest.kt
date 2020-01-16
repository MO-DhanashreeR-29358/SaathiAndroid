package com.mosl.saathi.api.model.lead.request

import com.google.gson.annotations.SerializedName

data class SearchRequest(
  @JvmField @SerializedName("SearchText") val searchText: String,
  @JvmField @SerializedName("SearchType") val searchType: String,
  @JvmField @SerializedName("ViewAll") val viewAll: Boolean = false
)