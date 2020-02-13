package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class SubmitOTPResponse(
    @JvmField @SerializedName("LeadId") val leadId: String,
    @JvmField @SerializedName("UCID") val ucid: String,
    @JvmField @SerializedName("DeviceType") val deviceType: String,
    @JvmField @SerializedName("EmployeeCode") val employeeCode: String,
    @JvmField @SerializedName("OTP") val otp: String,
    @JvmField @SerializedName("validationMessage") val validationMessage: String,
    @JvmField @SerializedName("payload") val payLoad: String,
    @JvmField @SerializedName("IsAdminMode") val isAdminMode: String,
    @JvmField @SerializedName("Status") val id: Int,
    @JvmField @SerializedName("InApproval") val inApproval: Boolean

)