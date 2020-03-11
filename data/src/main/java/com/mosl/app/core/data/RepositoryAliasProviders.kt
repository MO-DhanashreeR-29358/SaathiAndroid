package com.mosl.app.core.data

import com.mosl.app.core.data.autarget.TargetRepositoryImpl
import com.mosl.app.core.data.bank.BankRepositoryImpl
import com.mosl.app.core.data.clients.ClientsRepositoryImpl
import com.mosl.app.core.data.lead.LeadRepositoryImpl
import com.mosl.app.core.data.snapshot.BusinessSnapshotRepositoryImpl
import com.mosl.app.core.data.tradeyesterday.ClientTradeYesterdayRepositoryImpl
import com.mosl.app.core.data.trends.TrendsRepositoryImpl
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.clients.ClientsRepository
import com.mosl.mobile.domain.lead.LeadRepository
import com.mosl.mobile.domain.snapshot.BusinessSnapshotRepository
import com.mosl.mobile.domain.target.TargetRepository
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayRepository
import com.mosl.mobile.domain.trends.TrendsRepository
import dagger.Binds
import dagger.Module

@Module
interface RepositoryAliasProviders {

  @Binds
  fun provideBankRepository(repository: BankRepositoryImpl): BankRepository

  @Binds
  fun provideAusfbLeadRepository(repository: LeadRepositoryImpl): LeadRepository

  @Binds
  fun provideClientsRepositoryRepository(repository: ClientsRepositoryImpl): ClientsRepository

  @Binds
  fun provideBusinessSnapshotRepository(repository: BusinessSnapshotRepositoryImpl): BusinessSnapshotRepository

  @Binds
  fun provideAuTargetVsAchRepository(repository: TargetRepositoryImpl): TargetRepository

  @Binds
  fun provideClientTradeYesterdayRepository(repository: ClientTradeYesterdayRepositoryImpl): ClientTradeYesterdayRepository

  @Binds
  fun provideTrendsRepositoryRepository(repository: TrendsRepositoryImpl): TrendsRepository
}