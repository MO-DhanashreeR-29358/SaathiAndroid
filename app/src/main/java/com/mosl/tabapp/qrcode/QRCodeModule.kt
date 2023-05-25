package com.mosl.tabapp.qrcode

import com.mosl.app.core.data.qrcode.QRCodeRepositoryImpl
import com.mosl.app.core.data.query.QueryRepositoryImpl
import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.mobile.domain.query.QueryRepository
import dagger.Binds
import dagger.Module

@Module
abstract class QRCodeModule {
    @Binds
    abstract fun provideQRCodeContractView(fragment: QRCodeFragment?): QRCodeView?

    @Binds
    abstract fun provideQRCodeRepository(repository: QRCodeRepositoryImpl?): QRCodeRepository?
}