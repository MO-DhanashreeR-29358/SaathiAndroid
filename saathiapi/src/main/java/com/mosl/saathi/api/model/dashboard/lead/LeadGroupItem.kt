package com.mosl.saathi.api.model.dashboard.lead

import com.google.gson.annotations.SerializedName

data class LeadGroupItem(

    @JvmField @SerializedName("GroupId") val groupId: String,
    @JvmField @SerializedName("childid") val childId: String,
    @JvmField @SerializedName("RowFilter") val rowFilter: String?
)