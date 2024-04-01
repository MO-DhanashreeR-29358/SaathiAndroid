package com.mosl.tabapp

import com.mosl.annotations.ActivityScoped
import com.mosl.mobile.user.model.UserModule
import com.mosl.tabapp.branch.BranchActivity
import com.mosl.tabapp.branch.BranchModule
import com.mosl.tabapp.login.LoginActivity
import com.mosl.tabapp.login.LoginActivityModule
import com.mosl.tabapp.settings.SettingActivity
import com.mosl.tabapp.settings.SettingFragmentProvider
import com.mosl.tabapp.slider.SliderActivity
import com.mosl.tabapp.slider.SliderActivityModule
import com.mosl.tabapp.splash.SplashActivity
import com.mosl.tabapp.splash.SplashActivityModule
import com.mosl.user.AppUserModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [UserModule::class, AppUserModule::class])
abstract class CommonActivityBuilder {
    @ActivityScoped
    @ContributesAndroidInjector(modules = [SplashActivityModule::class])
    internal abstract fun bindSplashActivity(): SplashActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginActivityModule::class])
    internal abstract fun bindLoginActivity(): LoginActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SliderActivityModule::class])
    internal abstract fun bindSliderActivity(): SliderActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [SettingFragmentProvider::class])
    internal abstract fun bindSettingActivity(): SettingActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [BranchModule::class])
    internal abstract fun bindBranchActivity(): BranchActivity

}