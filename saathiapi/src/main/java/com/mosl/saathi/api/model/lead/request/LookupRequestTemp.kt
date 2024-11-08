package com.mosl.saathi.api.model.lead.request

import com.google.gson.annotations.SerializedName

data class LookupRequestTemp(
    @JvmField @SerializedName("BusinessUnitId") val businessUnitId: String,

    @JvmField @SerializedName("LookupTypeId") val lookupTypeId: Int,

    @JvmField @SerializedName("ParentId") val parentId: String? = null,

    @JvmField @SerializedName("IgnoreBusinessUnit") val IgnoreBusinessUnit: Boolean? = null,

    @JvmField @SerializedName("LookUpid") val lookupId:Int,

    @JvmField @SerializedName("LookUpName") val lookUpName: String? = null,
    @JvmField @SerializedName("LookupType") val lookupType:Int,

    @JvmField @SerializedName("Sequence") val sequence:Int,

    @JvmField @SerializedName("Status") val status:Int,

    @JvmField @SerializedName("Active") val active:Boolean,

    @JvmField @SerializedName("SysName") val sysName:String?=null,
)
