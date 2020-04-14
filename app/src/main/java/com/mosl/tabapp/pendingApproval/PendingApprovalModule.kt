package com.mosl.tabapp.pendingApproval

import dagger.Binds
import dagger.Module

@Module
abstract class PendingApprovalModule {
  @Binds
  abstract fun bindLeadViewActivitiesView(fragment: PendingApprovalFragment): PendingApprovalView
}