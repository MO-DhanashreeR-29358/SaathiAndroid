package com.mosl.tabapp

import android.R.attr
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.room.Room
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mosl.app.core.utils.matchesService
import com.mosl.tabapp.database.AppDatabase
import com.mosl.tabapp.database.Data.MIGRATION_1_2
import com.mosl.tabapp.ws.CRMWebServiceHandler
import dagger.android.DaggerApplication
import io.reactivex.rxjava3.annotations.NonNull
import processors.coreProcessors.eventReceivers.AbstractEventObserver.SESSION_EXPIRED
import processors.coreProcessors.eventReceivers.EventBroadcast
import processors.coreProcessors.eventReceivers.IEventObserver
import sdk.DistaSDK
import sdk.interfaces.OnTaskFailureListener
import sdk.interfaces.OnTaskSuccessListener
import utils.BaseApplication
import utils.DistaSDKInitializer
import utils.interfaces.IRepositoryUtils
import javax.inject.Inject


abstract class SaathiAppNew : DaggerApplication(), IEventObserver<Boolean>, Configuration.Provider  {
  init {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
  }

  lateinit var appComponent: AppComponent
  private val DATABASE_NAME = "location_db"
  private var appDatabase: AppDatabase? = null
  @Inject
  lateinit var applicationObserver: ApplicationObserver

  companion object {
    lateinit var INSTANCE: SaathiAppNew

    @JvmStatic
    fun get(): SaathiAppNew {
      return INSTANCE
    }
  }

  @NonNull
  override fun getWorkManagerConfiguration(): Configuration {
    return Configuration.Builder()
      .setMinimumLoggingLevel(Log.INFO)
      .build()
  }
  override fun onCreate() {
    super.onCreate()
      EventBroadcast.registerSessionExpiredObserver(this, this)
    init()
    CRMWebServiceHandler.initSaathiApi(this)

       ProcessLifecycleOwner.get()
        .lifecycle
       .addObserver(applicationObserver)


    appDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, DATABASE_NAME
    ).addMigrations(MIGRATION_1_2)
        .build()
    INSTANCE = this
    DistaSDKInitializer.Companion.newInstance(this)

    DistaSDK.getInstance().initOrganization( "MOSL",
      object : OnTaskSuccessListener {
        override fun onSuccess() {
         // Toast.makeText(
          //  applicationContext,
           // "initialize organization successfully !",
           // Toast.LENGTH_LONG
          //).show()
        }
      },
      object : OnTaskFailureListener {
        override fun onFailure(message: String?) {
          //Toast.makeText(
          //  applicationContext,
           // message,
           // Toast.LENGTH_LONG
          //).show()
        }
      })

    FirebaseApp.initializeApp(this)

    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)


  }

  fun getDB(): AppDatabase? {
    return this.appDatabase
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  abstract fun init()

  override fun getSystemService(name: String): Any? {
    return if (matchesService(name, AppComponent::class.java)) {
      appComponent
    } else super.getSystemService(name)
  }

    override fun onEventReceived(eventName: String?, data: Boolean?) {
      when (eventName) {

        SESSION_EXPIRED -> {
          IRepositoryUtils.pushAppStateToFirestore(INSTANCE, SESSION_EXPIRED)
          BaseApplication.getBaseApplication().setAssetDetails(false,null)
        }
      }
    }
}