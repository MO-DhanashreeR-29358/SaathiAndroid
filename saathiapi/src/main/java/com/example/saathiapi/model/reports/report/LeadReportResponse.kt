package com.example.saathiapi.model.reports.report

import com.google.gson.annotations.SerializedName

data class LeadReportResponse(

  @SerializedName("Response")
  @JvmField val response: Response,

  @SerializedName("Status")
  @JvmField val status: Boolean,

  @SerializedName("Type")
  @JvmField val type: String,

  @SerializedName("Message")
  @JvmField val message: String,

  @SerializedName("Erros")
  @JvmField val erros: List<Any?>
)