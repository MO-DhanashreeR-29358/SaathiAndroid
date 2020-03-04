package com.mosl.saathi.api.model.dashboard.trends

import com.google.gson.annotations.SerializedName

data class TrendsTableItem(
  @JvmField @SerializedName("Parameters")
  val parameters: String,
  @JvmField @SerializedName("ChartType")
  val chartType: String,
  @JvmField @SerializedName("Oct")
  val oct: Float?,
  @JvmField @SerializedName("Nov")
  val nov: Float?,
  @JvmField @SerializedName("Dec")
  val dec: Float?,
  @JvmField @SerializedName("Jan")
  val jan: Float?,
  @JvmField @SerializedName("Feb")
  val feb: Float?,
  @JvmField @SerializedName("Mar")
  val mar: Float?

)