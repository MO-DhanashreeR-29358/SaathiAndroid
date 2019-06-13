package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class AvailableWidgetsItem(

  @JvmField @SerializedName("WidgetFor")
  val widgetFor: String?,

  @JvmField @SerializedName("DisplayOrder")
  val displayOrder: Int?,

  @JvmField @SerializedName("PlaceHolder")
  val placeHolder: String?,

  @JvmField @SerializedName("ImageUrl2")
  val imageUrl2: String?,

  @JvmField @SerializedName("AvailableFilter")
  val availableFilter: List<AvailableFilterItem>?,

  @JvmField @SerializedName("ImageUrl")
  val imageUrl: String?,

  @JvmField @SerializedName("SystemName")
  val systemName: String?,

  @JvmField @SerializedName("Id")
  val id: Int?,

  @JvmField @SerializedName("WidgetType")
  val widgetType: List<Any>?,

  @JvmField @SerializedName("Name")
  val name: String?
)