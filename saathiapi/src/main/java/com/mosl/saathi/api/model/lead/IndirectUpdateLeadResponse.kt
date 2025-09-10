package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class IndirectUpdateLeadResponse(
  @JvmField @SerializedName("ApprovalMessage") val approvalMessage: String? = null,
  @JvmField @SerializedName("CreateMeeting") val createMeeting: Int
)