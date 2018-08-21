package com.mosl.tabapp.service;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mosl.tabapp.CrmApp;
import com.mosl.tabapp.R;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.database.LocationData;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import static com.mosl.tabapp.common.CRMAppUtil.getPrefs;

public class ForegroundService extends Service {

    private static final String CHANNEL_ID = "channel_01";
    private static final String TAG = ForegroundService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    Handler refreshHandler = new Handler();
    int NOTIFICATION_ID = 12345;
    Location mCurrentLocation; // location
    private NotificationManager mNotificationManager;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 5 * 60 * 100;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            try {
                if (getPrefs(getApplicationContext()).getString("EmployeeCode", null) != null || !getPrefs(getApplicationContext()).getString("EmployeeCode", null).isEmpty()) {
                    LocationUpdate();
                }
                refreshHandler.postDelayed(runnableCode, 5 * 60 * 1000);

            } catch (Throwable ex) {
                Log.d("Exception", ex.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
        startLocationUpdates();


        refreshHandler.postDelayed(runnableCode, 10000);

        return START_STICKY;
    }

    private Notification getNotification() {


        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_icon);
        Intent notificationIntent = new Intent(this, ForegroundService.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentText("We are tracking your location")
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_icon)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID); // Channel ID
        }


        return notification.build();


    }

    private void LocationUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LocationData locationData = new LocationData();
                if (mCurrentLocation != null) {

                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(
                                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
                        locationData.setGeoArea(addressList.get(0).getAddressLine(0));
                        locationData.setGeoPincode(addressList.get(0).getPostalCode());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    locationData.setLatitude(mCurrentLocation.getLatitude() + "");
                    locationData.setLongitude(mCurrentLocation.getLongitude() + "");
                    locationData.setAccuracy(mCurrentLocation.getAccuracy() + "");
                    locationData.setEmpCode(getPrefs(getApplicationContext()).getString("EmployeeCode", null));
                    locationData.setTime(CRMAppUtil.getCurrentDisplayDate());
                    locationData.setDeviceId(getPrefs(getApplicationContext()).getString("macaddress", null));

                    CrmApp.get().getDB().daoAccess().insertLocationList(locationData);
                    Log.d("Location Data entered", locationData.toString());
                }
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
    }

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        stopForeground(true);

        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        stopForeground(true);

        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.

        // TODO(developer). If targeting O, use the following code.
           /* if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        ForegroundService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }*/
        startForeground(NOTIFICATION_ID, getNotification());


        return true; // Ensures onRebind() is called when a client re-binds.
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        mCurrentLocation = locationResult.getLastLocation();

                    }
                },
                Looper.myLooper());
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }
}
