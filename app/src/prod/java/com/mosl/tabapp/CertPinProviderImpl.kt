package com.mosl.tabapp

import com.mosl.app.core.data.util.CertPinProvider
import okhttp3.CertificatePinner

object CertPinProviderImpl : CertPinProvider {
    override fun getPinner(): CertificatePinner? {
        return CertificatePinner.Builder()
            .add(
                "*.motilaloswal.com",
                "sha256/SXBYISpTUynqc/luYeFJrZtM01FDF4Il9BynZGcPPGo=",
                "sha256/zUIraRNo+4JoAYA7ROeWjARtIoN4rIEbCpfCRQT6N6A=",
                "sha256/r/mIkG3eEpVdm+u/ko/cwxzOMo1bk4TyHIlByibiA5E=",
                "sha256/4jQdZdahTbw/WEh4cZEG/LcXh7LGlblu4dhZ1tPlaXc="

            )
            /*.add(
                "online.motilaloswalmf.com",
                "sha256/9j/xIZ3ru4qXRK0HC+QdVs3Sscl7Ya9k0ZI/Iu0TLBA="
            )*/

            .build()
    }
}