package com.mosl.saathi.api.model.pendingforapproval

import com.google.gson.annotations.SerializedName

data class ApprovelFieldItem(
  @JvmField @SerializedName("EntityField") val entityField: String?,
  @JvmField @SerializedName("ReferenceName") val referenceName: String?
)