package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class MeetingByIdRequest(
  @JvmField @SerializedName("id") val id: String
)