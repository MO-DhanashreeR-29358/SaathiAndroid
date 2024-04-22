package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class TokenResponse(
  @JvmField @SerializedName("Token") val token: String,
  @JvmField @SerializedName("RefreshToken") val refreshToken: String,
  @JvmField @SerializedName("ExpiresIn") val expiresOn: Int
): HrmsErrorResponse()