package com.mosl.saathi.api.model.clientviewhistory

import com.google.gson.annotations.SerializedName

data class Filter(

  @JvmField @SerializedName("Operator")
  val operator: Any? = null,

  @JvmField @SerializedName("Field")
  val field: Any? = null,

  @JvmField @SerializedName("Filters")
  val filters: Any? = null,

  @JvmField @SerializedName("ExportFilter")
  val exportFilter: Any? = null,

  @JvmField @SerializedName("Value")
  val value: Any? = null,

  @JvmField @SerializedName("Export")
  val export: Any? = null,

  @JvmField @SerializedName("Logic")
  val logic: Any? = null
)