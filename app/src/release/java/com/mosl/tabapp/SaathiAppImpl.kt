package com.mosl.tabapp

import android.content.Context
import com.mosl.mobile.saathi.DaggerAppComponent
import com.mosl.tabapp.SaathiAppNew
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class SaathiAppImpl : SaathiAppNew() {
  override fun init() {
    /*Timber.plant(object : Timber.DebugTree() {
      override fun createStackElementTag(element: StackTraceElement): String? {
        return element.lineNumber.toString() + ":" + super.createStackElementTag(element)
      }
    })*/

    val crashlyticsKit = Crashlytics.Builder()
        .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
        .build()
    Fabric.with(this, crashlyticsKit)

    RxJavaPlugins.setErrorHandler { throwable ->
      Timber.tag("RxError")
          .e(throwable)
    }

//    Fabric.with(this, Crashlytics())
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
//    MultiDex.install(this)
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
    val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()
    this.appComponent = appComponent
    return appComponent
  }
}