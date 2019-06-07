package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName

data class Table1Item(

  @JvmField @SerializedName("formatted")
  val formatted: String?,

  @JvmField @SerializedName("label")
  val label: String?,

  @JvmField @SerializedName("WidgetType")
  val widgetType: String?,

  @JvmField @SerializedName("value")
  val value: String?
)