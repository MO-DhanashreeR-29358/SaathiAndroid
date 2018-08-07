package com.mosl.mobile.ekyc.database;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import com.loopj.android.http.HttpGet;
import com.mosl.mobile.ekyc.model.CollateralModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by pramod on 25-11-2016.
 */

public class CollateralDBService extends Service {
    private Handler mHandler;
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    String TAG = "CollateralDBService";
    private DBHelper db;

    public CollateralDBService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (db == null) {
            db = new DBHelper(this);
        }
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public CollateralDBService getService() {
            return CollateralDBService.this;
        }
    }

    //////////////////////////////
    public CollateralModel getCollateralById(int id) {
        return db.getCollateralById(id);
    }

    public List<CollateralModel> getCollateralBySection(String section) {
        return db.getCollateralBySection(section);
    }
    public List<CollateralModel> getAllCollateral() {
        return db.getAllCollateral();
    }
    public CollateralModel addCollateral(CollateralModel model) {
        try {

            if (getCollateralById(model.getId()) == null) {
                File file = new File(getFilesDir(), model.getFileName());
                model.setLocalFilePath(file.getAbsolutePath());
                model = db.insertCollateral(model);
                Log.d(TAG, "[" + model.getId() + "] New collateral added into database");
                //new FileDownloadTask().execute(model);xxxx
            } else {
                model = db.updateServerVersion(model.getId(),model.getServerVersion()); //update the server version
                File file = new File(getFilesDir(), model.getFileName());
                model.setLocalFilePath(file.getAbsolutePath());
                Log.d(TAG, "[" + model.getId() + "]Collateral doc already exist in database");
            }
            return model;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d(TAG, ex.toString());
        }
        return null;
    }

    /**
     *
     * @param collateralId
     * @param downloadedStatus Y or N
     */
    public void markDownloaded(int collateralId, String downloadedStatus){
        db.updateDownloadStatus(collateralId,downloadedStatus);
    }
}
