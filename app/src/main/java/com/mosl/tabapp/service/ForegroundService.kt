package com.mosl.tabapp.service

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Binder
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat.Builder
import com.google.android.gms.location.*
import com.mosl.app.core.network.isNetworkConnected
import com.mosl.app.core.prefs.StringPreference
import com.mosl.mobile.domain.location.LocationRepository
import com.mosl.mobile.domain.location.LocationResponse
import com.mosl.mobile.user.annotation.LastLocationAPITime
import com.mosl.mobile.user.annotation.LastLocationTime
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.shared.util.*
import com.mosl.tabapp.BuildConfig
import com.mosl.tabapp.R.drawable
import com.mosl.tabapp.R.string
import com.mosl.tabapp.SaathiAppNew.Companion.get
import com.mosl.tabapp.database.LocationData
import com.mosl.tabapp.service.location.transformLocationData
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ForegroundService : Service() {
    private val CHANNEL_ID = "saathi"
    private val TAG = ForegroundService::class.java.simpleName
    private val mBinder: IBinder = LocalBinder()
    var refreshHandler = Handler()
    var refreshHandlerAPI = Handler()
    var NOTIFICATION_ID = 12345

    @Inject
    lateinit var userManager: UserManager
    var mLocationRequest: LocationRequest? = null
    protected val disposable: CompositeDisposable = CompositeDisposable()

    @field:LastLocationTime
    @Inject
    lateinit var lastLocationTime: StringPreference

    @Inject
    lateinit var saathiApi: SaathiApi

    @Inject
    lateinit var repository: LocationRepository

    @field:LastLocationAPITime
    @Inject
    lateinit var lastLocationAPITime: StringPreference

    private val runnableCode: Runnable = object : Runnable {
        override fun run() {
            if (userManager.isLoggedIn() && hasGPSDevice(applicationContext)) {
                startLocationUpdates()
            }
            refreshHandler.postDelayed(this, 5 * 60 * 1000.toLong())
        }
    }

    private val runnableCodeAPI: Runnable = object : Runnable {
        override fun run() {
            if (isNetworkConnected() && userManager.isLoggedIn()) {
                initLocationApi()
            }
            refreshHandlerAPI.postDelayed(this, 15 * 60 * 1000.toLong())
        }
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val name: CharSequence = getString(string.app_name)
            // Create the channel for the notification
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel)
        }
        if (serviceIsRunningInForeground(this)) {
            mNotificationManager.notify(NOTIFICATION_ID, getNotification())
        }
        refreshHandler.postDelayed(runnableCode, 1000)
        refreshHandlerAPI.postDelayed(runnableCodeAPI, 1000 * 10.toLong())
        return START_STICKY
    }

    private fun getNotification(): Notification? {
        val notificationIntent = Intent(this, ForegroundService::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, notificationIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        val notification =
            Builder(this, CHANNEL_ID)
                .setContentText("We are tracking your location")
                .setContentTitle(getString(string.app_name))
                .setSmallIcon(drawable.ic_icon)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            notification.setChannelId(
                CHANNEL_ID
            ) // Channel ID
        }
        return notification.build()
    }

    fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager
            .getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    override fun onBind(intent: Intent): IBinder {
        Timber.tag(TAG).i("in onBind()")
        stopForeground(true)
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        Timber.tag(TAG).i("in onRebind()")
        stopForeground(true)
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Timber.tag(TAG).i("Last client unbound from service")
        startForeground(NOTIFICATION_ID, getNotification())
        return true
    }

    fun initLocationApi() {

        if ((lastLocationAPITime.get().isEmpty() ||
                    Utility.isTimeInterval(lastLocationAPITime.get(), /*27*/13))
        ) {
            val db1 = get()
                .getDB()
            Observable.just(db1)
                .subscribeOn(Schedulers.io())
                .subscribe { db ->
                    if (db?.daoAccess()?.locationList?.size != 0) {
                        locationUpdate()
                    }
                }.addTo(disposable)

        }

    }

    private fun locationUpdate() {
        val locationResponse = repository.getGeoLocation(
            get().getDB()?.daoAccess()?.locationList!!.transformLocationData()
        ).blockingFirst()
        renderLocation(locationResponse)
    }

    private fun renderLocation(uiModel: LocationResponse) {
        uiModel.isSuccessful.let {
            lastLocationAPITime.set(AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()))
            get().getDB()?.daoAccess()?.deleteAll()
        }
    }

    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        /* 60 secs */
        val UPDATE_INTERVAL = (5 * 60 * 1000).toLong()
        mLocationRequest?.interval = UPDATE_INTERVAL
        /* 60 sec */
        val FASTEST_INTERVAL: Long = 5 * 60 * 1000
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(applicationContext)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        val mainHandler = Handler(mainLooper)

        val myRunnable = Runnable {


            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) else {
                LocationServices.getFusedLocationProviderClient(applicationContext)
                    .requestLocationUpdates(
                        mLocationRequest, object : LocationCallback() {
                            override fun onLocationResult(locationResult: LocationResult?) {
                                if (locationResult != null) {
                                    onLocationChanged(locationResult.lastLocation)
                                }
                            }
                        }, Looper.myLooper()
                    )
            }
        }
        mainHandler.postDelayed(myRunnable, 5 * 60 * 1000)
    }

    fun onLocationChanged(location: Location) {
        try {
            var geoArea = ""
            var geoPincode = "000000"

            val geocoder = Geocoder(applicationContext, Locale.getDefault())
            val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList.size > 0) {
                geoArea = addressList[0].getAddressLine(0)
                geoPincode = addressList[0].postalCode ?: "000000"
            }

            val locationData = LocationData(
                location.latitude.toString(),
                location.longitude.toString(), getDeviceID(contentResolver),
                userManager.getUser()?.employeeCode ?: "0",
                AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()),
                location.accuracy.toString(),
                geoArea, geoPincode, BuildConfig.VERSION_CODE.toString()

            )
            if ((lastLocationTime.get().isEmpty() || Utility.isTimeInterval(
                    lastLocationTime.get(),
                    4/*12*/
                )) && !isLocationFromMockProvider(applicationContext, location)
            ) {
                lastLocationTime.set(AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()))
                val db1 = get()
                    .getDB()
                Observable.just(db1)
                    .subscribeOn(Schedulers.io())
                    .subscribe { db ->
                        db?.daoAccess()
                            ?.insertLocationList(locationData)
                    }
                    .addTo(disposable)

                Timber.tag("Location Data entered")
                    .d(locationData.toString())
            }
        } catch (e: IOException) {
            Timber.tag("LocatnChange Exception").d(e)
            e.printStackTrace()
        }
    }

    inner class LocalBinder : Binder() {
        fun getService(): ForegroundService {
            return this@ForegroundService
        }
    }

}