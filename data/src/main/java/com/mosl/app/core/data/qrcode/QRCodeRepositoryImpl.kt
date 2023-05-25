package com.mosl.app.core.data.qrcode

import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.saathi.api.SaathiApi
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class QRCodeRepositoryImpl @Inject constructor(val saathiApi: SaathiApi) : QRCodeRepository{

    override fun getQRCode(empCode: String): Observable<String> {
        val strUrl = "https://saathi.motilaloswal.com/saathi_plus/api/DrishtiTransfer/GenerateQrCode_API/?Empcode="+
                empCode
        return saathiApi.getQRCode(strUrl).map { it }
    }
}