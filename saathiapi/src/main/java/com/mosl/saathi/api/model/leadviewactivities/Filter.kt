package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName

data class Filter(

  @JvmField @SerializedName("Operator") val operator: Any,

  @JvmField @SerializedName("Field") val field: Any,

  @JvmField @SerializedName("Filters") val filters: Any,

  @JvmField @SerializedName("ExportFilter") val exportFilter: Any,

  @JvmField @SerializedName("Value") val value: Any,

  @JvmField @SerializedName("Export") val export: Any,

  @JvmField @SerializedName("Logic") val logic: Any
)