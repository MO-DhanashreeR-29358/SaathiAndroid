package com.mosl.saathi.api.model.lead

import com.google.gson.annotations.SerializedName

data class LookupItem(

  @JvmField @SerializedName("ParentId") val parentId: Int,

  @JvmField @SerializedName("Status") val status: Int,

  @JvmField @SerializedName("SysName") val sysName: Any,

  @JvmField @SerializedName("Active") val active: Boolean,

  @JvmField @SerializedName("LookupType") val lookupType: Int,

  @JvmField @SerializedName("LookUpid") val lookUpid: String?,

  @JvmField @SerializedName("LookUpName") val lookUpName: String?,

  @JvmField @SerializedName("BusinessUnitId") val businessUnitId: Int,

  @JvmField @SerializedName("Sequence") val sequence: Int,

  @JvmField @SerializedName("IgnoreBusinessUnit") val ignoreBusinessUnit: Boolean,

  @JvmField @SerializedName("LookupTypeId") val lookupTypeId: Int
)