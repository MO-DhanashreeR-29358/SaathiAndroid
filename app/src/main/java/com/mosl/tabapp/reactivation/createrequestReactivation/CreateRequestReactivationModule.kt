package com.mosl.tabapp.reactivation.createrequestReactivation

import dagger.Binds
import dagger.Module

@Module
abstract class CreateRequestReactivationModule {
    @Binds
    abstract fun provideCreateRequestReactivationView(fragment:CreateRequestReactivationFragment):CreateRequestReactivationView
}