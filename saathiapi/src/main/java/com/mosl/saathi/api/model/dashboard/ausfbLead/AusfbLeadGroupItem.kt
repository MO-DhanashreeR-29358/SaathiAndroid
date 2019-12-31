package com.mosl.saathi.api.model.dashboard.ausfbLead

import com.google.gson.annotations.SerializedName

data class AusfbLeadGroupItem(

    @JvmField @SerializedName("GroupId") val groupId: String,
    @JvmField @SerializedName("childid") val childId: String,
    @JvmField @SerializedName("RowFilter") val rowFilter: String?
)