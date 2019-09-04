package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankEmployeeRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
  @JvmField @SerializedName("EmployeeName") val employeeName: String,
  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String,
  @JvmField @SerializedName("BranchCode") val branchCode: String
)