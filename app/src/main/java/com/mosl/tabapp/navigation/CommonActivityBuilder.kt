package com.mosl.tabapp.navigation

import com.mosl.annotations.ActivityScoped
import com.mosl.mobile.user.model.UserModule
import com.mosl.tabapp.login.LoginActivityModule
import com.mosl.tabapp.login.LoginActivityNew
import com.mosl.tabapp.slider.SliderActivity
import com.mosl.tabapp.slider.SliderActivityModule
import com.mosl.tabapp.splash.SplashActivityModule
import com.mosl.tabapp.splash.SplashActivityNew
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [UserModule::class])
abstract class CommonActivityBuilder {
  @ActivityScoped
  @ContributesAndroidInjector(modules = [SplashActivityModule::class])
  internal abstract fun bindSplashActivity(): SplashActivityNew

  @ActivityScoped
  @ContributesAndroidInjector(modules = [LoginActivityModule::class])
  internal abstract fun bindLoginActivity(): LoginActivityNew

  @ActivityScoped
  @ContributesAndroidInjector(modules = [SliderActivityModule::class])
  internal abstract fun bindSliderActivity(): SliderActivity

}