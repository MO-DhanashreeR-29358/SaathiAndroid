package com.mosl.saathi.api.model.dashboard.actionablecount

import com.google.gson.annotations.SerializedName

data class ActionableCountRequest(
  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: Map<String, String>?,
  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String
)