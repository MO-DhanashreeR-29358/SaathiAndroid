package com.mosl.tabapp

import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class SaathiAppImpl : SaathiAppNew() {
  override fun init() {

    Fabric.with(this, Crashlytics())

    Timber.plant(object : Timber.DebugTree() {
      override fun createStackElementTag(element: StackTraceElement): String? {
        return element.lineNumber.toString() + ":" + super.createStackElementTag(element)
      }
    })
    RxJavaPlugins.setErrorHandler { throwable ->
      Timber.tag("RxError")
          .e(throwable)
    }

  }


  override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
    val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()
    this.appComponent = appComponent
    return appComponent
  }
}