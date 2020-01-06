package com.mosl.app.core.data


import com.mosl.app.core.data.aulead.AusfbLeadRepositoryImpl
import com.mosl.app.core.data.autarget.AuTargetRepositoryImpl
import com.mosl.app.core.data.bank.BankRepositoryImpl
import com.mosl.mobile.domain.aulead.AusfbLeadRepository
import com.mosl.mobile.domain.autarget.AuTargetRepository
import com.mosl.mobile.domain.bank.BankRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryAliasProviders {

    @Binds
    fun provideBankRepository(repository: BankRepositoryImpl): BankRepository

    @Binds
    fun provideAusfbLeadRepository(repository: AusfbLeadRepositoryImpl): AusfbLeadRepository

    @Binds
    fun provideAuTargetVsAchRepository(repository: AuTargetRepositoryImpl): AuTargetRepository
}