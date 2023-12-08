package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class ChipsEventResponse(

  @JvmField @SerializedName("eventName") val eventName: String,
  @JvmField @SerializedName("eventNameText") val eventNameText: String
)