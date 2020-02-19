package com.mosl.saathi.api.model.dashboard.target

import com.google.gson.annotations.SerializedName

data class TargetItemResponse(

    @JvmField @SerializedName("Table")
    val table: List<TableItem>?,

    @JvmField @SerializedName("Table1")
    val table1: List<TargetTable1Item>?
)