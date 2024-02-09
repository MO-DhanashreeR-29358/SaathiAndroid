package com.mosl.user.domain

import com.mosl.saathi.api.model.attendance.TokenRequest
import com.mosl.user.data.login.VerifyAction
import com.mosl.user.domain.model.GenerateOTP
import com.mosl.user.domain.model.ProfileInfo
import com.mosl.user.domain.model.TokenInfo
import com.mosl.user.domain.model.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface LoginRepository {
  fun verifyOtp(action: VerifyAction): Observable<User>

  fun userAuthentication(mobileNo: String): Observable<GenerateOTP>

  fun logoutUser(reason: Int): Completable

  fun getProfileInfo(): Observable<ProfileInfo>

  fun refreshToken(tokenRequest: TokenRequest): Observable<TokenInfo>
}