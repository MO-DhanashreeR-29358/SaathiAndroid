package com.mosl.tabapp.navigation

import com.mosl.navigator.BaseNavigator
import com.mosl.tabapp.menu.ScreenNavigatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NavigatorAliasProviders {
  @Binds
  abstract fun provideScreenNavigator(screenNavigator: ScreenNavigatorImpl): BaseNavigator
}