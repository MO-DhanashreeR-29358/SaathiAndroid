package com.mosl.saathi.api.model.lead.createmeeting

import com.google.gson.annotations.SerializedName

data class PostOTPForMeetingRequest(
  @JvmField @SerializedName("EmployeeCode") val employeeCode: Int,
  @JvmField @SerializedName("LeadId") val leadId: String,
  @JvmField @SerializedName("OTP") val oTP: String,
  @JvmField @SerializedName("UCID") val uCID: String,
  @JvmField @SerializedName("payload") val payLoad: Int
)