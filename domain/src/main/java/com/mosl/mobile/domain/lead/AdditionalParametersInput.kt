package com.mosl.mobile.domain.lead

data class AdditionalParametersInput(
    @JvmField val fltBankName: String?,
    @JvmField val fltBankSubBrokerCode: String?,
    @JvmField val fltBankBranchCode: String?,
    @JvmField val fltHierarchy: String?,
    @JvmField val fltPeriod: String?,
    @JvmField val fltProduct: String?,
    @JvmField val fltProspect: String?
)