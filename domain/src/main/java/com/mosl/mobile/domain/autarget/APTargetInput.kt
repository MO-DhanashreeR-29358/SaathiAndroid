package com.mosl.mobile.domain.autarget

data class APTargetInput(
    @JvmField val fltBankName: String?,
    @JvmField val fltBankSubBrokerCode: String?,
    @JvmField val fltBankBranchCode: String?,
    @JvmField val fltHierarchy: String?,
    @JvmField val fltPeriod: String?
)