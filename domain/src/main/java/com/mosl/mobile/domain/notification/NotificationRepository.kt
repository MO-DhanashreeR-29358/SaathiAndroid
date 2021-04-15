package com.mosl.mobile.domain.notification

import io.reactivex.rxjava3.core.Observable

interface NotificationRepository {
  fun getNotificationData(notificationRequestInput: NotificationRequestInput): Observable<List<NotificationResponseData>>
}