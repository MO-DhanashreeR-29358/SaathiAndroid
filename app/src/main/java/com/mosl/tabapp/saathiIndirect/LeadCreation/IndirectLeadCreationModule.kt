package com.mosl.tabapp.saathiIndirect.LeadCreation

import dagger.Binds
import dagger.Module

@Module
abstract class IndirectLeadCreationModule {
  @Binds abstract fun provideIndirectLeadCreationView(view: IndirectLeadCreationFragment): IndirectLeadCreationView
}