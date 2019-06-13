package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class GroupsItem(

  @JvmField @SerializedName("GroupName")
  val groupName: String?,

  @JvmField @SerializedName("ParentId")
  val parentId: Any?,

  @JvmField @SerializedName("DisplayOrder")
  val displayOrder: Int?,

  @JvmField @SerializedName("ImageUrl2")
  val imageUrl2: Any?,

  @JvmField @SerializedName("AvailableFilter")
  val availableFilter: List<Any>?,

  @JvmField @SerializedName("ImageUrl")
  val imageUrl: Any?,

  @JvmField @SerializedName("SystemName")
  val systemName: String?,

  @JvmField @SerializedName("AvailableWidgets")
  val availableWidgets: List<AvailableWidgetsItem>?,

  @JvmField @SerializedName("DashBoardGroupId")
  val dashBoardGroupId: Int?,

  @JvmField @SerializedName("Children")
  val children: List<Any?>?
)