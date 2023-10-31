package com.mosl.mobile.domain.clients

data class APClientsInput(
    @JvmField val fltBankName: String?,
    @JvmField val fltPeriod: String?,
    @JvmField val fltHierarchy: String
)

data class BusinessSnapAdditionalParametersInput(
  @JvmField val fltHierarchy: String?,
  @JvmField val fltPeriod: String?,
  @JvmField val fltProspect: String?,
  @JvmField val fltSelfBranch: String?
)