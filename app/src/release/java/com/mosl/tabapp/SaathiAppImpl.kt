package com.mosl.tabapp

import com.crashlytics.android.Crashlytics
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber

class SaathiAppImpl : SaathiAppNew() {
  override fun init() {

    Fabric.with(this, Crashlytics())

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