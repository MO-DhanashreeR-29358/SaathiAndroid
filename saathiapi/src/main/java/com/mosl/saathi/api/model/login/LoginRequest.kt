package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(
  @SerializedName("EmployeeCode") @JvmField val employeeCode: String,
  @SerializedName("Password") @JvmField val password: String,
  @SerializedName("IPAddress") @JvmField val ipAddress: String,
  @SerializedName("ClientInfo") @JvmField val clientInfo: String,
  @SerializedName("Macaddress") @JvmField val macAddress: String,
  @SerializedName("AppId") @JvmField val appId: String
)