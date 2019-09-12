package com.mosl.app.core.data.interceptor

import android.content.Context
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.ApiEndpoints.AppVersionEndPoint
import com.mosl.saathi.api.ApiEndpoints.LoginEndPoint
import com.mosl.shared.Encryptor.AESEncryptor
import com.mosl.shared.util.getDeviceID
import com.mosl.shared.util.getLocalIpAddress
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by lalkishore on 8/19/2016.
 */
class HeaderInterceptor @Inject constructor(
  val userManager: UserManager,
  val context: Context,
  val encryptor: AESEncryptor

) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val original = chain.request()

    val builder = original.newBuilder()

    if (chain.request().url().pathSegments().last() != LoginEndPoint && chain.request().url().pathSegments().last() != AppVersionEndPoint) {
      val user = userManager.user
      builder.header(API_HEADER_CONTENT_TYPE, encryptor.encrypt(API_HEADER_CONTENT_TYPE_VALUE))
      builder.header(API_HEADER_CLIENT_INFO, encryptor.encrypt(API_HEADER_CLIENT_INFO_VALUE))
      builder.header(API_HEADER_IP_ADDRESS, encryptor.encrypt(getLocalIpAddress()))
      builder.header(
          API_HEADER_MAC_ADDRESS, encryptor.encrypt(getDeviceID(context.contentResolver))
      )
      builder.header(API_HEADER_APP_ID, encryptor.encrypt(API_HEADER_APP_ID_VALUE))
      builder.header(API_HEADER_TOKEN, encryptor.encrypt(user.token))
      builder.header(API_HEADER_USER_ID, encryptor.encrypt(user.userId))
      builder.header(
          API_HEADER_BUSINESS_UNIT_ID, encryptor.encrypt(
          user.currentBusinessProfile.businessUnitId ?: "0"
      )
      )
      builder.header(API_HEADER_EMPLOYEE_CODE, encryptor.encrypt(userManager.user.employeeCode))

    }
    val response = chain.proceed(builder.build())

    return response
  }

  companion object {
    private val API_HEADER_CONTENT_TYPE = "Content-Type"
    private val API_HEADER_TOKEN = "token"
    private val API_HEADER_USER_ID = "userid"
    private val API_HEADER_CLIENT_INFO = "clientinfo"
    private val API_HEADER_IP_ADDRESS = "ipaddress"
    private val API_HEADER_BUSINESS_UNIT_ID = "BusinessUnitId"
    private val API_HEADER_MAC_ADDRESS = "macaddress"
    private val API_HEADER_EMPLOYEE_CODE = "EmployeeCode"
    private val API_HEADER_APP_ID = "AppId"
    private val API_HEADER_CONTENT_TYPE_VALUE = "application/json; charset=UTF-8"
    private val API_HEADER_CLIENT_INFO_VALUE = "android"
    private val API_HEADER_APP_ID_VALUE = "2"

  }
}
