package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovelFieldRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String
)