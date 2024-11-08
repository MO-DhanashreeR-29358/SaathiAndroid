package com.mosl.tabapp.saathiIndirect.setAppointment

import dagger.Binds
import dagger.Module

@Module
public abstract class IndirectSetAppointmentModule {

    @Binds abstract fun  provideSetAppointmentView( fragment :IndirectSetAppointmentFragment):IndirectSetAppointmentView
}