package com.mosl.tabapp

import com.mosl.app.core.data.util.CertPinProvider
import okhttp3.CertificatePinner

object CertPinProviderImpl : CertPinProvider {
    override fun getPinner(): CertificatePinner? {
        return CertificatePinner.Builder()
            .add(
                "maint.motilaloswal.com",
                "sha256/H+g1BLPmXnBqUJZfALPjVWEwC8eZ8YbUE2x77ELDzM8="
            )
            .add(
                "*.motilaloswal.com",
                "sha256/zUIraRNo+4JoAYA7ROeWjARtIoN4rIEbCpfCRQT6N6A=",
                "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=",
                "sha256/4jQdZdahTbw/WEh4cZEG/LcXh7LGlblu4dhZ1tPlaXc="
            )
            .build()
    }
}