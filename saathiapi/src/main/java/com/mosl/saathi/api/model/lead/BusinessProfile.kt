package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class BusinessProfile(
    @JvmField @SerializedName("EmployeeName") val employeeName: String?,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String?,
    @JvmField @SerializedName("BranchCode") val branchCode: String?
)