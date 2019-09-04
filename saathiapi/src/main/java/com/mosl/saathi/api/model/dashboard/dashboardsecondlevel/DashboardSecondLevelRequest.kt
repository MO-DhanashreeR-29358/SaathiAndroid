package com.mosl.saathi.api.model.dashboard.dashboardsecondlevel

import com.google.gson.annotations.SerializedName

data class DashboardSecondLevelRequest(
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: Map<String, String>?,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String,
  @JvmField @SerializedName("PageSize") val pageSize: String?,
  @JvmField @SerializedName("Page") val page: String?
)