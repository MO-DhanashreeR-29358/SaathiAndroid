package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovalListItem(

  @JvmField @SerializedName("Errors") val errors: Any,

  @JvmField @SerializedName("Total") val total: Int,

  @JvmField @SerializedName("Permissions") val permissions: Permissions,

  @JvmField @SerializedName("Data") val data: List<DataItem?>,

  @JvmField @SerializedName("ExtraData") val extraData: Any
)