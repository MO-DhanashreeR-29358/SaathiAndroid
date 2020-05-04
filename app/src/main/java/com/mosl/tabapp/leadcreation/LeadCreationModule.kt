package com.mosl.tabapp.leadcreation

import dagger.Binds
import dagger.Module

@Module
abstract class LeadCreationModule {
  @Binds abstract fun provideLeadCreationView(view: LeadCreationFragment): LeadCreationView
}