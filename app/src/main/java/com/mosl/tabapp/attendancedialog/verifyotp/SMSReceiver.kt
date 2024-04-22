package com.mosl.tabapp.attendancedialog.verifyotp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber

class SMSReceiver : BroadcastReceiver() {
  private val otpReadSubject = PublishSubject.create<Int>()

  override fun onReceive(context: Context, intent: Intent?) {
    intent ?: return
    if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
      val extras = intent.extras
      val status = extras?.get(SmsRetriever.EXTRA_STATUS) as? Status
      when (status?.statusCode) {
        CommonStatusCodes.SUCCESS -> {
          val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as? String
          Timber.d("SMSReceiver Success: %s", message)
          readMessage(message)
        }
        CommonStatusCodes.TIMEOUT -> {
          Timber.d("SMSReceiver TimeOut")
        }
      }
    }
  }

  protected fun readMessage(message: String?) {
    message ?: return
    val otpPart = message.split(".")[0]
    val length = otpPart.length
    val otp = otpPart.substring(length - 6, length)
    if (TextUtils.isDigitsOnly(otp)) {
      otpReadSubject.onNext(otp.toInt())
    }
  }

  fun read(): Observable<Int> {
    return otpReadSubject
  }
}