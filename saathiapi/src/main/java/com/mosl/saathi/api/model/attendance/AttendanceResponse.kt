package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
  @JvmField @SerializedName("Data") val data: String
): HrmsErrorResponse()