package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovalUserItem(

  @JvmField @SerializedName("UpdatedBy") val updatedBy: String?,

  @JvmField @SerializedName("Name") val name: String?
)