package com.mosl.tabapp.createMeeting

import com.mosl.tabapp.Permisssion.PermissionCheckHelperFragment
import com.mosl.tabapp.leadViewDetail.CreateMeetingFragment
import dagger.Module
import dagger.Provides

@Module
abstract class CreateMeetingModule {

  @Module
  companion object {
    @Provides @JvmStatic
    fun providePermissionHelper(fragment: CreateMeetingFragment): PermissionCheckHelperFragment {
      return PermissionCheckHelperFragment(fragment)
    }
  }

}