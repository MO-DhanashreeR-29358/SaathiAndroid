package com.mosl.mobile.saathi

import com.mosl.tabapp.SaathiAppImpl
import com.mosl.tabapp.SaathiAppNew
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, DebugModule::class, DebugActivityBuilder::class]
)
interface AppComponent : AndroidInjector<SaathiAppImpl> {

  override fun inject(instance: SaathiAppImpl)

//  fun inject(target: DrawerMenuView)

//  fun inject(target: ActionBar)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: SaathiAppNew): Builder

    fun build(): AppComponent
  }
}