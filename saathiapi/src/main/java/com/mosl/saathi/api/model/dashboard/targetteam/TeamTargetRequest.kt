package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName

data class TeamTargetRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: APTeamTargetRequest?,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String?,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)