package com.mosl.mobile.user.login

import android.util.Log
import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.*
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepositoryImpl @Inject constructor(
  internal val userRepository: AppUserRepository,
  internal val userManager: UserManagerImpl
) : LoginRepository {
  override fun versionCheck(version: VersionInfo): Observable<VersionResponse> {
    return userRepository.versionCheck(version)

  }

  override fun login(loginInfo: LoginInfo): Observable<User> {
    return userRepository.login(loginInfo)
        .map { response -> saveLoginResponse(response) }
  }

  fun saveLoginResponse(response: LoginResponse): User {
    userManager.saveUser(response.user)

    return response.user
  }

  override fun fetchReportTypes(): Observable<List<ReportType>> {
    return userRepository.fetchReportTypes()
  }

  override fun fetchDigitalSalesKitParentMenu(): Observable<List<DigitalSalesKitData>> {
    return userRepository.fetchDigitalSalesKitParentMenu()
  }

  override fun fetchDigitalSalesKitChildMenu(): Observable<List<DigitalSalesKitData>> {
    return userRepository.fetchDigitalSalesKitChildMenu()
  }

  override fun logout(): Completable {
    return Completable.fromAction {
      if (userManager.isLoggedIn()) {
        val user = userManager.user
        val logoutInfo = LogoutInfo(
          userId = user.userId, token = user.token,
          businessUnitId = user.currentBusinessProfile.businessUnitId ?: "0"
        )
        val loggedOut = userRepository.logout(logoutInfo)
            .onErrorReturnItem(false)
            .blockingFirst()
        Timber.d("Logout Success : %s", loggedOut)
        userManager.logout()
      }
    }
        .subscribeOn(Schedulers.io())
  }

  override fun fnValidateLogin(objValidateLoingPostModel: LoginInfo): Observable<ValidateLoingResponseModel> {
    Log.d("validate login","called")
    return  userRepository.fnValidateLogin(objValidateLoingPostModel)
  }
  override fun fnCallOtp(objGenerateOtpPostModel: LoginInfo): Observable<String> {
  return  userRepository.fnCallOtp(objGenerateOtpPostModel)
  }

  override fun fnCallValidateOtp(objValidateOtpPostModel: ValidateOtpPostModel): Observable<User> {
  return  userRepository.fnCallValidateOtp(objValidateOtpPostModel)
    .map { response -> saveLoginResponse(response) }
  }

}