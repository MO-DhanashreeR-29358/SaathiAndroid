package com.mosl.saathi.api.model.dashboard.incentive

import com.google.gson.annotations.SerializedName

data class Table1Item(

  @JvmField @SerializedName("Q1")
  val q1: String?,

  @JvmField @SerializedName("Q2")
  val q2: String?,

  @JvmField @SerializedName("Q3")
  val q3: String?,

  @JvmField @SerializedName("Q4")
  val q4: String?,

  @JvmField @SerializedName("id")
  val id: Int?,

  @JvmField @SerializedName("WidgetType")
  val widgetType: String?,

  @JvmField @SerializedName("Name")
  val name: String?
)