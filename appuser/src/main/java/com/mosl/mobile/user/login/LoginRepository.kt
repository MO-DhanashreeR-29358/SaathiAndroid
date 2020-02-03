package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.*
import io.reactivex.Completable
import io.reactivex.Observable

interface LoginRepository {
  fun login(loginInfo: LoginInfo): Observable<User>

  fun versionCheck(version: VersionInfo): Observable<VersionResponse>

  fun fetchReportTypes(): Observable<List<ReportType>>

  fun logout(): Completable
}