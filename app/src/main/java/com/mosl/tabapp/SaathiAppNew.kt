package com.mosl.tabapp

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import androidx.room.Room
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mosl.app.core.data.interceptor.HeaderInterceptor
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
import java.io.File
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

  private val STORAGE_PERMISSION_CODE = 1001

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

    //deleteOldLogs(this, maxAgeDays = 7) // delete logs older than 7 days

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

/*  fun deleteOldLogs(context: Context, maxAgeDays: Int = 7) {
    val logDir = File(context.getExternalFilesDir(null), "logs")
    if (!logDir.exists()) return

    val now = System.currentTimeMillis()
    val maxAgeMillis = maxAgeDays * 24 * 60 * 60 * 1000L

    logDir.listFiles()?.forEach { file ->
      val lastModified = file.lastModified()
      if ((now - lastModified) > maxAgeMillis) {
        val deleted = file.delete()
        Log.d("LogCleanup", "Deleted ${file.name}: $deleted")
      }
    }
  }*/


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

  fun checkAndRequestStoragePermission() {
    if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
      != PackageManager.PERMISSION_GRANTED) {

      ActivityCompat.requestPermissions(
        applicationContext as Activity,
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
        STORAGE_PERMISSION_CODE
      )
    } else {
      // Permission already granted
      return
    }
  }
}