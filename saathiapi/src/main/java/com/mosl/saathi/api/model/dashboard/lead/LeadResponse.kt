package com.mosl.saathi.api.model.dashboard.lead

import com.google.gson.annotations.SerializedName

data class LeadResponse(

    @JvmField @SerializedName("Table")
    val table: List<LeadItem>?,

    @JvmField @SerializedName("Table1")
    val table1: List<LeadItem>?,

    @JvmField @SerializedName("Table2")
    val table2: List<LeadGroupItem>?
)