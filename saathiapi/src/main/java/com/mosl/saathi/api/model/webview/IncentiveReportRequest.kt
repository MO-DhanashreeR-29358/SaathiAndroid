package com.mosl.saathi.api.model.webview

import com.google.gson.annotations.SerializedName

data class IncentiveReportRequest(
  @JvmField @SerializedName("Id")
  val empCode: String
)