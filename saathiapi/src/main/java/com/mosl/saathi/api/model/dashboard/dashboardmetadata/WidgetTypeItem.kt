package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class WidgetTypeItem(

  @JvmField @SerializedName("Status")
  val status: Int?,

  @JvmField @SerializedName("Order")
  val order: Int?,

  @JvmField @SerializedName("WidgetTypes")
  val widgetTypes: String?,

  @JvmField @SerializedName("Title")
  val title: String?,

  @JvmField @SerializedName("WidgetId")
  val widgetId: Int?
)