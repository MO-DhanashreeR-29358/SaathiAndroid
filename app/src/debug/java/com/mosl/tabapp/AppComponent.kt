package com.mosl.mobile.saathi

import com.mosl.tabapp.SaathiAppImpl
import com.mosl.tabapp.SaathiAppNew
import com.mosl.tabapp.service.DataSyncWorker
import com.mosl.tabapp.service.ServiceModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, DebugModule::class, DebugActivityBuilder::class, ServiceModule::class]
)
interface AppComponent : AndroidInjector<SaathiAppImpl> {

  override fun inject(instance: SaathiAppImpl)

//  fun inject(target: DrawerMenuView)

//  fun inject(target: ActionBar)

  fun inject(target: DataSyncWorker)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: SaathiAppNew): Builder

    fun build(): AppComponent
  }
}