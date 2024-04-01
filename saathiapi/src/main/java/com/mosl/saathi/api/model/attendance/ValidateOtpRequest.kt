package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class ValidateOtpRequest(
  @JvmField @SerializedName("RequestId") val requestId: String,
  @JvmField @SerializedName("OTP") val oTP: Int
)