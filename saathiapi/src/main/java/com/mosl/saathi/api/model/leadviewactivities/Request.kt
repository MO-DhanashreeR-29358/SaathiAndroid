package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName

data class Request(

  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("PageSize") val pageSize: String,

  @JvmField @SerializedName("Page") val page: String,

  @JvmField @SerializedName("CalculatedPage") val calculatedPage: String,

  @JvmField @SerializedName("TotalCount") val totalCount: String,

  @JvmField @SerializedName("UCID") val uCID: String
)