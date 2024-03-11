package com.mosl.mobile.user.model

data class VersionResponse(
  @JvmField val statusCode: Int,
  @JvmField val msg: String,
  @JvmField val url: String?,
  @JvmField val newEkyc: Int,
  @JvmField val newEkycUrl: String,
  @JvmField val isOtpRequired:String
) {
  fun hasUrl(): Boolean {
    return url.isNullOrEmpty() && url.isNullOrBlank()
  }

  fun isNewEkyc(): Boolean {
    return newEkyc == 1
  }
}
