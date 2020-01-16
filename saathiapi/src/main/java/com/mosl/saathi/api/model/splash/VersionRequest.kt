package com.mosl.saathi.api.model.splash

import com.google.gson.annotations.SerializedName

data class VersionRequest(
  @JvmField @SerializedName("ApplicationType") val applicationType: String,
  @JvmField @SerializedName("AppVersion") val appVersion: Int,
  @JvmField @SerializedName("RequestSource") val requestSource: String,
  @JvmField @SerializedName("DisplayVersion") val displayVersion: String
//  @JvmField @SerializedName("macaddress") val macAddress: String

)