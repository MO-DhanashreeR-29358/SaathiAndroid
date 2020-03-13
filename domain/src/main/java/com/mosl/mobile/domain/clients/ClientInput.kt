package com.mosl.mobile.domain.clients


data class ClientInput(
    @JvmField val businessProfileId: String?,
    @JvmField val addtionalParameters: APClientsInput,
    @JvmField val widgetSystemName: String?,
    @JvmField val productId: String?,
    @JvmField val employeeCode: String?
)
