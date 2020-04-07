package com.mosl.tabapp

import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class SaathiAppImpl : SaathiAppNew() {
  override fun init() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      //This process is dedicated to LeakCanary for heap analysis.
      //You should not init your app in this process.
      return
    }
    LeakCanary.install(this)
    Stetho.initializeWithDefaults(this)

    Timber.plant(object : Timber.DebugTree() {
      override fun createStackElementTag(element: StackTraceElement): String? {
        return element.lineNumber.toString() + ":" + super.createStackElementTag(element)
      }
    })
    RxJavaPlugins.setErrorHandler { throwable ->
      Timber.tag("RxError")
          .e(throwable)
    }

    //Disable crashlytics while keeping answers enabled on debug builds.
//    val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
//    Fabric.with(this, Crashlytics.Builder().core(core).build())
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
    val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()
    this.appComponent = appComponent
    return appComponent
  }
}