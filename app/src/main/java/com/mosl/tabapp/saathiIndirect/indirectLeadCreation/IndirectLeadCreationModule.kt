package com.mosl.tabapp.saathiIndirect.indirectLeadCreation

import dagger.Binds
import dagger.Module

@Module
abstract class IndirectLeadCreationModule {
  @Binds abstract fun provideLeadCreationView(view: IndirectLeadCreationFragment): IndirectLeadCreationView
}