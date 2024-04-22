package com.mosl.user.data.login.model

import com.google.gson.annotations.SerializedName

data class DisprzAuthenticationRequest(
  @JvmField
  @SerializedName("stringParameter")
  val userName: String
)