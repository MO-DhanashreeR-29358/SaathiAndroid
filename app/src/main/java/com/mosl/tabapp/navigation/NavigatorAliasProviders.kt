package com.mosl.tabapp.navigation

import com.mosl.navigator.BaseNavigator
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailNavigator
import com.mosl.tabapp.leadViewDetail.main.LeadViewDetailNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorAliasProviders {
  @Binds
  abstract fun provideScreenNavigator(screenNavigator: ScreenNavigatorImpl): BaseNavigator

  @Binds
  abstract fun provideEkycNavigator(screenNavigator: ScreenNavigatorImpl): EkycNavigator

  @Binds
  abstract fun provideLeadViewDetailNavigator(
    navigator: LeadViewDetailNavigatorImpl
  ): LeadViewDetailNavigator

}