package com.mosl.tabapp;

import android.content.Context;
import android.util.Log;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import androidx.room.Room;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.mosl.tabapp.database.AppDatabase;
import com.mosl.tabapp.ws.AppProvider;
import com.mosl.tabapp.ws.CRMWebServiceHandler;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.leakcanary.LeakCanary;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class CrmApp extends MultiDexApplication {
    private static Context context;
    private static final String DATABASE_NAME = "location_db";
    private AppDatabase appDatabase;
    public static CrmApp INSTANCE;

    public static CrmApp get() {
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
        context = getApplicationContext();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
            .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build();
        Fabric.with(this, crashlyticsKit);
        Stetho.initializeWithDefaults(this);
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

        CRMWebServiceHandler.initSaathiApi(this);
        AppProvider.initSaathiApi(this);

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME)
                .build();
        INSTANCE = this;
    }

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
}
