package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName

data class AppointmentRequest(

  @SerializedName("Request") val request: Request,

  @SerializedName("Filter") val filter: Filter,

  @SerializedName("IsAdminMode") val isAdminMode: Boolean
)