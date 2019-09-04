package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class MultiUpdateApprovalRequest(
  @JvmField @SerializedName("status") val status: String,
  @JvmField @SerializedName("ApprovalReqiestIds") val approvalRequestId: String
)