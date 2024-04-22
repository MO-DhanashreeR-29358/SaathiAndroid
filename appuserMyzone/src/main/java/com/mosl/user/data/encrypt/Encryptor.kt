package com.mosl.data.api.encrypt

interface Encryptor {
    fun encrypt(data: String): String
    fun decrypt(data: String): String
}