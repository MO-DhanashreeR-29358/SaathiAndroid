package com.mosl.user.data.encrypt

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class ConveyanceEncryptor {
    //private val keySpec = SecretKeySpec(KEY.toByteArray(UTF_8), "AES")
    //private val ivSpec = IvParameterSpec(IV.toByteArray(UTF_8))

    /*override fun encrypt(data: String): String {
      return try {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            .apply {
              init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            }
        val encryptedOutput = cipher.doFinal(data.toByteArray(UTF_8))
        Base64.encodeToString(encryptedOutput, Base64.DEFAULT)
      } catch (ex: Exception) {
        ""
      }
    }*/

    @Throws(
        UnsupportedEncodingException::class, InvalidKeyException::class,
        NoSuchAlgorithmException::class, NoSuchPaddingException::class,
        InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    public fun encrypt(plainText: String): String {
        val plainTextBytes = plainText.toByteArray(charset(ENCODING))
        //val aesEncryptionKey = "8080808080808080"
        val keyBytes = getKeyBytes(KEY)
        //val aesEncryptionIV = "8080808080808080"

        return Base64.encodeToString(
            encrypt(plainTextBytes, keyBytes, IV.toByteArray(charset(ENCODING))),
            Base64.DEFAULT
        )
            .replace("\n".toRegex(), "")
            .replace("\r".toRegex(), "")
    }

    @Throws(
        NoSuchAlgorithmException::class, NoSuchPaddingException::class, InvalidKeyException::class,
        InvalidAlgorithmParameterException::class, IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    private fun encrypt(
        plainText: ByteArray,
        key: ByteArray,
        initialVector: ByteArray
    ): ByteArray {
        var text = plainText
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)
        val ivParameterSpec = IvParameterSpec(initialVector)
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        text = cipher.doFinal(text)
        return text
    }

    @Throws(UnsupportedEncodingException::class)
    private fun getKeyBytes(key: String): ByteArray {
        val keyBytes = ByteArray(16)
        val parameterKeyBytes = key.toByteArray(charset(ENCODING))
        System.arraycopy(
            parameterKeyBytes, 0, keyBytes, 0,
            Math.min(parameterKeyBytes.size, keyBytes.size)
        )
        return keyBytes
    }

    /*internal fun decrypt(data: String): String {
      return try {
        val byteData = Base64.decode(data, Base64.DEFAULT)
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
            .apply {
              init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            }
        val encVal = cipher.doFinal(byteData)
        String(encVal, UTF_8)
      } catch (ex: Exception) {
        ""
      }
    }*/

    companion object {
        private const val ENCODING = "UTF-8"
        private const val CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"
        private const val ALGORITHM = "AES"
        private const val KEY = "Asj4npxEkIo4giEk"
        private const val IV = "yjo3fE0Sp2p8dRjK"
    }
}

fun String.aesEncrypt(): String? {
    try {
        return ConveyanceEncryptor().encrypt(this)
    } catch (e: Exception) {
        return null
    }
}
