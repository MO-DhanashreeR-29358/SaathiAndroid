package com.mosl.saathi.api.model.reports.report

import com.google.gson.annotations.SerializedName

data class AssignedLeadReportItem(
  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,
  @JvmField @SerializedName("BranchCode") val branchCode: String
)