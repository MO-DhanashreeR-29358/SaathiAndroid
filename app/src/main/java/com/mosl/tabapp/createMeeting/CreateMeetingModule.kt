package com.mosl.tabapp.createMeeting

import com.mosl.tabapp.leadViewDetail.CreateMeetingFragmentOld
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import dagger.Module
import dagger.Provides

@Module
abstract class CreateMeetingModule {

  @Module
  companion object {
    @Provides
    @JvmStatic
    fun providePermissionHelper(fragment: CreateMeetingFragmentOld): PermissionCheckHelperFragment {
      return PermissionCheckHelperFragment(fragment)
    }
  }

}