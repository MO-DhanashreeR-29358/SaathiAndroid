package com.mosl.mobile.domain.saathiIndirect.dashboard



data class FocoDashboardInput(
    val employeeCode: String? = null,

    val pageSize: Int? = null,

    val calculatedPage: Int? = null,

    val page: Int? = null,

    val frequency: Any? = null,

    val totalCount: Int? = null,

    val pageSizeP: Int? = null,

    val filter: Any? = null,

    val calculatedPageP: Int? = null,

    val pageP: Int? = null,

    val totalCountP: Int? = null,

    val category: Any? = null,

    val fltUserStatus: Any? = null,
   val fitHierarchy:String?=null
)
