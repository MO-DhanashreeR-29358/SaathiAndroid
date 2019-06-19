package com.mosl.saathi.api.model.pendingapproval

import com.google.gson.annotations.SerializedName

data class ApprovalSummaryCountItem(

  @JvmField @SerializedName("PendingCount") val pendingCount: Int?,

  @JvmField @SerializedName("ApprovedCount") val approvedCount: Int?
)