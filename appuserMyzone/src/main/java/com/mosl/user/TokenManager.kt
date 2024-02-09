package com.mosl.user

import android.util.Log
import androidx.annotation.WorkerThread
import com.mosl.user.domain.model.TokenInfo
import com.mosl.user.helper.*
import com.mosl.user.login.UserManager
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
  private val prefStore: TokenPrefStore,
  private val loginPrefStore: LoginPrefStore,
  val userManager: UserManager,
  private val clock: Clock
) {
  private val tokenRenewalSubject: PublishSubject<AccessToken> =
    PublishSubject.create<AccessToken>()
  private val tokenEventsSubject: PublishSubject<TokenEvents> = PublishSubject.create()
  private val tokenProviderSubject: PublishSubject<String> = PublishSubject.create<String>()

  private val TOKEN_SYNC_BUFFER_TIME = TimeUnit.MINUTES.toMillis(1)
  private val disposable: CompositeDisposable = CompositeDisposable()
  private var logger: Logger = DEFAULT
  private val LoggerConsumer: Consumer<Any> = Consumer {
    logger.log("Event - $it")
  }

  init {
    if (wasDeviceRebooted(prefStore.tokenCreationTime.get())) {
      loginPrefStore.loginTime.set(0)
      prefStore.tokenCreationTime.set(0)
    }
  }

  /*fun getTokenRequest(): TokenRequest {
    return TokenRequest(getAccessToken(), getRefreshToken())
  }

  fun getSilentLoginRequest(): RefreshTokenInfo {
    return RefreshTokenInfo(
        userManager.getUser()!!.pan, getRefreshToken(),
        getAccessToken()
    )
  }

  }*/

  fun saveTokens(
    tokenInfo: TokenInfo
  ) {
    saveAccessToken(tokenInfo)
    prefStore.refreshToken.set(tokenInfo.refreshToken)
//    prefStore.reLoginDuration.set(tokenInfo.reLoginAfter * 1000L)
  }

  fun saveAccessToken(tokenInfo: TokenInfo) {
    prefStore.accessToken.set(tokenInfo.accessToken)
//    prefStore.tokenValidity.set(tokenInfo.expiresIn * 1000L)
//    prefStore.tokenCreationTime.set(tokenInfo.loginTime)
    notifySubscribers(tokenInfo.accessToken)
    tokenRenewalSubject.onNext(AccessToken.OnRefresh)
  }

  fun tokenSaved() {
    Log.d("TokenManager:","on token saved")
    tokenEventsSubject.onNext(AccessTokenSaved)
  }

  private fun notifySubscribers(token: String) {
    tokenProviderSubject.onNext(token)
  }

  fun getAccessToken(): String {
    return prefStore.accessToken.get()
  }

  @WorkerThread
  fun getAuthorizationToken(): Single<String> {
    return Single.just(getAccessToken())
  }

  fun onTokenEvent(): Observable<TokenEvents> {
    return tokenEventsSubject.hide()
  }

  fun getRefreshToken(): String {
    return prefStore.refreshToken.get()
  }

  val tokens: Pair<String, String>
    get() {
      return Pair(getAccessToken(), getRefreshToken())
    }

  fun hasTokens(): Boolean {
    return prefStore.accessToken.isSet && prefStore.refreshToken.isSet
  }

  fun logoutNeeded() {
    tokenEventsSubject.onNext(TokenEvents.Logout)
  }

  fun refreshTokenExpired() {
    tokenEventsSubject.onNext(GetRefreshToken)
  }

  fun reInitiate() {
    tokenEventsSubject.onNext(TokenEvents.ReInitiate)
  }

  fun expired() {
    tokenRenewalSubject.onNext(AccessToken.OnUnauthorized)
  }

  fun start() {
    stop()
    logger.log("Starting TokenManager")
    val tokenRenewalObs = tokenRenewalSubject.hide().share()

    val onExpiredObs = tokenRenewalObs
        .filter { it === AccessToken.OnExpire }

    val onUnauthorizedObs = tokenRenewalObs.filter { it === AccessToken.OnUnauthorized }

    disposable.add(
      Observable.merge(onExpiredObs, onUnauthorizedObs)
            .doOnNext(LoggerConsumer)
            .map { it }
            .debounce(2000L, MILLISECONDS)
            .doOnNext { Log.d("TokenManager","Call /token Event") }
//            .distinctUntilChanged()
            .subscribe({ tokenEventsSubject.onNext(GetAccessToken) },
                { Log.d("TokenManager","TokenManagerSequence Failed $it") })
    )
  }

  private fun isSilentLoginNeeded(): Boolean {
    return getSilentLoginValidity() <= 0
  }

  private fun getSilentLoginValidity(): Long {
    val creationTime = loginPrefStore.loginTime.get()
    if (creationTime == 0L) return 0L
    val reLoginAt = creationTime + prefStore.reLoginDuration.get() - TOKEN_SYNC_BUFFER_TIME
    val currentTime = clock.getTimestamp()
    return if (currentTime > reLoginAt) {
      0
    } else reLoginAt - currentTime
  }

  fun isTokenExpired(): Boolean {
    return getRemainingValidity() <= 0
  }

  private fun getRemainingValidity(): Long {
    val creationTime = prefStore.tokenCreationTime.get()
    if (creationTime == 0L) return 0
    val expiresAt = creationTime + prefStore.tokenValidity.get() - TOKEN_SYNC_BUFFER_TIME
    val currentTime = clock.getTimestamp()
    return if (currentTime > expiresAt) {
      0
    } else expiresAt - currentTime
  }

  fun stop() {
    logger.log("Stopping TokenManager")
    disposable.clear()
  }

  fun clear() {
    stop()
    prefStore.clear()
    loginPrefStore.clear()
  }
}