package com.mosl.saathi.api.model.leadviewactivities

import com.google.gson.annotations.SerializedName

data class MeetingRequest(

  @JvmField @SerializedName("Request") val request: Request,

  @JvmField @SerializedName("Filter") val filter: Filter,

  @JvmField @SerializedName("IsAdminMode") val isAdminMode: Boolean
)