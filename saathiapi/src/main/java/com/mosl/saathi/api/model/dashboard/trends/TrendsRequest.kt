package com.mosl.saathi.api.model.dashboard.trends

import com.google.gson.annotations.SerializedName

data class TrendsRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String,
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: Map<String, String>? = null,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)