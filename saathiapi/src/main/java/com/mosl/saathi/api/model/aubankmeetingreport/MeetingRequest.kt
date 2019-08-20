package com.mosl.saathi.api.model.aubankmeetingreport

import com.google.gson.annotations.SerializedName

data class MeetingRequest(
  @JvmField @SerializedName("TotalCount") val totalCount: Int = 0,
  @JvmField @SerializedName("PageSize") val pageSize: Int = 50,
  @JvmField @SerializedName("CalculatedPage") val calculatedPage: Int = 0,
  @JvmField @SerializedName("Export") val export: Boolean = true,
  @JvmField @SerializedName("FromDate") val fromDate: String = "",
  @JvmField @SerializedName("ToDate") val toDate: String = "",
  @JvmField @SerializedName("Page") val page: Int = 1
)