package com.mosl.tabapp.leadViewDetail.leaddetail

import dagger.Binds
import dagger.Module

@Module
abstract class LeadDetailModule {
  @Binds
  abstract fun bindLeadDetailView(fragment: LeadDetailFragment): LeadDetailView
}