package com.mosl.saathi.api.model.reports.report

import com.google.gson.annotations.SerializedName

data class LeadReportItem(

  @SerializedName("Errors")
  @JvmField val errors: Any,

  @SerializedName("Total")
  @JvmField val total: Int,

  @SerializedName("Permissions")
  @JvmField val permissions: Permissions,

  @SerializedName("Data")
  @JvmField val data: List<DataItem?>,

  @SerializedName("ExtraData")
  @JvmField val extraData: Any
)