package com.mosl.app.core.data.interceptor

import android.util.Log
import com.mosl.shared.Encryptor.AESEncryptor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.Buffer
import java.io.IOException
import javax.inject.Inject

class EncryptionInterceptor @Inject constructor(
  val encryptor: AESEncryptor
) : Interceptor {

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()
    val oldBody = request.body
    var isEncrypted = false

    if (oldBody != null) {
      val buffer = Buffer()
      oldBody.writeTo(buffer)
      val strOldBody = buffer.readUtf8()

      val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
      val strNewBody = encryptor.encrypt(strOldBody)
      Log.d("EncryptedRequest11" , strNewBody)
      val body = RequestBody.create(mediaType, strNewBody)
      request = request.newBuilder()
        .method(request.method, body)
          .build()
      isEncrypted = true
    }

    val response = chain.proceed(request)
    if (isEncrypted && response.body != null) {
      try {
        val strResponse = response.body
        val decrypted = encryptor.decrypt(strResponse!!.string())
        if (decrypted != null) {
          val decryptedBody =
            ResponseBody.create("application/json".toMediaTypeOrNull(), decrypted)
          return response.newBuilder()
              .body(decryptedBody)
              .build()
        } else {
          return response;
        }
      } catch (e: Exception) {
        return response
      }
    } else {
      return response
    }
  }
}