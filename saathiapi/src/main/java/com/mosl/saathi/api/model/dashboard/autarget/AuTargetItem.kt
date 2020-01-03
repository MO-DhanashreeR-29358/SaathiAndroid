package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName

data class AuTargetItem(

    @JvmField @SerializedName("Table")
    val table: List<AuTableItem>?,

    @JvmField @SerializedName("Table1")
    val table1: List<AuTable1Item>?
)