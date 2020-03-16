package com.mosl.app.core.data.notification

import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : NotificationRepository {

  override fun getNotificationData(request: NotificationRequestInput): Observable<List<NotificationResponseData>> {

    val rewques = request.responseClientDetailItemTransform()

    return saathiApi.getNotifications(rewques).map {
      it.responseNotificationDataTransform()
    }
  }
}