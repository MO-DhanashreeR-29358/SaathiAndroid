package com.mosl.mobile.domain.autarget


data class AuTargetInput(
    @JvmField val businessProfileId: String?,
    @JvmField val addtionalParameters: APTargetInput?,
    @JvmField val widgetSystemName: String?,
    @JvmField val productId: String?,
    @JvmField val employeeCode: String?
)
