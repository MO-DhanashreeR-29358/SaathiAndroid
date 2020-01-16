package com.mosl.mobile.user.login

import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.LoginInfo
import com.mosl.mobile.user.model.LoginResponse
import com.mosl.mobile.user.model.ReportType
import com.mosl.mobile.user.model.VersionInfo
import com.mosl.mobile.user.model.VersionResponse
import io.reactivex.Observable

/**
 * Created by lalkishore on 13/01/18.
 */

interface AppUserRepository {
  fun login(request: LoginInfo): Observable<LoginResponse>
  fun versionCheck(versionInfo: VersionInfo): Observable<VersionResponse>
  fun fetchReportTypes(): Observable<List<ReportType>>
  fun logout(logoutInfo: LogoutInfo): Observable<Boolean>
}
