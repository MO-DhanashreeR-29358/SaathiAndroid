package com.mosl.mobile.user.login

import com.mosl.mobile.user.LogoutInfo
import com.mosl.mobile.user.model.*
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import com.mosl.saathi.api.model.splash.DigitalSalesKitMenu
import io.reactivex.rxjava3.core.Observable

/**
 * Created by lalkishore on 13/01/18.
 */

interface AppUserRepository {
  fun login(request: LoginInfo): Observable<LoginResponse>
  fun versionCheck(versionInfo: VersionInfo): Observable<VersionResponse>
  fun fetchReportTypes(): Observable<List<ReportType>>
  fun fetchDigitalSalesKitParentMenu(): Observable<List<DigitalSalesKitData>>
  fun fetchDigitalSalesKitChildMenu(): Observable<List<DigitalSalesKitData>>
  fun logout(logoutInfo: LogoutInfo): Observable<Boolean>
  fun getProfileInfo(): Observable<ProfileInfo>
}
