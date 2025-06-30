package com.mosl.app.core.data.interceptor

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mosl.app.core.utils.getLocalIpAddress
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.ApiEndpoints.AppVersionEndPoint
import com.mosl.saathi.api.ApiEndpoints.GenerateOTP
import com.mosl.saathi.api.ApiEndpoints.LoginEndPoint
import com.mosl.saathi.api.ApiEndpoints.SendSurveyLink
import com.mosl.saathi.api.ApiEndpoints.ValidateLogin
import com.mosl.saathi.api.ApiEndpoints.ValidateOTP
import com.mosl.shared.Encryptor.AESEncryptor
import com.mosl.shared.util.getDeviceID
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    val startTime = System.nanoTime()

    val builder = original.newBuilder()
    val endpoint = original.url.toString().substringAfterLast('/')
    Log.d("APILog", endpoint)
    writeLogToFile(context, "$endpoint--> ${original.method} ${original.url}")
    // Check if the request is one of the excluded endpoints
    if (!isExcludedEndpoint(original)) {
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
      builder.header(
        "currentBusinessProfileId",
        encryptor.encrypt(userManager.user.currentBusinessProfile.businessProfileId ?: "0")
      )

    }

    // Proceed with the request and obtain the response
    val newRequest = builder.build()
    if(endpoint.equals("GetNotificationMessageMobile")) {
      for (name in newRequest.headers.names()) {
        val value = newRequest.header(name)
        writeLogToFile(context, "$endpoint: Header: $name = $value")
      }
    }
    val requestBody = newRequest.body
    var requestBodyString = ""

    if (requestBody != null) {
      try {
        val buffer = Buffer()
        requestBody.writeTo(buffer)

        val contentType = requestBody.contentType()
        val charset = contentType?.charset(Charsets.UTF_8) ?: Charsets.UTF_8

        requestBodyString = buffer.readString(charset)
        writeLogToFile(context, "$endpoint: Request: $requestBodyString")
      } catch (e: Exception) {
      }
    }


    val response = try {
      chain.proceed(newRequest)
    } catch (e: Exception) {
      writeLogToFile(context, "<--$endpoint: HTTP FAILED: ${e.localizedMessage}")
      throw e
    }

    val endTime = System.nanoTime()
    val durationMs = (endTime - startTime) / 1_000_000

    writeLogToFile(context, "$endpoint: ${response.code} ${response.message} (${durationMs}ms)")

    // Optional: log response body (be careful with large or sensitive data)
    //val responseBody = response.peekBody(1024 * 1024) // up to 1MB
    val responseBody = response.body
    val responseBodyString = responseBody?.string() ?: ""

    writeLogToFile(context, "<--$endpoint:Response Body: $responseBodyString")

    // Recreate the response so Retrofit can still read it
    val contentType = responseBody?.contentType()
    val newResponseBody = ResponseBody.create(contentType, responseBodyString)

    return response.newBuilder()
      .body(newResponseBody)
      .build()
  }

  private fun isExcludedEndpoint(request: Request): Boolean {
    val lastPathSegment = request.url.pathSegments.last()
    return lastPathSegment == LoginEndPoint ||
            lastPathSegment == AppVersionEndPoint ||
            lastPathSegment == ValidateLogin ||
            lastPathSegment == GenerateOTP ||
            lastPathSegment == ValidateOTP ||
            lastPathSegment == SendSurveyLink
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

  fun getDailyLogFile(context: Context): File {
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val fileName = "http_logs_$date.txt"
    val logDir = File(context.getExternalFilesDir(null), "logs")

    if (!logDir.exists()) logDir.mkdirs()

    return File(logDir, fileName)
  }

  fun writeLogToFile(context: Context, logText: String) {
   /* val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
    val logLine = "[$timestamp] $logText"

    val logFile = getDailyLogFile(context)

    try {
      FileOutputStream(logFile, true).bufferedWriter().use { writer ->
        writer.appendLine(logLine)
      }
    } catch (e: IOException) {
      Log.e("Logger", "Failed to write log: ${e.localizedMessage}")
    }*/
  }




}
