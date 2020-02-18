package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.data.leadviewdetail.profiling.LeadViewProfilingRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import dagger.Binds
import dagger.Module

@Module
abstract class LeadProfilingModule {

  @Binds
  abstract fun bindLeadProfilingView(fragment: LeadProfilingFragment): LeadProfilingView

  @Binds
  abstract fun bindLeadViewProfilingRepository(repository: LeadViewProfilingRepositoryImpl): LeadViewProfilingRepository
}