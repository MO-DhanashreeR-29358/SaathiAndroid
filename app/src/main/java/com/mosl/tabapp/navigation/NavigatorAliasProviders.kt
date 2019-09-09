package com.mosl.tabapp.navigation

import com.mosl.navigator.BaseNavigator
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorAliasProviders {
  @Binds
  abstract fun provideScreenNavigator(screenNavigator: ScreenNavigatorImpl): BaseNavigator
}