package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationSourceRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String
)