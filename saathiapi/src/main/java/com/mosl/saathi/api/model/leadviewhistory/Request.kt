package com.mosl.saathi.api.model.leadviewhistory

import com.google.gson.annotations.SerializedName

data class Request(

  @JvmField @SerializedName("TotalCount")
  val totalCount: Int = 0,

  @JvmField @SerializedName("PageSize")
  val pageSize: Int = 50,

  @JvmField @SerializedName("CalculatedPage")
  val calculatedPage: Int = 0,

  @JvmField @SerializedName("LeadID")
  var leadID: String = "0",

  @JvmField @SerializedName("Page")
  val page: Int = 1
)