package com.mosl.saathi.api.model.dashboard.hierarchy

import com.google.gson.annotations.SerializedName

data class DashboardHierarchyRequest(
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: Map<String, String>,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String
)