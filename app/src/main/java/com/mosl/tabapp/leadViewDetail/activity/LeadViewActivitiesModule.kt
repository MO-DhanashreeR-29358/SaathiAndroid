package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.data.leadviewdetail.activities.LeadViewDetailActivitiesRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.activities.LeadViewDetailActivitiesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class LeadViewActivitiesModule {

  @Binds
  abstract fun bindLeadViewActivitiesView(fragment: LeadViewActivitiesFragment): LeadViewActivitiesView

  @Binds
  abstract fun bindLeadViewDetailActivitiesRepository(repository: LeadViewDetailActivitiesRepositoryImpl): LeadViewDetailActivitiesRepository
}