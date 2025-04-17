package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankMeetingVerifyOTPRequest(
  @JvmField @SerializedName("BankId") val bankId: String,
  @JvmField @SerializedName("BankBranchCode") val bankBranchCode: String,
  @JvmField @SerializedName("OTP") val otp: String
)