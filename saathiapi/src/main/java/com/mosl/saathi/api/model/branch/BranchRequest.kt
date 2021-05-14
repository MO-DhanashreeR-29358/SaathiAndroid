package com.mosl.saathi.api.model.branch

import com.google.gson.annotations.SerializedName

data class BranchRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String
)