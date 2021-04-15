package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LoginRepository {
  fun login(loginInfo: LoginInfo): Observable<User>

  fun versionCheck(version: VersionInfo): Observable<VersionResponse>

  fun fetchReportTypes(): Observable<List<ReportType>>

  fun logout(): Completable
}