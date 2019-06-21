package com.mosl.saathi.api.model.clientprofile

import com.google.gson.annotations.SerializedName

data class ClientProfileResponse(
  @JvmField @SerializedName("ClientPotential") val clientPotential: String,
  @JvmField @SerializedName("ApprovalMessage") val approvalMessage: String?
)