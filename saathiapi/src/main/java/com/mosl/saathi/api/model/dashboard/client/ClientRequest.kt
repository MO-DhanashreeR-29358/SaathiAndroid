package com.mosl.saathi.api.model.dashboard.client

import com.google.gson.annotations.SerializedName

data class ClientRequest(
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: String,

  @JvmField @SerializedName("AddtionalParameters") val addtionalParameters: AddtionalParameters,

  @JvmField @SerializedName("WidgetSystemName") val widgetSystemName: String
)