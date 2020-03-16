package com.mosl.mobile.domain.notification

data class NotificationResponseData(

    @JvmField val message: String?,
    @JvmField val creationDate: String?,
    @JvmField val additionalData: String?
)