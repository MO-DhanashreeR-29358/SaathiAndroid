package com.mosl.mobile.domain.aulead

data class AusfbLeadInput(
    @JvmField val businessProfileId: String?,
    @JvmField val addtionalParameters: AdditionalParametersInput?,
    @JvmField val widgetSystemName: String?,
    @JvmField val productId: String?,
    @JvmField val employeeCode: String?
)
