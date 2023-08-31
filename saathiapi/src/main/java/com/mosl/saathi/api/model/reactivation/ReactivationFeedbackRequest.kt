package com.mosl.saathi.api.model.reactivation

import com.google.gson.annotations.SerializedName

data class ReactivationFeedbackRequest(
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("Period") val period: String,
    @JvmField @SerializedName("CurrentStage") val currentStage: String,
    @JvmField @SerializedName("RefNo") val refNo: String,
    @JvmField @SerializedName("Remarks") val remarks: String,
)
