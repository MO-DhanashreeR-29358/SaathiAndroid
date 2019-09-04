package com.mosl.saathi.api.model.documents

import com.google.gson.annotations.SerializedName

data class FilterRequest(

  @JvmField @SerializedName("Operator") val operator: String?,

  @JvmField @SerializedName("Field") val field: String?,

  @JvmField @SerializedName("Filters") val filters: String?,

  @JvmField @SerializedName("ExportFilter") val exportFilter: String?,

  @JvmField @SerializedName("Value") val value: String?,

  @JvmField @SerializedName("Export") val export: String?,

  @JvmField @SerializedName("Logic") val logic: String?
)