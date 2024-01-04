package com.mosl.app.core.data.fcm

import android.content.Context
import com.google.gson.Gson
import com.mosl.annotations.FcmTokenRequest
import com.mosl.app.core.prefs.StringPreference
import com.mosl.app.core.utils.getUuid
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class FcmTokenSyncHelper @Inject constructor(
  val context: Context,
  val userManager: UserManager,
  @FcmTokenRequest private val fcmTokenRequest: StringPreference,
  val gson: Gson,
  val saathiApi: SaathiApi
) {

  fun saveToken(token: String): Observable<String> {
    return Observable.fromCallable {
      val uuid = context.getUuid()
      com.mosl.saathi.api.model.fcm_token.FcmTokenRequest(
          token,
          uuid

      )
    }
        .flatMap { it ->
            gson.toJson(it)
                ?.let { request ->
                    if (request == fcmTokenRequest.get()) {
                        Observable.just("NoNeed")
                    } else {

                        saathiApi.updateFCMToken(it)
                            .filter { t -> t.isSuccess() }
                            .map { t -> t.message }
                            .doOnNext { fcmTokenRequest.set(request) }

                    }
                }!!
        }
        .subscribeOn(Schedulers.io())
  }

}