package com.mosl.tabapp.leadViewDetail.main

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class LeadViewDetailPagerModule {
  @Binds
  abstract fun bindLeadViewDetailPagerView(view: LeadViewDetailPagerFragment): LeadViewDetailPagerView

  companion object {
    @Provides
    fun provideLeadViewDetailArgument(fragment: LeadViewDetailPagerFragment): LeadViewDetailArgument {
      val args = fragment.arguments
      return LeadViewDetailArgument(
          leadId = args?.getString(LeadViewDetailPagerView.ARG_LEAD_ID, "0") ?: "0",
          ucId = args?.getString(LeadViewDetailPagerView.ARG_UC_ID, "0") ?: "0"
      )
    }
  }
}