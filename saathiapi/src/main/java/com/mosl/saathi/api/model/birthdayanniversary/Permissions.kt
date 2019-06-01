package com.mosl.saathi.api.model.birthdayanniversary

import com.google.gson.annotations.SerializedName

data class Permissions(

  @JvmField @SerializedName("Read")
  val read: Boolean,

  @JvmField @SerializedName("Write")
  val write: Boolean,

  @JvmField @SerializedName("BusinessProfileId")
  val businessProfileId: Int,

  @JvmField @SerializedName("Export")
  val export: Boolean,

  @JvmField @SerializedName("SystemName")
  val systemName: Any
)