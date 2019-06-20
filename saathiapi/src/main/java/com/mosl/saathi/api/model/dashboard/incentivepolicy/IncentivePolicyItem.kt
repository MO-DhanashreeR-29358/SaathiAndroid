package com.mosl.saathi.api.model.dashboard.incentivepolicy

import com.google.gson.annotations.SerializedName

data class IncentivePolicyItem(
  @JvmField @SerializedName("FilePath")
  val filePath: String?
)