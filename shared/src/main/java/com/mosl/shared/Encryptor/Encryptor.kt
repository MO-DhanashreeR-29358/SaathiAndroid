package com.mosl.shared.Encryptor

interface Encryptor {
  fun encrypt(data: String): String
  fun decrypt(data: String): ByteArray?
}