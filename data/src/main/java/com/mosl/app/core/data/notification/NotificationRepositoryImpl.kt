package com.mosl.app.core.data.notification

import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : NotificationRepository {

  override fun getNotificationData(notificationRequestInput: NotificationRequestInput): Observable<List<NotificationResponseData>> {

    val request = notificationRequestInput.requestTransform()

    return saathiApi.getNotifications(request).map {
      it.responseTransform()
    }
  }
}