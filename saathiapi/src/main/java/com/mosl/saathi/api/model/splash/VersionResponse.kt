package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class VersionResponse(
  @JvmField @SerializedName("StatusCode") val statusCode: Int,
  @JvmField @SerializedName("msg") val msg: String,
  @JvmField @SerializedName("URL") val url: String?,
  @JvmField @SerializedName("NEWEKYC") val newEkyc: Int,
  @JvmField @SerializedName("NEWEKYCURL") val newEkycUrl: String,
  @JvmField @SerializedName("IsOTPRequired") val isOtpRequired:String?
) {
  fun hasUrl(): Boolean {
    return !url.isNullOrEmpty() && !url.isNullOrBlank()
  }

  fun isNewEkyc(): Boolean {
    return newEkyc == 1
  }
}
