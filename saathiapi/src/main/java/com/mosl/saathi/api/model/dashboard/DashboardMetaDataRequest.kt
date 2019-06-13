package com.mosl.saathi.api.model.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardMetaDataRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String?,
  @JvmField @SerializedName("ProductId") val productId: String?,
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String?
)