package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class GenerateOtpResponse(
  @JvmField @SerializedName("RequestId") val requestId: String,
  @JvmField @SerializedName("OTP") val oTP: String?
): HrmsErrorResponse()