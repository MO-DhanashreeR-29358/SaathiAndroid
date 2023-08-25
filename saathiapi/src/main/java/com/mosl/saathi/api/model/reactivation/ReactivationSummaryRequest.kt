package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationSummaryRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("Period") val period: String
)