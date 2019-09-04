package com.mosl.saathi.api.model.reports.report

import com.google.gson.annotations.SerializedName

data class AssignedLeadReportRequest(
  @SerializedName("ChannelIds")
  @JvmField val channelIds: String
)