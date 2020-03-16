package com.mosl.tabapp.notification

import com.mosl.app.core.data.leadviewdetail.activities.LeadViewDetailActivitiesRepositoryImpl
import com.mosl.app.core.data.notification.NotificationRepositoryImpl
import com.mosl.mobile.domain.leadviewdetail.activities.LeadViewDetailActivitiesRepository
import com.mosl.mobile.domain.notification.NotificationRepository
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NotificationModule {

  @Binds
  abstract fun bindLeadViewActivitiesView(fragment: NotificationFragmentKtt): NotificationView

  @Binds
  abstract fun bindLeadViewDetailActivitiesRepository(repository: NotificationRepositoryImpl): NotificationRepository

}