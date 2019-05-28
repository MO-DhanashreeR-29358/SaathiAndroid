package com.mosl.saathi.saathiapi.model.lead

import com.google.gson.annotations.SerializedName

data class LookupItem(

  @SerializedName("ParentId")
  @JvmField val parentId: Int,

  @SerializedName("Status")
  @JvmField val status: Int,

  @SerializedName("SysName")
  @JvmField val sysName: Any,

  @SerializedName("Active")
  @JvmField val active: Boolean,

  @SerializedName("LookupType")
  @JvmField val lookupType: Int,

  @SerializedName("LookUpid")
  @JvmField val lookUpid: Int,

  @SerializedName("LookUpName")
  @JvmField val lookUpName: String,

  @SerializedName("BusinessUnitId")
  @JvmField val businessUnitId: Int,

  @SerializedName("Sequence")
  @JvmField val sequence: Int,

  @SerializedName("IgnoreBusinessUnit")
  @JvmField val ignoreBusinessUnit: Boolean,

  @SerializedName("LookupTypeId")
  @JvmField val lookupTypeId: Int
)