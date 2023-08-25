package com.mosl.tabapp.reactivation

import com.mosl.app.core.data.qrcode.QRCodeRepositoryImpl
import com.mosl.app.core.data.query.QueryRepositoryImpl
import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.mobile.domain.query.QueryRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ReactivationSummaryModule {
    @Binds
    abstract fun provideRCTSummaryContractView(fragment: ReactivationSummaryFragment?): ReactivationSummaryView?

}