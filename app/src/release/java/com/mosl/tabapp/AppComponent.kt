package com.mosl.mobile.saathi

import com.mosl.tabapp.ActivityBuilder
import com.mosl.tabapp.ApplicationModule
import com.mosl.tabapp.SaathiAppImpl
import com.mosl.tabapp.SaathiAppNew
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton
import com.mosl.tabapp.service.DataSyncWorker

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, ApplicationModule::class, ActivityBuilder::class, ServiceModule::class]
)
interface AppComponent : AndroidInjector<SaathiAppImpl> {
  override fun inject(instance: SaathiAppImpl)
  fun inject(target: DataSyncWorker)

  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: SaathiAppNew): Builder

    fun build(): AppComponent
  }
}