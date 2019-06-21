package com.mosl.saathi.api.model.assignto

import com.google.gson.annotations.SerializedName

data class UserBusinessProfileRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
  @JvmField @SerializedName("EmployeeName") val employeeName: String
)