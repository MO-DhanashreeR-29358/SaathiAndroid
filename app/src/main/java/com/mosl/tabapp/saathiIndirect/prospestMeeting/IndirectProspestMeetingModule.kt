package com.mosl.tabapp.saathiIndirect.prospestMeeting

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectProspestMeetingModule {
    @Binds
    abstract fun  providIndirectProspestMeetingModule(fragment: IndirectProspestmeetingFragment):IndirectProspestMeetingView
}