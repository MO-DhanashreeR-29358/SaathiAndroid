package com.mosl.app.core.data.qrcode

import com.mosl.mobile.data.util.CommonBaseUrls
import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QRCodeRepositoryImpl @Inject constructor(private val apiUrl: CommonBaseUrls,
                                               val saathiApi: SaathiApi) : QRCodeRepository{

    override fun getQRCode(empCode: String): Observable<String> {
        val strUrl = apiUrl.getQRCodeApiUrl() + empCode
        return saathiApi.getQRCode(strUrl).map { it }
    }
}