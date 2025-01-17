package com.mosl.saathi.api.model.saathiIndirect.dashboard

import com.google.gson.annotations.SerializedName

data class IndirectDashboardFilterPostRequest(
    @JvmField @SerializedName("EmployeeCode")
val employeeCode: String? = null,

@JvmField @SerializedName("PageSize")
val pageSize: Int? = null,

@JvmField @SerializedName("CalculatedPage")
val calculatedPage: Int? = null,

@JvmField @SerializedName("Page")
val page: Int? = null,

@JvmField @SerializedName("frequency")
val frequency: String? = null,

@JvmField @SerializedName("TotalCount")
val totalCount: Int? = null,

@JvmField @SerializedName("PageSizeP")
val pageSizeP: Int? = null,

@JvmField @SerializedName("Filter")
val filter: Any? = null,

@JvmField @SerializedName("CalculatedPageP")
val calculatedPageP: Int? = null,

@JvmField @SerializedName("PageP")
val pageP: Int? = null,

@JvmField @SerializedName("TotalCountP")
val totalCountP: Int? = null,

@JvmField @SerializedName("category")
val category: Any? = null,

@JvmField @SerializedName("flt_userstatus")
val fltUserStatus: Any? = null,

    @JvmField @SerializedName("date")
    val date: String? = null,
    @JvmField @SerializedName("flt_Hierarchy")
    val fitHierarchy:String?=null,
)
