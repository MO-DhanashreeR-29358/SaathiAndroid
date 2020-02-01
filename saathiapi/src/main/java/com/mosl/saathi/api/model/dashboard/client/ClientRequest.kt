package com.mosl.saathi.api.model.dashboard.client

import com.google.gson.annotations.SerializedName

data class ClientRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: Map<String, String>,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)