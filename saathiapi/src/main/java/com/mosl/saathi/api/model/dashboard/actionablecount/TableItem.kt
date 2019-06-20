package com.mosl.saathi.api.model.dashboard.actionablecount

import com.google.gson.annotations.SerializedName

data class TableItem(
  @JvmField @SerializedName("Types") val types: String?,

  @JvmField @SerializedName("Projected") val projected: String?,

  @JvmField @SerializedName("Actual") val actual: String?
)