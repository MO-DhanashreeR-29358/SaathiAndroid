package com.mosl.saathi.api.model.pendingapproval

import com.google.gson.annotations.SerializedName

data class PendingApprovalRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String
)