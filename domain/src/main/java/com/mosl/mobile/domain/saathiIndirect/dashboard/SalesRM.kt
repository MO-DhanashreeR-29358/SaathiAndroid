package com.mosl.mobile.domain.saathiIndirect.dashboard

data class SalesRM(
    val SalesRM: String,
    val ReportingTo: String?,
    val Revenue: String?,
    var subordinates: List<SalesRM> = mutableListOf(),
    var isExpanded: Boolean = false
)

data class ApiResponse(
    val Response: Response,
    val Type: String,
    val Message: String,
    val Status: Boolean,
    val Erros: List<Any>
)

data class Response(
    val Data: List<SalesRM>
)