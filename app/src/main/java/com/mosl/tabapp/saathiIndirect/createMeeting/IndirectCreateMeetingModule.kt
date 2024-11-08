package com.mosl.tabapp.saathiIndirect.createMeeting

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectCreateMeetingModule {
    @Binds
    abstract fun  providIndirectCreateMeetingModule(fragment: IndirectCreateMeetingFragment):IndirectCreateMeetingView
}