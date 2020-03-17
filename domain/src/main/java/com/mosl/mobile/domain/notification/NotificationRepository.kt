package com.mosl.mobile.domain.notification

import io.reactivex.Observable

interface NotificationRepository {
  fun getNotificationData(notificationRequestInput: NotificationRequestInput): Observable<List<NotificationResponseData>>
}