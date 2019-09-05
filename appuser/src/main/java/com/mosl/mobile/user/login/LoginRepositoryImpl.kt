package com.mosl.mobile.user.login

import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.LoginResponse
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.VersionInfo
import com.mosl.mobile.user.model.VersionResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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

  override fun logout(logoutInfo: LogoutInfo): Completable {
    return Completable.fromAction {
      if (userManager.isLoggedIn()) {
        val loggedOut = userRepository.logout(logoutInfo)
            .onErrorReturnItem(false)
            .blockingFirst()
        Timber.d("Logout Success : %s", loggedOut)
        userManager.logout()
      }
    }
        .subscribeOn(Schedulers.io())
  }
}