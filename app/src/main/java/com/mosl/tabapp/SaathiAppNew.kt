package com.mosl.tabapp

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.room.Room
import com.mosl.app.core.utils.matchesService
import com.mosl.tabapp.database.AppDatabase
import com.mosl.tabapp.database.Data.MIGRATION_1_2
import com.mosl.tabapp.ws.CRMWebServiceHandler
import dagger.android.DaggerApplication

abstract class SaathiAppNew : DaggerApplication() {
  init {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
  }

  lateinit var appComponent: AppComponent
  private val DATABASE_NAME = "location_db"
  private var appDatabase: AppDatabase? = null

  companion object {
    lateinit var INSTANCE: SaathiAppNew

    @JvmStatic
    fun get(): SaathiAppNew {
      return INSTANCE
    }
  }

  override fun onCreate() {
    super.onCreate()
    init()
    CRMWebServiceHandler.initSaathiApi(this)

    appDatabase = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, DATABASE_NAME
    ).addMigrations(MIGRATION_1_2)
        .build()
    INSTANCE = this
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
}