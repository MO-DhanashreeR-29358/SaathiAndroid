package com.mosl.mobile.domain.reactivation

data class ReactivationClientSearchPostModel(

    @JvmField val employeeCode: String,

    @JvmField val clientCode: String,

    @JvmField val requestTypeFlag: String,

    @JvmField val branchShift: String
)
