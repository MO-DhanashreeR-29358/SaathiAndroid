package com.mosl.app.core.data


import com.mosl.app.core.data.autarget.TargetRepositoryImpl
import com.mosl.app.core.data.bank.BankRepositoryImpl
import com.mosl.app.core.data.lead.LeadRepositoryImpl
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.lead.LeadRepository
import com.mosl.mobile.domain.target.TargetRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryAliasProviders {

    @Binds
    fun provideBankRepository(repository: BankRepositoryImpl): BankRepository

    @Binds
    fun provideAusfbLeadRepository(repository: LeadRepositoryImpl): LeadRepository

    @Binds
    fun provideAuTargetVsAchRepository(repository: TargetRepositoryImpl): TargetRepository
}