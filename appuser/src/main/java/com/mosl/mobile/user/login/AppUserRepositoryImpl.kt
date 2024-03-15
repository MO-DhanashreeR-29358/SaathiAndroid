package com.mosl.mobile.user.login

import android.content.Context
import com.mosl.app.core.exception.ServiceFailureException
import com.mosl.app.core.utils.getLocalIpAddress
import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.*
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.login.LoginRequest
import com.mosl.saathi.api.model.login.ValidateLoginRequest
import com.mosl.saathi.api.model.logout.LogoutRequest
import com.mosl.saathi.api.model.otp.GenerateOtpRequest
import com.mosl.saathi.api.model.otp.ValidateOtpRequest
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.saathi.api.model.splash.VersionRequest
import com.mosl.shared.Encryptor.AESEncryptor
import com.mosl.shared.util.getDeviceID
import io.reactivex.rxjava3.core.Observable
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
            macAddress = getDeviceID(context.contentResolver), appVersion = request.appVersion
        )
    )
        .map { response: com.mosl.saathi.api.model.login.LoginResponse -> response.transform(request.password) }
  }

  override fun versionCheck(versionInfo: VersionInfo): Observable<VersionResponse> {
    return saathiApi.appVersion(
        encryptor.encrypt(getDeviceID(context.contentResolver)),
        VersionRequest(
            versionInfo.applicationType, versionInfo.appVersion, versionInfo.requestSource,
            versionInfo.displayVersion
        )
    )
        .filter { t: List<com.mosl.saathi.api.model.splash.VersionResponse> -> !t.isEmpty() }
        .map { response: List<com.mosl.saathi.api.model.splash.VersionResponse> ->
          response.get(0)
              .transform()
        }
  }

  override fun fetchReportTypes(): Observable<List<ReportType>> {
    return saathiApi.fetchReportTypes()
        .map { response -> response.response?.transformReportData()!! }
  }

    override fun fetchDigitalSalesKitParentMenu(): Observable<List<DigitalSalesKitData>> {
        return saathiApi.GetCategoryforMobileParentMenuList()
            .map { response -> response.response?.transformDigitalSalesKitData()!! }
    }

    override fun fetchDigitalSalesKitChildMenu(): Observable<List<DigitalSalesKitData>> {
        return saathiApi.GetCategoryforMobileChildMenuList()
            .map { response -> response.response?.transformDigitalSalesKitData()!! }
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

    override fun fnValidateLogin(objValidateLoingPostModel: LoginInfo): Observable<ValidateLoingResponseModel> {
            val  request=ValidateLoginRequest(
                ipaddress=getLocalIpAddress(),
                isPersistance=false,
                 employeeCode=objValidateLoingPostModel.empCode,
                macaddress=getDeviceID(context.contentResolver),
                appId="2",
                clientinfo="Android",
                password=objValidateLoingPostModel.password,
                appVersion = objValidateLoingPostModel.appVersion
            )
        return saathiApi.fnValidateLogin(request)
            .map {
                if (it.status) {
                    it.response?.transformnewlogin(it.message)?:ValidateLoingResponseModel(strMessage = it.message)

                } else {
                    throw ServiceFailureException(it.errors?.firstOrNull()?.message ?: ""+it.getMessage())
                }

            }
    }

    override fun fnCallOtp(objGenerateOtpPostModel: LoginInfo): Observable<String> {
            val request= GenerateOtpRequest(
                ipaddress=getLocalIpAddress(),
                isPersistance=false,
                employeeCode=objGenerateOtpPostModel.empCode,
                macaddress=getDeviceID(context.contentResolver),
                appId="2",
                clientinfo="Android",
                password=objGenerateOtpPostModel.password,
                appVersion = objGenerateOtpPostModel.appVersion
            )
        return saathiApi.fnGenerateOtp(request).map {
            if (it.status) {
                it.message?:it.message

            } else {
                throw ServiceFailureException(it.errors?.firstOrNull()?.message ?: ""+it.getMessage())
            }

        }
    }

    override fun fnCallValidateOtp(objValidateOtpPostModel: ValidateOtpPostModel): Observable<LoginResponse> {
        val reques=ValidateOtpRequest(
            ipaddress=getLocalIpAddress(),
            isPersistance=false,
            employeeCode=objValidateOtpPostModel.employeeCode,
            macaddress=getDeviceID(context.contentResolver),
            appId="2",
            clientinfo="Android",
            password=objValidateOtpPostModel.password,
            oTP = objValidateOtpPostModel.oTP
        )
        return saathiApi.fnValidateOtp(reques).map {
                response: com.mosl.saathi.api.model.login.LoginResponse -> response.transform(objValidateOtpPostModel.password?:"")
        }
    }
}