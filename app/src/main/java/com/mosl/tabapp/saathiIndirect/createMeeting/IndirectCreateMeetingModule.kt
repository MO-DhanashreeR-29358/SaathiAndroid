package com.mosl.tabapp.saathiIndirect.createMeeting

import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingFragment
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class IndirectCreateMeetingModule {
    @Binds
    abstract fun  providIndirectCreateMeetingModule(fragment: IndirectCreateMeetingFragment):IndirectCreateMeetingView

}