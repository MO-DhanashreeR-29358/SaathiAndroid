package com.mosl.app.core.data.notification

import com.mosl.app.core.data.util.safeMap
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.saathi.api.model.notification.NotificationRequest
import com.mosl.saathi.api.model.notification.NotificationResponse

fun NotificationRequestInput.requestTransform(): NotificationRequest {
    return NotificationRequest(category=category)
}

fun List<NotificationResponse>.responseTransform(): List<NotificationResponseData> {
    return safeMap {
        NotificationResponseData(
            message = it.message,
            creationDate = it.creationDate,
            additionalData = it.additionalData
        )
    }
}


