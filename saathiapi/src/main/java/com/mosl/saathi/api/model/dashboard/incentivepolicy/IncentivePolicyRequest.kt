package com.mosl.saathi.api.model.dashboard.incentivepolicy

import com.google.gson.annotations.SerializedName

data class IncentivePolicyRequest(
  @JvmField @SerializedName("BusinessProfileId")
  val businessProfileId: String?,

  @JvmField @SerializedName("EmployeeCode")
  val employeeCode: String?,

  @JvmField @SerializedName("DashboardId")
  val dashboardId: String?
)