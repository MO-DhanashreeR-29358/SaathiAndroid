package com.mosl.tabapp.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mosl.app.core.prefs.StringPreference;
import com.mosl.mobile.user.annotation.LastLocationAPITime;
import com.mosl.mobile.user.annotation.LastLocationTime;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.saathi.api.SaathiApi;
import com.mosl.shared.Encryptor.AESEncryptor;
import com.mosl.shared.util.AppDateFormats;
import com.mosl.shared.util.Utility;
import com.mosl.tabapp.R;
import com.mosl.tabapp.SaathiAppNew;
import com.mosl.tabapp.common.CRMAppUtil;
import com.mosl.tabapp.common.MSFStatistics;
import com.mosl.tabapp.database.LocationData;
import com.mosl.tabapp.urls.ServiceUrlsImpl;
import dagger.android.AndroidInjection;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.json.JSONException;
import org.json.JSONObject;
import timber.log.Timber;

import static com.mosl.app.core.utils.Utils.getLocalIpAddress;
import static com.mosl.shared.util.CRMAppUtilsKt.getDeviceID;
import static com.mosl.shared.util.Utility.isTimeInterval;
import static com.mosl.tabapp.common.CRMAppUtil.isLocationFromMockProvider;
import static com.mosl.tabapp.common.CRMMOSLCommon.hasGPSDevice;
import static com.mosl.tabapp.constants.MOSLConstants.STATUS;

public class ForegroundService extends Service {

  private static final String CHANNEL_ID = "saathi";
  private static final String TAG = ForegroundService.class.getSimpleName();
  private final IBinder mBinder = new LocalBinder();
  Handler refreshHandler = new Handler();
  Handler refreshHandlerAPI = new Handler();
  int NOTIFICATION_ID = 12345;
  @Inject UserManager userManager;
  @Inject SaathiApi saathiApi;
  @Inject AESEncryptor encryptor;
  @LastLocationTime
  @Inject StringPreference lastLocationTime;

  @LastLocationAPITime
  @Inject StringPreference lastLocationAPITime;

  private LocationRequest mLocationRequest;
  private Runnable runnableCode = new Runnable() {
    @Override public void run() {
      if (hasGPSDevice(getApplicationContext())) {
        startLocationUpdates();
      }
      refreshHandler.postDelayed(runnableCode, 15 * 60 * 1000);
    }
  };

  private Runnable runnableCodeAPI = new Runnable() {
    @Override public void run() {
      saveLocation();
      refreshHandlerAPI.postDelayed(runnableCodeAPI, 32 * 60 * 1000);
    }
  };

  public void saveLocation() {
    AsyncTask.execute(() -> {
      if (MSFStatistics.isNetworkEnabled(getApplicationContext())
          && userManager.isLoggedIn()
          && !userManager.getUser().isAufsb()) {
        if ((lastLocationAPITime.get().isEmpty() ||
            Utility.isTimeInterval(lastLocationAPITime.get(), 27))
            && SaathiAppNew.get().getDB().daoAccess().getLocationList().size() != 0) {
          renderLocation();
        }
      }
    });
  }

  @Override public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    NotificationManager mNotificationManager =
        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

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
    refreshHandler.postDelayed(runnableCode, 1000);
    refreshHandlerAPI.postDelayed(runnableCodeAPI, 1000 * 10);

    return START_STICKY;
  }

  private Notification getNotification() {

    Intent notificationIntent = new Intent(this, ForegroundService.class);
    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
            PendingIntent.FLAG_ONE_SHOT);

    NotificationCompat.Builder notification =
        new NotificationCompat.Builder(this, CHANNEL_ID).setContentText(
            "We are tracking your location")
            .setContentTitle(getString(R.string.app_name))
            .setSmallIcon(R.drawable.ic_icon)
            .setContentIntent(pendingIntent)
            .setOngoing(true);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      notification.setChannelId(CHANNEL_ID); // Channel ID
    }

    return notification.build();
  }

  private void LocationUpdate(Location mCurrentLocation) {
    new Thread(() -> {
      LocationData locationData;
      String geoArea = "";
      String geoPincode = "000000";
      Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
      try {
        List<Address> addressList =
            geocoder.getFromLocation(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude(), 1);
        geoArea = addressList.get(0).getAddressLine(0);
        geoPincode = addressList.get(0).getPostalCode();
      } catch (IOException e) {
        e.printStackTrace();
      }
      locationData = new LocationData(mCurrentLocation.getLatitude() + "",
          mCurrentLocation.getLongitude() + "",
          getDeviceID(getApplicationContext().getContentResolver()),
          userManager.getUser().employeeCode,
          CRMAppUtil.getCurrentDisplayDate(), mCurrentLocation.getAccuracy() + "", geoArea,
          geoPincode);
      if ((lastLocationTime.get().isEmpty() || isTimeInterval(lastLocationTime.get(), 13))
          && !isLocationFromMockProvider(getApplicationContext(), mCurrentLocation)) {
        lastLocationTime.set(AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()));
        SaathiAppNew.get().getDB().daoAccess()
            .insertLocationList(locationData);
        Timber.tag("Location Data entered").d(locationData.toString());
      }
    }).start();
  }

  @Override public void onDestroy() {
    super.onDestroy();
  }

  public boolean serviceIsRunningInForeground(Context context) {
    ActivityManager manager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager
        .getRunningServices(
            Integer.MAX_VALUE)) {
      if (getClass().getName().equals(service.service.getClassName())) {
        if (service.foreground) {
          return true;
        }
      }
    }
    return false;
  }

  @Override public IBinder onBind(Intent intent) {
    Timber.tag(TAG).i("in onBind()");
    stopForeground(true);

    return mBinder;
  }

  @Override public void onRebind(Intent intent) {
    Timber.tag(TAG).i("in onRebind()");
    stopForeground(true);

    super.onRebind(intent);
  }

  @Override public boolean onUnbind(Intent intent) {
    Timber.tag(TAG).i("Last client unbound from service");
    startForeground(NOTIFICATION_ID, getNotification());
    return true;
  }

  protected void startLocationUpdates() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    /* 10 secs */
    long UPDATE_INTERVAL = 5 * 60 * 1000;
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    /* 2 sec */
    long FASTEST_INTERVAL = 5 * 60 * 1000;
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
    builder.addLocationRequest(mLocationRequest);
    LocationSettingsRequest locationSettingsRequest = builder.build();

    SettingsClient settingsClient = LocationServices.getSettingsClient(this);
    settingsClient.checkLocationSettings(locationSettingsRequest);

    Handler mainHandler = new Handler(getApplication().getMainLooper());
    Runnable myRunnable = () -> {
      LocationServices.getFusedLocationProviderClient(getApplicationContext())
          .requestLocationUpdates(mLocationRequest, new LocationCallback() {
            @Override public void onLocationResult(LocationResult locationResult) {
              LocationUpdate(locationResult.getLastLocation());
            }
          }, Looper.myLooper());
    };
    mainHandler.post(myRunnable);
  }

  private void renderLocation() {
    OkHttpClient client = new OkHttpClient();
    List<LocationData> locationDataList =
        SaathiAppNew.get().getDB().daoAccess().getLocationList();
    MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    String url = ServiceUrlsImpl.CRM_BASE_URL();
    String serviceMethod = "GeoLocation/SaveGeoLocation";
    RequestBody body = RequestBody.create(MEDIA_TYPE,
        encryptor.encrypt(getLocationMapping(locationDataList)));

    final Request request = new Request.Builder()
        .url(url + serviceMethod)
        .post(body)
        .addHeader("Content-Type",
            encryptor.encrypt("application/json; charset=UTF-8"))
        .addHeader("AppId", encryptor.encrypt("2"))
        .addHeader("clientinfo", encryptor.encrypt("android"))
        .addHeader("EmployeeCode",
            encryptor.encrypt(userManager.getUser().employeeCode))
        .addHeader("token",
            encryptor.encrypt(userManager.getUser().token))
        .addHeader("userid",
            encryptor.encrypt(userManager.getUser().userId))
        .addHeader("ipaddress",
            encryptor.encrypt(getLocalIpAddress()))
        .addHeader("BusinessUnitId",
            encryptor.encrypt(
                userManager.getUser().currentBusinessProfile.businessUnitId))
        .addHeader("macaddress",
            encryptor.encrypt(
                getDeviceID(getApplicationContext().getContentResolver())))
        .build();
    Timber.tag("Location Data Request").d(request.toString());
    Buffer buffer = new Buffer();
    try {
      body.writeTo(buffer);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Timber.tag("Location encrpt ReqBody").d(buffer.readUtf8());
    Timber.tag("Location Data ReqBody").d(getLocationMapping(locationDataList));
    Timber.tag("Location Header").d(request.headers().toString());

    try {
      Response response = client.newCall(request).execute();
      String res = response.body() != null ? response.body().string() : null;
      Timber.tag("Responce w/o decrypt").d(res);
      Timber.tag("Responce with decrypt").d(new String(
          encryptor.decrypt(res)));
      try {
        JSONObject jsonObject;
        jsonObject = new JSONObject(new String(
            encryptor.decrypt(res)));
        String status = jsonObject.getString(STATUS);
        if (status.equalsIgnoreCase("true")) {
          lastLocationAPITime.set(
              AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()));
          SaathiAppNew.get().getDB().daoAccess().deleteAll();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }

      // Do something with the response.
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String getLocationMapping(List<LocationData> locationDataList) {
    String json = "{\"GeoList\":[";
    for (LocationData model : locationDataList) {
      json += "{";
      json += "\"EmployeeCode\":\"" + model.getEmpCode() + "\"";
      json += ",\"GeoArea\":\"" + model.getGeoArea() + "\"";
      json += ",\"GeoLongitude\":\"" + model.getLongitude() + "\"";
      json += ",\"GeoLatitude\":\"" + model.getLatitude() + "\"";
      json += ",\"Time\":\"" + model.getTime() + "\"";
      json += ",\"DeviceId\":\"" + model.getDeviceId() + "\"";
      json += ",\"Accuracy\":\"" + model.getAccuracy() + "\"";
      json += ",\"Id\":\"" + 0 + "\"";
      json += ",\"GeoPinCode\":\"" + model.getGeoPincode() + "\"";

      json += "},";
    }
    json = json.substring(0, json.length() - 1);

    json += "]}";
    return json;
  }

  public class LocalBinder extends Binder {
    public ForegroundService getService() {
      return ForegroundService.this;
    }
  }
}
