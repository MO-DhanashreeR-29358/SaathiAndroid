package com.mosl.mobile.user.login

import com.mosl.mobile.user.model.*
import com.mosl.saathi.api.model.splash.DigitalSalesKitData
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LoginRepository {
  fun login(loginInfo: LoginInfo): Observable<User>

  fun versionCheck(version: VersionInfo): Observable<VersionResponse>

  fun fetchReportTypes(): Observable<List<ReportType>>

  fun fetchDigitalSalesKitParentMenu(): Observable<List<DigitalSalesKitData>>

  fun fetchDigitalSalesKitChildMenu(): Observable<List<DigitalSalesKitData>>

  fun logout(): Completable

  fun fnValidateLogin(objValidateLoingPostModel: ValidateLoginPostModel):Observable<ValidateLoingResponseModel>

  fun fnCallOtp(objGenerateOtpPostModel: GenerateOtpPostModel):Observable<String>

  fun fnCallValidateOtp(objValidateOtpPostModel: ValidateOtpPostModel):Observable<User>
}