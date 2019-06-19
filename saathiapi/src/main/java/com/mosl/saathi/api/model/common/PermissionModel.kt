package com.mosl.saathi.api.model.common

import com.google.gson.annotations.SerializedName

data class PermissionModel(
  @JvmField @SerializedName("Read") val read: Boolean,
  @JvmField @SerializedName("Write") val write: Boolean,
  @JvmField @SerializedName("BusinessProfileId") val businessProfileId: Int,
  @JvmField @SerializedName("Export") val export: Boolean,
  @JvmField @SerializedName("SystemName") val systemName: String?
)