package com.mosl.tabapp.leadViewDetail.updatemeeting

import com.mosl.app.core.data.leadviewdetail.updatemeeting.UpdateMeetingRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.UpdateMeetingRepository
import com.mosl.tabapp.leadViewDetail.updatemeeting.UpdateMeetingView.Companion.ARG_UPDATE_MEETING
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class UpdateMeetingModule {

  @Binds abstract fun bindUpdateMeetingView(fragment: UpdateMeetingFragment): UpdateMeetingView
  @Binds
  abstract fun bindUpdateMeetingRepository(repository: UpdateMeetingRepositoryImpl): UpdateMeetingRepository

  @Module
  companion object {

    @Provides @JvmStatic
    fun provideUpdateMeetingScreenData(fragment: UpdateMeetingFragment): UpdateMeetingScreenData {
      return fragment.arguments?.getParcelable(ARG_UPDATE_MEETING)!!
    }

  }
}