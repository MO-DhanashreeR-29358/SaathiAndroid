package com.mosl.tabapp.leadViewDetail.main

import dagger.Binds
import dagger.Module

@Module
abstract class LeadViewDetailPagerModule {
  @Binds
  abstract fun bindLeadViewDetailPagerView(view: LeadViewDetailPagerFragment): LeadViewDetailPagerView
}