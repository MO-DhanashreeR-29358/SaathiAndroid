package com.mosl.saathi.api.model.advanceSearch

import com.google.gson.annotations.SerializedName

data class AdvanceSearchCommonResponse (

   @JvmField @SerializedName("Errors")
    val errors: Any? = null,

   @JvmField @SerializedName("Total")
    val total: Int? = null,

   @JvmField @SerializedName("ObjectData")
    val objectData: Any? = null,

   @JvmField @SerializedName("Permissions")
    val permissions: Permissions? = null,

   @JvmField @SerializedName("Data")
    val data:  ArrayList<AdvanceSearchData?> = arrayListOf(),

   @JvmField @SerializedName("ExtraData")
    val extraData: Any? = null
)

data class Permissions(

   @JvmField @SerializedName("Read")
    val read: Boolean? = null,

   @JvmField @SerializedName("Write")
    val write: Boolean? = null,

   @JvmField @SerializedName("BusinessProfileId")
    val businessProfileId: Int? = null,

   @JvmField @SerializedName("Export")
    val export: Boolean? = null,

   @JvmField @SerializedName("SystemName")
    val systemName: Any? = null
)