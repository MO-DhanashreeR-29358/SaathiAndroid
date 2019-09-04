package com.mosl.saathi.api.model.fcm_token

import com.google.gson.annotations.SerializedName

data class FcmTokenRequest(
  @SerializedName("DeviceToken") @JvmField val deviceToken: String,
  @SerializedName("DeviceId") @JvmField val deviceId: String
)