package com.mosl.saathi.saathiapi.model.pendingapproval

import com.google.gson.annotations.SerializedName

class ApprovalSummaryByEmployeeItem(
  @JvmField @SerializedName("PandingCount") val pendingCount: String?,

  @JvmField @SerializedName("ReferenceName") val approvedCount: String?,

  @JvmField @SerializedName("EntityField") val entityField: String?
)