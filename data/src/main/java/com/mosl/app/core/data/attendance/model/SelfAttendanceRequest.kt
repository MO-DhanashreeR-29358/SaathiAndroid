package com.mosl.app.core.data.attendance.model

import com.google.gson.annotations.SerializedName

data class SelfAttendanceRequest(
  @JvmField @SerializedName("FromDate") val fromDate: String,

  @JvmField @SerializedName("Todate") val toDate: String
)