package com.mosl.user.data.login

import android.util.Log
import com.mosl.saathi.api.MyZoneApi
import com.mosl.saathi.api.model.attendance.GenerateOtpRequest
import com.mosl.saathi.api.model.attendance.TokenRequest
import com.mosl.saathi.api.model.attendance.ValidateOtpRequest
import com.mosl.user.R
import com.mosl.user.TokenManager
import com.mosl.user.domain.LoginRepository
import com.mosl.user.domain.model.GenerateOTP
import com.mosl.user.domain.model.ProfileInfo
import com.mosl.user.domain.model.TokenInfo
import com.mosl.user.domain.model.User
import com.mosl.user.helper.GetAccessToken
import com.mosl.user.helper.TokenEvents
import com.mosl.user.login.UserEvent
import com.mosl.user.login.MyZoneUserManagerImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject

class MyZoneLoginRepositoryImpl @Inject constructor(
    private val hrmsApi: MyZoneApi,
    private val userManager: MyZoneUserManagerImpl,
    private val tokenManager: TokenManager
) : LoginRepository {
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun verifyOtp(
        action: VerifyAction
    ): Observable<User> {
        return hrmsApi.validateOTP(
            ValidateOtpRequest(
                requestId = action.requestId, oTP = action.otp.toInt()
            )
        )
            .map {
                tokenManager.saveTokens(
                    TokenInfo(
                        accessToken = it.token,
                        refreshToken = it.refreshToken,
                        expiresIn = it.expiresOn
                    )
                )
                userManager.saveUser(DataMapper.transformData(it, action.mobileNumber))
                userManager.getUser()!!
            }
    }

    override fun userAuthentication(mobileNo: String): Observable<GenerateOTP> {
        return hrmsApi.generateOTP(GenerateOtpRequest(mobileNo))
            .map {
                GenerateOTP(
                    it.requestId, it.oTP ?: ""
                )
            }
    }

    override fun logoutUser(reason: Int): Completable {
        return Completable.fromAction {
            if (userManager.isLoggedIn()) {
                val loggedOut = hrmsApi.logout()
                    .onErrorReturnItem("unSuccessful")
                    .blockingFirst()
                Log.d("Logout Success : %s", loggedOut)
                tokenManager.clear()
                userManager.logout(reason)
            }
        }
            .subscribeOn(Schedulers.io())
    }

    override fun getProfileInfo(): Observable<ProfileInfo> {
        return hrmsApi.getProfile()
            .map {
                ProfileInfo(
                    inTime = Date()/*DateParser.parseToInTime(it.inTime)*/,
                    attendanceCount = it.attendanceCount,
                    webviewUrl = it.redirectUrl,
                    attendanceType = it.attendanceType,
                    joiningDate = it.joiningDate,
                    isInitialDeclaration = it.isInitialDeclaration
                )
            }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun callLogout(): Observable<String> {
        return logoutUser(R.string.logout_msg).toSingleDefault("Successful")
            .toObservable()
    }

    override fun refreshToken(tokenRequest: TokenRequest): Observable<TokenInfo> {
        val tokens = tokenManager.tokens
        return hrmsApi.getRefreshToken(tokenRequest)
            .map { t ->
                TokenInfo(
                    accessToken = t.token, refreshToken = t.refreshToken, expiresIn = t.expiresOn
                )
            }
    }

    fun start() {
        disposable.add(
            userManager.onUserChanged().subscribe {
                when (it) {
                    is UserEvent.LoggedIn -> {
                        tokenManager.start()
                    }
                    is UserEvent.LoggedOut -> {

                    }
                    UserEvent.OpenUser -> {
                    }
                }
            }
        )

        val tokenEventObs = tokenManager.onTokenEvent()
            .share()

        disposable.add(tokenEventObs.filter { it === TokenEvents.Logout }
            .switchMap { callLogout() }
            .subscribe())

        disposable.add(tokenEventObs.filter { it == GetAccessToken }
            .map { tokenManager.tokens }
            .switchMap { tokens ->
                hrmsApi.getRefreshToken(
                    TokenRequest(
                        token = tokens.first,
                        refreshToken = tokens.second
                    )
                )
                    .compose(UiModelHelper.applyUiModel())
            }.doOnError { tokenManager.logoutNeeded() }.subscribe { result ->
                result.data?.let {
                    Log.d("save access token %s", it.toString())
                    tokenManager.saveAccessToken(DataMapper.transformTokenDate(it))
                    tokenManager.tokenSaved()
                }
                result.error?.let {
                    tokenManager.logoutNeeded()
                }
            })

        if (userManager.isLoggedIn()) {
            tokenManager.start()
        }
    }

    fun stop() {
        disposable.clear()
        tokenManager.stop()
    }
}