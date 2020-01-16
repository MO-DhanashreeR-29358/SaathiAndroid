package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class AusfbLeadResponse(

    @JvmField @SerializedName("Table")
    val table: List<AusfbLeadItem>?,

    @JvmField @SerializedName("Table1")
    val table1: List<AusfbLeadItem>?,

    @JvmField @SerializedName("Table2")
    val table2: List<AusfbLeadGroupItem>?
)