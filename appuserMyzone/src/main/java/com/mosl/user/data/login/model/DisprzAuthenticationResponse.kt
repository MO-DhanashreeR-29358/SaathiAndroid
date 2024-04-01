package com.mosl.user.data.login.model

import com.google.gson.annotations.SerializedName

data class DisprzAuthenticationResponse(
  @JvmField @SerializedName("status") val status: String,

  @JvmField @SerializedName("url") val url: String,

  @JvmField @SerializedName("mobileUrl") val mobileUrl: String,

  @JvmField @SerializedName("userName") val userName: String,

  @JvmField @SerializedName("firstName") val firstName: String,

  @JvmField @SerializedName("lastName") val lastName: String,

  @JvmField @SerializedName("email") val email: String
)