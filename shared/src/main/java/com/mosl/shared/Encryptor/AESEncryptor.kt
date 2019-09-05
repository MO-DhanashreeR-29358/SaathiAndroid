package com.mosl.shared.Encryptor

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.text.Charsets.UTF_8

class AESEncryptor @Inject constructor() : Encryptor {
  private val keySpec = SecretKeySpec(ZERO_PADDING_KEY.toByteArray(UTF_8), "AES")
  private val ivSpec = IvParameterSpec(IV.toByteArray(UTF_8))

  override fun encrypt(data: String): String {
    return try {
      val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
          .apply {
            init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
          }
      val encryptedOutput = cipher.doFinal(data.toByteArray(UTF_8))
      var encrptedValue = Base64.encodeToString(encryptedOutput, Base64.DEFAULT)
      encrptedValue.replace("\n", "")
          .replace("\r", "");
    } catch (ex: Exception) {
      ""
    }
  }

  override fun decrypt(data: String): ByteArray? {
    return try {
      val resp = data.replace("\n", "")
          .replace("\r", "")
          .replace("Output", "")
          .replace(":", "")
      val byteData = Base64.decode(resp, Base64.DEFAULT)
      val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
          .apply {
            init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
          }
      cipher.doFinal(byteData)
//      String(encVal, UTF_8)
    } catch (ex: Exception) {
      null
    }
  }

  companion object {
    private const val CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val ZERO_PADDING_KEY = "8080808080808080"
    private const val IV = "8080808080808080"
  }
}