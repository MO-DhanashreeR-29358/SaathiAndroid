package com.mosl.saathi.api.model.advanceSearch

import com.google.gson.annotations.SerializedName

data class AdvanceSearchCommonResponse (

    @field:SerializedName("Errors")
    val errors: Any? = null,

    @field:SerializedName("Total")
    val total: Int? = null,

    @field:SerializedName("ObjectData")
    val objectData: Any? = null,

    @field:SerializedName("Permissions")
    val permissions: Permissions? = null,

    @field:SerializedName("Data")
    val data:  ArrayList<AdvanceSearchData?> = arrayListOf(),

    @field:SerializedName("ExtraData")
    val extraData: Any? = null
)

data class Permissions(

    @field:SerializedName("Read")
    val read: Boolean? = null,

    @field:SerializedName("Write")
    val write: Boolean? = null,

    @field:SerializedName("BusinessProfileId")
    val businessProfileId: Int? = null,

    @field:SerializedName("Export")
    val export: Boolean? = null,

    @field:SerializedName("SystemName")
    val systemName: Any? = null
)