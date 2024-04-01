package com.mosl.user

import com.mosl.core.annotations.ConveyanceAuthToken
import com.mosl.core.annotations.EncryptedTokenForAuthToken
import com.mosl.user.login.UserManager
import com.mosl.user.prefs.StringPreference
import dagger.Lazy
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizationInterceptor @Inject constructor(
    private val userManager: UserManager,
    private val tokenManager: Lazy<TokenManager>
) : Interceptor {
    private val tokenProvider: TokenManager by lazy { tokenManager.get() }

    @field:EncryptedTokenForAuthToken
    @Inject
    lateinit var EncryptedTokenForAuthToken: StringPreference

    @field:ConveyanceAuthToken
    @Inject
    lateinit var ConveyanceAuthToken: StringPreference

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {

        var original = chain.request()
        var newRequest: Request? = null
        val requestBody = original.body()
        val requestBuilder = original.newBuilder()

        if (original.header(NO_TOKEN) != null) {
            requestBuilder.removeHeader(NO_TOKEN)
        } else if (userManager.getUser() != null) {
            if (original.url().toString().endsWith("auth/token")) {
                requestBuilder.header("Content-Type", "application/json")
                    .header("Authorization", EncryptedTokenForAuthToken.get())

            } else if (original.url().toString().endsWith("track-locations/") ||
                original.url().toString().endsWith("redirect-url")
            ) {
                requestBuilder.header("Content-Type", "application/json")
                    .header("Authorization", ConveyanceAuthToken.get())

            } else {
                requestBuilder.header("Content-Type", "application/json")
                    .header("Authorization", tokenProvider.getAuthorizationToken().blockingGet())
            }
//          .header("empId", "CA-0217-5616")
//          .header("empId", "RA-2419-5496")
            original = if (requestBody != null) {
                requestBuilder
                    .post(requestBody)
                    .build()
            } else {
                requestBuilder.get()
                    .build()
            }
        }

        val response = chain.proceed(original)

        val code = response.header("StatusCode")
        if ((!code.isNullOrEmpty() && code == "401") || response.code() == 401) {
            tokenProvider.expired()
        }

        return response

    }

    companion object {
        const val NO_TOKEN = "No-Token"
    }
}