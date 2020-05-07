package com.mosl.tabapp.navigation

import com.mosl.navigator.BaseNavigator
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.leadcreation.ui.LeadCreationNavigator
import com.mosl.tabapp.leadcreation.ui.LeadCreationNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorAliasProviders {
  @Binds
  abstract fun provideScreenNavigator(screenNavigator: ScreenNavigatorImpl): BaseNavigator

  @Binds
  abstract fun provideEkycNavigator(screenNavigator: ScreenNavigatorImpl): EkycNavigator

  @Binds
  abstract fun provideLeadCreationNavigator(navigator: LeadCreationNavigatorImpl): LeadCreationNavigator
}