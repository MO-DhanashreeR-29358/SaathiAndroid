package com.mosl.saathi.api.model.dashboard.actionablecount

import com.google.gson.annotations.SerializedName

data class TableItem(
  @JvmField @SerializedName("Type") val types: String?
)