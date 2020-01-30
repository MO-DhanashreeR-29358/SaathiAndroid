package com.mosl.tabapp

import com.mosl.app.core.data.util.CertPinProvider
import okhttp3.CertificatePinner

object CertPinProviderImpl : CertPinProvider {
    override fun getPinner(): CertificatePinner? {
        return null
    }
}