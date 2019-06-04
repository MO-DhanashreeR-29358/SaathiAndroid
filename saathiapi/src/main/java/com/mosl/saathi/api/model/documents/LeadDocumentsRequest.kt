package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class LeadDocumentsRequest(

  @JvmField @SerializedName("TotalCount") val totalCount: String,

  @JvmField @SerializedName("PageSize") val pageSize: String,

  @JvmField @SerializedName("CalculatedPage") val calculatedPage: String,

  @JvmField @SerializedName("LeadID") val leadID: String,

  @JvmField @SerializedName("Page") val page: String,

  @JvmField @SerializedName("UCID") val uCID: String
)