package com.mosl.mobile.user.login

import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.User
import com.mosl.mobile.user.model.VersionInfo
import com.mosl.mobile.user.model.VersionResponse
import io.reactivex.Completable
import io.reactivex.Observable

interface LoginRepository {
  fun login(loginInfo: LoginInfo): Observable<User>

  fun versionCheck(version: VersionInfo): Observable<VersionResponse>

  fun fetchReportTypes(): Observable<List<ReportType>>

  fun logout(logoutInfo: LogoutInfo): Completable
}