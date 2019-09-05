package com.mosl.mobile.user.login

import android.content.Context
import android.util.Log
import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.LoginResponse
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.VersionInfo
import com.mosl.mobile.user.model.VersionResponse
import com.mosl.mobile.user.model.transform
import com.mosl.mobile.user.model.transformReportData
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.login.LoginRequest
import com.mosl.saathi.api.model.login.LoginResponseData
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.splash.VersionRequest
import com.mosl.saathi.api.model.splash.VersionResponseData
import com.mosl.shared.Encryptor.AESEncryptor
import com.mosl.shared.util.getDeviceID
import com.mosl.shared.util.getLocalIpAddress
import io.reactivex.Observable
import javax.inject.Inject

class AppUserRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi,
  val encryptor: AESEncryptor,
  val context: Context
) : AppUserRepository {

  override fun login(request: LoginInfo): Observable<LoginResponse> {
    return saathiApi.login(
        request.empCode,
        LoginRequest(
            request.empCode, request.password, ipAddress = getLocalIpAddress(),
            macAddress = getDeviceID(context.contentResolver)
        )
    )
        .map { response: LoginResponseData -> response.transform() }
  }

  override fun versionCheck(versionInfo: VersionInfo): Observable<VersionResponse> {
    return saathiApi.appVersion(
        encryptor.encrypt(getDeviceID(context.contentResolver)),
        VersionRequest(
            versionInfo.applicationType, versionInfo.appVersion, versionInfo.requestSource,
            versionInfo.displayVersion
        )
    )
        .filter { t: List<VersionResponseData> -> !t.isEmpty() }
        .map { response: List<VersionResponseData> ->
          response.get(0)
              .transform()
        }
        .doOnError { t ->
          Log.d("on Error ", t.message)
        }
  }

  override fun fetchReportTypes(): Observable<List<ReportType>> {
    return saathiApi.fetchReportTypes()
        .map { response -> response.response?.transformReportData() }
  }

  override fun logout(logoutInfo: LogoutInfo): Observable<Boolean> {
    val request = LogoutRequest(
        userId = logoutInfo.userId, businessUnitId = logoutInfo.businessUnitId,
        token = logoutInfo.token,
        ipAddress = getLocalIpAddress(), macAddress = getDeviceID(context.contentResolver)
    )
    return saathiApi.logout(request)
        .map { true }
  }
}