package com.mosl.saathi.api.model.aubankmeeting

import com.google.gson.annotations.SerializedName

data class BankMeetingVerifyOTPRequest(
  @JvmField @SerializedName("BankId") val bankId: Int,
  @JvmField @SerializedName("BankBranchCode") val bankBranchCode: String,
  @JvmField @SerializedName("OTP") val otp: Int
)