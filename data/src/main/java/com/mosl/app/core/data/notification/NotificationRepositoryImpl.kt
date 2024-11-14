package com.mosl.app.core.data.notification

import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.mobile.domain.notification.NotificationRequestInput
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.SaathiIndirectApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi,
  val saathiIndirectApi: SaathiIndirectApi,
  val userManager: UserManager
) : NotificationRepository {

  override fun getNotificationData(notificationRequestInput: NotificationRequestInput): Observable<List<NotificationResponseData>> {

    val request = notificationRequestInput.requestTransform()

    if(userManager.getUser()?.isIndirectUser() == true){
      return saathiIndirectApi.getNotifications(request).map {
        it.responseTransform()
      }
    }else{
      return saathiApi.getNotifications(request).map {
        it.responseTransform()
      }
    }

  }
}