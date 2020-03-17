package com.mosl.tabapp.notification

import dagger.Binds
import dagger.Module

@Module
abstract class NotificationModule {
    @Binds
    abstract fun bindLeadViewActivitiesView(fragment: NotificationFragmentKtt): NotificationView
}