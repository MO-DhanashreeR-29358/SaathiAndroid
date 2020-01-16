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
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.mosl.mobile.user.login.UserManager;
import com.mosl.tabapp.R;
import dagger.android.AndroidInjection;
import javax.inject.Inject;
import timber.log.Timber;

import static com.mosl.tabapp.common.CRMMOSLCommon.hasGPSDevice;
import static java.util.Objects.requireNonNull;

public class ForegroundService extends Service {

	private static final String CHANNEL_ID = "saathi";
	private static final String TAG = ForegroundService.class.getSimpleName();
	static Location mCurrentLocation; // location
	private final IBinder mBinder = new LocalBinder();
	Handler refreshHandler = new Handler();
	int NOTIFICATION_ID = 12345;
	private LocationRequest mLocationRequest;
	@Inject UserManager userManager;
	private Runnable runnableCode = new Runnable() {
		@Override public void run() {
			try {
				if (userManager.isLoggedIn()) {
					LocationManager manager =
						(LocationManager) getApplicationContext().getSystemService(
							Context.LOCATION_SERVICE);
					if (requireNonNull(manager)
						.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(
						getApplicationContext())) {
						//LocationUpdate();
					}
				}
				refreshHandler.postDelayed(runnableCode, 15 * 60 * 1000);
			} catch (Throwable ex) {
				Timber.tag("Exception").d(ex.toString());
			}
		}
	};

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
			requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
		}
		//if (serviceIsRunningInForeground(this)) {
		//	requireNonNull(mNotificationManager).notify(NOTIFICATION_ID, getNotification());
		//}
		startLocationUpdates();

		refreshHandler.postDelayed(runnableCode, 10000);

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

	//private void LocationUpdate() {
	//	new Thread(new Runnable() {
	//		@Override public void run() {
	//			LocationData locationData = new LocationData();
	//			if (mCurrentLocation != null) {
	//				Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
	//				try {
	//					List<Address> addressList =
	//						geocoder.getFromLocation(mCurrentLocation.getLatitude(),
	//							mCurrentLocation.getLongitude(), 1);
	//					locationData.setGeoArea(addressList.get(0).getAddressLine(0));
	//					locationData.setGeoPincode(addressList.get(0).getPostalCode());
	//				} catch (IOException e) {
	//					e.printStackTrace();
	//				}
	//				locationData.setLatitude(mCurrentLocation.getLatitude() + "");
	//				locationData.setLongitude(mCurrentLocation.getLongitude() + "");
	//				locationData.setAccuracy(mCurrentLocation.getAccuracy() + "");
	//				locationData.setEmpCode(
	//					requireNonNull(userManager.getUser()).employeeCode);
	//				locationData.setTime(CRMAppUtil.getCurrentDisplayDate());
	//				locationData.setDeviceId(
	//					getDeviceID(getApplicationContext().getContentResolver()));
	//				if (!isLocationFromMockProvider(getApplicationContext(), mCurrentLocation)) {
	//					requireNonNull(SaathiAppNew.get().getDB()).daoAccess()
	//						.insertLocationList(locationData);
	//					Timber.tag("Location Data entered").d(locationData.toString());
	//				}
	//			} else {
	//				getCurrentLocation();
	//				refreshHandler.postDelayed(runnableCode, 10000);
	//			}
	//		}
	//	}).start();
	//}

	@Override public void onDestroy() {
		super.onDestroy();
	}

	public boolean serviceIsRunningInForeground(Context context) {
		ActivityManager manager =
			(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningServiceInfo service : requireNonNull(manager)
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
		//startForeground(NOTIFICATION_ID, getNotification());
		return true;
	}

	protected void startLocationUpdates() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		/* 10 secs */
		long UPDATE_INTERVAL = 10 * 1000;
		mLocationRequest.setInterval(UPDATE_INTERVAL);
		/* 2 sec */
		long FASTEST_INTERVAL = 2000;
		mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
		builder.addLocationRequest(mLocationRequest);
		LocationSettingsRequest locationSettingsRequest = builder.build();

		SettingsClient settingsClient = LocationServices.getSettingsClient(this);
		settingsClient.checkLocationSettings(locationSettingsRequest);
		FusedLocationProviderClient mFusedLocation =
			LocationServices.getFusedLocationProviderClient(this);
		getCurrentLocation();
	}

	private void getCurrentLocation() {

		Handler mainHandler = new Handler(getApplication().getMainLooper());

		Runnable myRunnable = new Runnable() {
			@Override public void run() {
				if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					&& ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.ACCESS_COARSE_LOCATION)
					!= PackageManager.PERMISSION_GRANTED) {
					return;
				}
				LocationServices.getFusedLocationProviderClient(getApplicationContext())
					.requestLocationUpdates(mLocationRequest, new LocationCallback() {
						@Override public void onLocationResult(LocationResult locationResult) {
							mCurrentLocation = locationResult.getLastLocation();
						}
					}, Looper.myLooper());
			} // This is your code
		};
		mainHandler.post(myRunnable);
	}

	public class LocalBinder extends Binder {
		public ForegroundService getService() {
			return ForegroundService.this;
		}
	}
}
