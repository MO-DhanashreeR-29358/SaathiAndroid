package com.mosl.tabapp.service.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.mosl.app.core.utils.obtain
import com.mosl.mobile.user.login.UserManager
import com.mosl.shared.util.*
import com.mosl.tabapp.AppComponent
import com.mosl.tabapp.SaathiAppNew
import com.mosl.tabapp.database.LocationData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject

class LocationWorker(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    protected val disposable: CompositeDisposable = CompositeDisposable()

    init {
        context.applicationContext.obtain<AppComponent>(AppComponent::class.java)
            .inject(this)
    }

    var mLocationRequest: LocationRequest? = null;

    @Inject
    lateinit var userManager: UserManager

    override fun doWork(): Result {

        try {
            if (userManager.isLoggedIn()) {
                if (hasGPSDevice(context)) {
                    startLocationUpdates()
                }
            }
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.d(e.toString())
            return Result.retry()
        }

    }

    protected fun startLocationUpdates() {
        mLocationRequest = LocationRequest()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        /* 60 secs */
        val UPDATE_INTERVAL = (5 * 1000 * 60).toLong()
        mLocationRequest?.interval = UPDATE_INTERVAL
        /* 60 sec */
        val FASTEST_INTERVAL: Long = 5 * 60 * 1000
        mLocationRequest?.fastestInterval = FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        val mainHandler = Handler(context.getMainLooper())

        val myRunnable = Runnable {

            getFusedLocationProviderClient(context).requestLocationUpdates(
                mLocationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        if (locationResult != null) {
                            onLocationChanged(locationResult.lastLocation)
                        }
                    }
                }, Looper.myLooper()
            )
        }
        mainHandler.postDelayed(myRunnable, 5 * 60 * 1000)
    }

    fun onLocationChanged(location: Location) {
        try {
            var geoArea = ""
            var geoPincode = "000000"

            val geocoder = Geocoder(context, Locale.getDefault())

            val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressList.size > 0) {
                geoArea = addressList[0].getAddressLine(0)
                geoPincode = addressList[0].postalCode ?: "000000"
            }

            val locationData = LocationData(
                location.latitude.toString(),
                location.longitude.toString(), getDeviceID(context.contentResolver),
                userManager.getUser()?.employeeCode,
                AppDateFormats.DF_JSON_DATE9.format(Utility.getCurrentDisplayDate()),
                location.accuracy.toString(),
                geoArea, geoPincode
            )
            if (!isLocationFromMockProvider(context, location)) {
                val db1 = SaathiAppNew.get()
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
            e.printStackTrace()
        }
    }

}