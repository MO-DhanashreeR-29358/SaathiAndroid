package com.mosl.saathi.api.model.fcm_token

import com.google.gson.annotations.SerializedName

data class FcmTokenResponse(
  @SerializedName("Status")
  val status: String,

  @SerializedName("Response")
  val response: String,

  @SerializedName("Type")
  val type: String,
  @SerializedName("Message")
  val message: String

) {
  fun isSuccess(): Boolean {
    return status.equals("true")
  }
}