package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class UpdateLeadResponse(
  @JvmField @SerializedName("ApprovalMessage") val approvalMessage: String? = null
)