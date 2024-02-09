package com.mosl.user.data.login.model

import com.google.gson.annotations.SerializedName

data class CheckAppUpdateResponse(
  @JvmField @SerializedName("Channel") val Channel: String,

  @JvmField @SerializedName("AppVersion") val appVersion: String,

  @JvmField @SerializedName("IsMandatory") val isMandatory: Boolean,

  @JvmField @SerializedName("Url") val url: String,

  @JvmField @SerializedName("ReleaseNotes") val releaseNotes: String,

  @JvmField @SerializedName("Status") val status: Boolean
)