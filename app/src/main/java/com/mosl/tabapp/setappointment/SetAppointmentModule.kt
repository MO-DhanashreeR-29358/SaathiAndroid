package com.mosl.tabapp.setappointment

import com.mosl.app.core.data.setappointment.SetAppointmentRepositoryImpl
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SetAppointmentModule {

  @Binds
  abstract fun bindSetAppointmentView(fragment: SetAppointmentFragment): SetAppointmentView

  @Binds
  abstract fun bindSetAppointmentRepository(repository: SetAppointmentRepositoryImpl): SetAppointmentRepository

}