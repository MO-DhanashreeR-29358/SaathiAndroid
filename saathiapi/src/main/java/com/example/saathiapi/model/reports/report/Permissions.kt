package com.example.saathiapi.model.reports.report

import com.google.gson.annotations.SerializedName

data class Permissions(

  @SerializedName("Read")
  @JvmField val read: Boolean,

  @SerializedName("Write")
  @JvmField val write: Boolean,

  @SerializedName("BusinessProfileId")
  @JvmField val businessProfileId: Int,

  @SerializedName("Export")
  @JvmField val export: Boolean,

  @SerializedName("SystemName")
  @JvmField val systemName: Any
)