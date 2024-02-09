package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class TokenRequest(
  @JvmField @SerializedName("Token") val token: String,
  @JvmField @SerializedName("RefreshToken") val refreshToken: String
)