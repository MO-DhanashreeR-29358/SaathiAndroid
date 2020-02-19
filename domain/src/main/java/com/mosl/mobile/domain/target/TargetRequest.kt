package com.mosl.mobile.domain.target


data class TargetRequest(
    @JvmField val businessProfileId: String?,
    @JvmField val addtionalParameters: AditionalParametersTarget?,
    @JvmField val widgetSystemName: String?,
    @JvmField val productId: String?,
    @JvmField val employeeCode: String?
)
