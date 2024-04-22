package com.mosl.user.data.login.model

import com.google.gson.annotations.SerializedName

data class CheckAppUpdateRequest(
  @JvmField @SerializedName("appMarket") val appMarket: String = "AndroidMarket",

  @JvmField @SerializedName("appVersion") val appVersion: String
)