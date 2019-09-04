package com.mosl.saathi.api.model.aubankmeetingreport

import com.google.gson.annotations.SerializedName

data class MeetingReportRequest(
  @JvmField @SerializedName("Request")
  var request: MeetingRequest = MeetingRequest(),

  @JvmField @SerializedName("Filter")
  val filter: Filter = Filter(),

  @JvmField @SerializedName("IsAdminMode")
  val isAdminMode: Boolean = true
)