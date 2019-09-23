package com.mosl.mobile.saathi

import com.mosl.tabapp.ApplicationModule
import com.mosl.tabapp.SaathiAppImpl
import com.mosl.tabapp.SaathiAppNew
import com.mosl.tabapp.bankmeeting.adapter.BankEmployeeAdapter
import com.mosl.tabapp.drawermenu.DrawerMenuView
import com.mosl.tabapp.navigation.ActivityBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton
import com.mosl.tabapp.service.DataSyncWorker
import com.mosl.tabapp.service.ServiceModule
import com.mosl.tabapp.ws.HeaderInterceptor

@Singleton
@Component(
    modules = [AndroidInjectionModule::class, ApplicationModule::class, ActivityBuilder::class, ServiceModule::class]
)
interface AppComponent : AndroidInjector<SaathiAppImpl> {
  override fun inject(instance: SaathiAppImpl)
  fun inject(target: DataSyncWorker)

  fun inject(target: DrawerMenuView)

//  fun inject(target: ActionBar)

  fun inject(target: HeaderInterceptor)

  fun inject(target: BankEmployeeAdapter)
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: SaathiAppNew): Builder

    fun build(): AppComponent

  }
}