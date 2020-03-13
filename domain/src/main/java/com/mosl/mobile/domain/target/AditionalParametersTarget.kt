package com.mosl.mobile.domain.target

data class AditionalParametersTarget(
    @JvmField val fltBankName: String?,
    @JvmField val fltBankSubBrokerCode: String?,
    @JvmField val fltBankBranchCode: String?,
    @JvmField val fltHierarchy: String?,
    @JvmField val fltPeriod: String?
)