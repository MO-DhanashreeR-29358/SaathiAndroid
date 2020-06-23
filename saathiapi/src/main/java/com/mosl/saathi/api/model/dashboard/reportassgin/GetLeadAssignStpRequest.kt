package com.mosl.saathi.api.model.dashboard.reportassgin

import com.google.gson.annotations.SerializedName

data class GetLeadAssignStpRequest(

    @JvmField @SerializedName("PageSize")
    val pageSize: Int,

    @JvmField @SerializedName("PageIndex")
    val pageIndex: Int,

    @JvmField @SerializedName("CalculatedPage")
    val calculatedPage: Int
)