package com.mosl.tabapp.leadViewDetail.updatemeeting

import dagger.Binds
import dagger.Module

@Module
abstract class UpdateMeetingModule {

  @Binds abstract fun bindUpdateMeetingView(fragment: UpdateMeetingFragment): UpdateMeetingView
}