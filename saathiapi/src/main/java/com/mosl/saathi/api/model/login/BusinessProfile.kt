package com.mosl.saathi.api.model.login

import com.google.gson.annotations.SerializedName

data class BusinessProfile(
  @SerializedName("BranchCode") @JvmField val branchCode: String?,
  @SerializedName("Id") @JvmField val userId: String?
)