package com.mosl.saathi.api.model.dashboard.incentive

import com.google.gson.annotations.SerializedName

data class TableItem(

  @JvmField @SerializedName("Q1")
  val q1: Float?,

  @JvmField @SerializedName("Q2")
  val q2: Float?,

  @JvmField @SerializedName("Q3")
  val q3: Float?,

  @JvmField @SerializedName("Parameters")
  val parameters: String?,

  @JvmField @SerializedName("Q4")
  val q4: Float?,

  @JvmField @SerializedName("YTD")
  val yTD: Float?,

  @JvmField @SerializedName("id")
  val id: Float?,

  @JvmField @SerializedName("WidgetType")
  val widgetType: String?
)