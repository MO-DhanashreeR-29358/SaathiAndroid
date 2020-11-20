package com.mosl.tabapp.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mosl.tabapp.settings.SettingActivity;

import javax.inject.Inject;

import timber.log.Timber;

/*@ActivityScoped*/ public class FusedLocation {
    private static final String TAG = "FusedLocation";
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final int REQUEST_CHECK_SETTINGS = 100;
    private final AppCompatActivity activity;
    LatLongDispatcher dispatcher;
    boolean showDialog = false;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private android.location.Location mCurrentLocation;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;

    @Inject
    public FusedLocation(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void initDispatcher(LatLongDispatcher dispatcher) {
        this.dispatcher = dispatcher;
        init();
    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                if (mCurrentLocation != null) {
                    dispatcher.onLatLongChanged(
                            mCurrentLocation);
                    //showToast("" + locationResult.getLastLocation().getAccuracy());
                    //mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                }
            }
        };
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        startLocationUpdates();
    }

     public void initV2() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mSettingsClient = LocationServices.getSettingsClient(activity);
        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(activity, locationSettingsResponse -> {
                    Timber.i("All location settings are satisfied. Started location updates!");
                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    showToast("fg yhgfhy");
                })
                .addOnFailureListener(activity, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                            Looper.myLooper());
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Timber.i("FusedLocation settings are not satisfied. Attempting to upgrade "
                                    + "location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                if (CRMAppUtil.isNetworkEnabled(activity)) {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                                }
                            } catch (IntentSender.SendIntentException sie) {
                                Timber.i("PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage =
                                    "FusedLocation settings are inadequate, and cannot be "
                                            + "fixed here. Fix in Settings.";
                            Timber.e(errorMessage);
                    }
                    showToast("");
                });
    }

    public void LocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,
                Looper.myLooper());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        mRequestingLocationUpdates = false;
                        //startLocationUpdates();
                        break;
                }
                break;
        }
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Timber.e("FusedLocation updates stopped!");
                    }
                });
    }

    private void showToast(String data) {
        //if (mCurrentLocation != null) {
        //    Toast.makeText(activity, "Data : "
        //        + mCurrentLocation.getLatitude()
        //        + "---"
        //        + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
        //}
    }

    public interface LatLongDispatcher {
        void onLatLongChanged(Location location);
    }
}