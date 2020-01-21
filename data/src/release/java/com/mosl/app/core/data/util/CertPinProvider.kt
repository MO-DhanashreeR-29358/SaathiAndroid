package com.mosl.app.core.data.util

import okhttp3.CertificatePinner

interface CertPinProvider {
    fun getPinner(): CertificatePinner?
}