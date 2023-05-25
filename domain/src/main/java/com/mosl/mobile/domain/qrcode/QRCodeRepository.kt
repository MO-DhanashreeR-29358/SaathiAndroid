package com.mosl.mobile.domain.qrcode

import io.reactivex.rxjava3.core.Observable

interface QRCodeRepository {
    fun getQRCode(empCode: String): Observable<String>
}