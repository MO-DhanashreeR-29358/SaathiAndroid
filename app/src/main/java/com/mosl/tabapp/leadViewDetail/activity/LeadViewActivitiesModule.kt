package com.mosl.tabapp.leadViewDetail.activity

import dagger.Binds
import dagger.Module

@Module
abstract class LeadViewActivitiesModule {

  @Binds
  abstract fun bindLeadViewActivitiesView(fragment: LeadViewActivitiesFragment): LeadViewActivitiesView
}