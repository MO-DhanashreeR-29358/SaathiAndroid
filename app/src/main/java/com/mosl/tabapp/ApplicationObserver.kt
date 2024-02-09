package com.mosl.tabapp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.mosl.user.data.login.MyZoneLoginRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationObserver @Inject constructor(
    val loginRepository: MyZoneLoginRepositoryImpl
) : LifecycleObserver {

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onForeground() {
    appInForeground = true
    loginRepository.start()
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onBackground() {
    appInForeground = false
    loginRepository.stop()
  }

  companion object {
    var appInForeground: Boolean = false
  }
}