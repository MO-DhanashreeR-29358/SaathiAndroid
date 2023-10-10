package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationDetailRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("Period") val period: String,
    @JvmField @SerializedName("CurrentStage") val currentStage: String,
    @JvmField @SerializedName("Source") val source: String,
    @JvmField @SerializedName("Page") val page: String = "1",
    @JvmField @SerializedName("PageSize") val pageSize: String = "0"

)