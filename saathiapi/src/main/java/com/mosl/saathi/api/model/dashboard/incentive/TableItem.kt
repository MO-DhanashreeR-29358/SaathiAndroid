package com.mosl.saathi.api.model.dashboard.incentive

import com.google.gson.annotations.SerializedName

data class TableItem(

  @JvmField @SerializedName("Q1")
  val q1: Int?,

  @JvmField @SerializedName("Q2")
  val q2: Int?,

  @JvmField @SerializedName("Q3")
  val q3: Int?,

  @JvmField @SerializedName("Parameters")
  val parameters: String?,

  @JvmField @SerializedName("Q4")
  val q4: Int?,

  @JvmField @SerializedName("YTD")
  val yTD: Int?,

  @JvmField @SerializedName("id")
  val id: Int?,

  @JvmField @SerializedName("WidgetType")
  val widgetType: String?
)