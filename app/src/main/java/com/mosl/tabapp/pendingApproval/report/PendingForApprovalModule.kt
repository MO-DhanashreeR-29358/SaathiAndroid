package com.mosl.tabapp.pendingApproval.report

import dagger.Binds
import dagger.Module

@Module
abstract class PendingForApprovalModule {
  @Binds
  abstract fun bindLeadViewActivitiesView(fragment: PendingForApprovalFragment): PendingForApprovalView
}