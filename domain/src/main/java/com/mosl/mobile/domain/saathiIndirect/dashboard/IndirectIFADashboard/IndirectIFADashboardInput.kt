package com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard

data class IndirectIFADashboardInput(
    val employeeCode: String? = null,

    val pageSize: Int? = null,

    val calculatedPage: Int? = null,

    val page: Int? = null,

    val frequency: String? = null,

    val totalCount: Int? = null,

    val pageSizeP: Int? = null,

    val filter: Any? = null,

    val calculatedPageP: Int? = null,

    val pageP: Int? = null,

    val totalCountP: Int? = null,

    val category: Any? = null,

    val fltUserStatus: Any? = null,
    val date:String?=null,
    val fitHierarchy:String?=null

)
