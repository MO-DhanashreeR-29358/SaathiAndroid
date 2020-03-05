package com.mosl.saathi.api.model.dashboard.clienttradedyesterday

import com.google.gson.annotations.SerializedName
import com.mosl.saathi.api.model.dashboard.client.AddtionalParameters

data class ClientTradeYesterdayRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: AddtionalParameters,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)