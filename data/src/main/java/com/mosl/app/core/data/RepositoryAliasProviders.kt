package com.mosl.app.core.data


import com.mosl.app.core.data.ausfbLead.AusfbLeadRepositoryImpl
import com.mosl.app.core.data.ausfbLead.BankRepositoryImpl
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.mobile.domain.ausfbLead.BankRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryAliasProviders {

    @Binds
    abstract fun provideBankRepository(repository: BankRepositoryImpl): BankRepository

    @Binds
    abstract fun provideAusfbLeadRepository(repository: AusfbLeadRepositoryImpl): AusfbLeadRepository

}