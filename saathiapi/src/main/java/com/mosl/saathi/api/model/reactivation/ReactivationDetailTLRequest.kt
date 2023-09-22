package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationDetailTLRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("FirstReportingTo") val firstReportingTo: String,
    @JvmField @SerializedName("Period") val period: String,
    @JvmField @SerializedName("CurrentStage") val currentStage: String,
    @JvmField @SerializedName("Page") val page: String = "1",
    @JvmField @SerializedName("PageSize") val pageSize: String = "0"
)