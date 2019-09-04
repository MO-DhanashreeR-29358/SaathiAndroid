package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class Request(

  @JvmField @SerializedName("daytype")
  val daytype: String,

  @JvmField @SerializedName("TotalCount")
  val totalCount: String = "0",

  @JvmField @SerializedName("PageSize")
  val pageSize: String,

  @JvmField @SerializedName("Occasion")
  val occasion: String,

  @JvmField @SerializedName("CalculatedPage")
  val calculatedPage: String = "0",

  @JvmField @SerializedName("Page")
  val page: String = "1"
)