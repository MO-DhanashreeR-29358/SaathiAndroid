package com.mosl.saathi.api.model.attendance

import com.google.gson.annotations.SerializedName

data class BranchLocationResponse(
  @JvmField @SerializedName("Latitude") val latitide: String?,
  @JvmField @SerializedName("Longitude") val longitude: String?,
  @JvmField @SerializedName("BranchName") val branchName: String,
  @JvmField @SerializedName("BranchId") val branchId: Int
)