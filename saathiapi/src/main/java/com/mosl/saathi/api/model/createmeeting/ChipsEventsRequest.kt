package com.mosl.saathi.api.model.createmeeting

import com.google.gson.annotations.SerializedName

data class ChipsEventsRequest(
  @JvmField @SerializedName("employeeCode") val employeeCode: String
)