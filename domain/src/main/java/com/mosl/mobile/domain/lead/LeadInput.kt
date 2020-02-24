package com.mosl.mobile.domain.lead

data class LeadInput(
    @JvmField val businessProfileId: String?,
    @JvmField val addtionalParameters: AdditionalParametersInput?,
    @JvmField val widgetSystemName: String?,
    @JvmField val productId: String?,
    @JvmField val employeeCode: String?
)
