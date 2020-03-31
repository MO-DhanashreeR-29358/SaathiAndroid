package com.mosl.saathi.api.model.dashboard.lead.report

import com.google.gson.annotations.SerializedName

data class LeadReportResponse(

  @JvmField @SerializedName("Table")
  val table: String?,

  @JvmField @SerializedName("Table2")
  val table2: String?
)