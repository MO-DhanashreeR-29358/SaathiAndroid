package com.mosl.tabapp.service.location

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.LocationSettingsRequest
import com.mosl.app.core.utils.obtain
import com.mosl.mobile.saathi.AppComponent
import com.mosl.mobile.user.login.UserManager
import com.mosl.shared.util.AppDateFormats
import com.mosl.shared.util.Utility
import com.mosl.shared.util.getDeviceID
import com.mosl.shared.util.hasGPSDevice
import com.mosl.shared.util.isLocationFromMockProvider
import com.mosl.tabapp.SaathiAppNew
import com.mosl.tabapp.database.LocationData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.IOException
import java.util.Locale
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

  @Inject lateinit var userManager: UserManager

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
    /* 10 secs */
    val UPDATE_INTERVAL = (15 * 1000 * 60).toLong()
    mLocationRequest?.interval = UPDATE_INTERVAL
    /* 2 sec */
    val FASTEST_INTERVAL: Long = 60 * 1000
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
    mainHandler.postDelayed(myRunnable, 15 * 60 * 1000)
  }

  fun onLocationChanged(location: Location) {
    var geoArea = ""
    var geoPincode = ""

    val geocoder = Geocoder(context, Locale.getDefault())
    try {
      val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
      geoArea = addressList[0].getAddressLine(0)
      geoPincode = addressList[0].postalCode
    } catch (e: IOException) {
      e.printStackTrace()
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
  }

}