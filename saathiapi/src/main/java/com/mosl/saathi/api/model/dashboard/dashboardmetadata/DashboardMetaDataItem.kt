package com.mosl.saathi.api.model.dashboard.dashboardmetadata

import com.google.gson.annotations.SerializedName

data class DashboardMetaDataItem(

  @JvmField @SerializedName("DashboardFor")
  val dashboardFor: String?,

  @JvmField @SerializedName("BusinessProfileId")
  val businessProfileId: Int?,

  @JvmField @SerializedName("Groups")
  val groups: List<GroupsItem>?,

  @JvmField @SerializedName("SystemName")
  val systemName: Any?,

  @JvmField @SerializedName("Id")
  val id: Int?,

  @JvmField @SerializedName("Name")
  val name: String?
)