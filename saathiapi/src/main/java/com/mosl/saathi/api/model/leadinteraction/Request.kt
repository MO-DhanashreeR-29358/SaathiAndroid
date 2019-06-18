package com.mosl.saathi.api.model.leadinteraction

import com.google.gson.annotations.SerializedName

data class Request(

  @JvmField @SerializedName("TotalCount")
  val totalCount: Int? = 0,

  @JvmField @SerializedName("PageSize")
  val pageSize: String? = "50",

  @JvmField @SerializedName("CalculatedPage")
  val calculatedPage: Int? = 0,

  @JvmField @SerializedName("LeadId")
  var leadId: String? = "0",

  @JvmField @SerializedName("Page")
  val page: Int? = 1,

  @JvmField @SerializedName("UCID")
  var uCID: String? = "0"
)