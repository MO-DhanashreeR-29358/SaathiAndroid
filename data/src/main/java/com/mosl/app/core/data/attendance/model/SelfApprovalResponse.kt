package com.mosl.app.core.data.attendance.model

import com.google.gson.annotations.SerializedName

data class SelfApprovalResponse(
  @JvmField @SerializedName("HEADING") val heading: String,

  @JvmField @SerializedName("COUNT") val count: String,

  @JvmField @SerializedName("URL") val url: String
)