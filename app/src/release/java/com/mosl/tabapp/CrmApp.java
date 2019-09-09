package com.mosl.tabapp;


import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import android.util.Log;
import androidx.room.Room;
import com.crashlytics.android.Crashlytics;
import com.mosl.tabapp.database.AppDatabase;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class CrmApp extends MultiDexApplication {
    private static final String DATABASE_NAME = "location_db";
    public static CrmApp INSTANCE;
    private static Context context;
    private AppDatabase appDatabase;

    public static CrmApp get() {
        return INSTANCE;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Fabric.with(this, new Crashlytics());
//        Stetho.initializeWithDefaults(this);
        initImageLoader(context);
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

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .build();
        INSTANCE = this;
    }

    public AppDatabase getDB() {
        return appDatabase;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }


        }
    }
}

