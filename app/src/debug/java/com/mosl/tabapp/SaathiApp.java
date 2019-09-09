package com.mosl.tabapp;

import android.content.Context;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDex;
import androidx.room.Room;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.mosl.tabapp.database.AppDatabase;
import com.mosl.tabapp.ws.AppProvider;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.squareup.leakcanary.LeakCanary;
import dagger.android.DaggerApplication;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

abstract public class SaathiApp extends DaggerApplication {
    private static final String DATABASE_NAME = "location_db";
    private AppDatabase appDatabase;
    public static SaathiApp INSTANCE;

    public static SaathiApp get() {
        return INSTANCE;
    }

    public void onCreate() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            //This process is dedicated to LeakCanary for heap analysis.
            //You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        super.onCreate();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
            .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build();
        Fabric.with(this, crashlyticsKit);
        Stetho.initializeWithDefaults(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected String createStackElementTag(StackTraceElement element) {
                    return element.getLineNumber() + ":" + super.createStackElementTag(element);
                }
            });
        } else {
            Timber.plant(new CrashReportingTree());
        }

        CRMWebServiceHandler.initSaathiApi(this);
        AppProvider.initSaathiApi(this);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
            AppDatabase.class, DATABASE_NAME)
            .build();
        INSTANCE = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        init();
    }

    public abstract void init();

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }
        }
    }

    public AppDatabase getDB() {
        return appDatabase;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
