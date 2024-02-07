package com.mosl.tabapp.reactivation.createrequestReactivationDetails

import dagger.Binds
import dagger.Module

@Module
abstract class ReactivationDetailsModule {
    @Binds
    abstract fun provideReactivationDetailsView (fragment:ReactivationDetailsFragment):ReactivationDetailsView
}