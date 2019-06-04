package com.mosl.saathi.api.model.logout

import com.google.gson.annotations.SerializedName

data class LogoutRequest(
  @SerializedName("id") @JvmField val userId: String,
  @SerializedName("token") @JvmField val token: String,
  @SerializedName("clientinfo") @JvmField val clientInfo: String,
  @SerializedName("ipaddress") @JvmField val ipAddress: String,
  @SerializedName("BusinessUnitId") @JvmField val businessUnitId: String,
  @SerializedName("macaddress") @JvmField val macAddress: String
)