package com.mosl.tabapp.attendancedialog

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.mosl.annotations.ActivityScoped
import com.mosl.tabapp.BuildConfig
import com.mosl.tabapp.settings.GpsCallBack
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@ActivityScoped
@SuppressLint("MissingPermission")
class UserLocationHelper @Inject constructor(
    val activity: AppCompatActivity
) {
    companion object {
        const val REQUEST_CHECK_SETTINGS = 0x1
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    val locationEvent = BehaviorSubject.create<Location>()
    private val locationRequest: LocationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 2000L
        fastestInterval = 2000
    }
    private var requestingLocationUpdates: Boolean = true
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            activity
        )
    private val settingsClient: SettingsClient = LocationServices.getSettingsClient(activity)
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let {
                locationEvent.onNext(it)
            }
            super.onLocationResult(locationResult)
        }
    }
    private val locationSettingsRequest: LocationSettingsRequest = LocationSettingsRequest.Builder()
        .setAlwaysShow(true)
        .addLocationRequest(locationRequest)
        .build()
    private lateinit var gpsCallBack: GpsCallBack
    private var isFromSettingFragment = false

    fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode != REQUEST_CHECK_SETTINGS) return
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (isFromSettingFragment)
                    gpsCallBack.gpsStatus(true)
            }
            Activity.RESULT_CANCELED -> {
                if (!isFromSettingFragment)
                    requestingLocationUpdates = false
                if (isFromSettingFragment)
                    gpsCallBack.gpsStatus(false)
            }
        }
    }

    fun initGpsCallBack(gpsCallBack: GpsCallBack) {
        isFromSettingFragment = true
        this@UserLocationHelper.gpsCallBack = gpsCallBack
    }

    fun start() {
        if (requestingLocationUpdates && checkPermissions()) {
            startLocationUpdates()
        } else if (!checkPermissions()) {
            requestPermissions()
        }
    }

    fun restart() {
        if (checkPermissions()) {
            requestingLocationUpdates = true
        } else {
            requestPermissions()
        }
    }

    fun triggerGetLocation() {
        getLocation()
    }

    fun getLocation() {
        if (requestingLocationUpdates && checkPermissions()) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
            val lastLocation = fusedLocationClient.lastLocation
            lastLocation.addOnSuccessListener { location ->
                location?.let {
                    locationEvent.onNext(it)
                }
            }
        } else if (!checkPermissions()) {
            requestPermissions()
        }
    }

    private fun startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnCompleteListener {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()
                )
            }
            .addOnFailureListener { exception ->
                if (exception is ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            Timber.i(
                                "Location settings are not satisfied. Attempting to upgrade %s",
                                "location settings "
                            )
                            try {
                                val rae = exception as ResolvableApiException
                                rae.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                            } catch (sie: IntentSender.SendIntentException) {
                                Timber.i("PendingIntent unable to execute request.")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage =
                                "Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings."
                            Timber.e(errorMessage)
                            Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG)
                                .show()
                            requestingLocationUpdates = false
                        }
                    }
                }
            }
    }

    fun stop() {
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
            .addOnCompleteListener {
                requestingLocationUpdates = false;
            }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            activity,
            permission.ACCESS_FINE_LOCATION
        )
        /*val permissionCoarseLocationState = ActivityCompat.checkSelfPermission(
            activity,
            permission.ACCESS_COARSE_LOCATION
        )*/

        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {

        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )

        /*val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
          activity,
          arrayOf(
            permission.ACCESS_FINE_LOCATION,
            permission.ACCESS_COARSE_LOCATION,
            permission.ACCESS_BACKGROUND_LOCATION
          ).toString()
        )


        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.")
            val dialog = AlertDialog.Builder(activity)
            dialog.setMessage(activity.getString(R.string.gps_network_not_enabled))
            dialog.setPositiveButton(
              activity.getString(R.string.settings)
            ) { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                if (myIntent.resolveActivity(activity.packageManager) != null) {
                    ActivityCompat.requestPermissions(
                      activity,
                      arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION, permission.ACCESS_BACKGROUND_LOCATION),
                      REQUEST_PERMISSIONS_REQUEST_CODE
                    )
                }
            }
            dialog.setCancelable(false)
            dialog.show()
        } else {
            Timber.i("Requesting permission")

            ActivityCompat.requestPermissions(
              activity,
              arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION, permission.ACCESS_BACKGROUND_LOCATION),
              REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }*/
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_PERMISSIONS_REQUEST_CODE) return

        if (grantResults.isEmpty()) {

        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestingLocationUpdates) {
                startLocationUpdates()
            }
        } else {
            AlertDialog.Builder(activity)
                .setMessage("Permission was denied, but is needed for core\n" +
                        "        functionality.")
                .setPositiveButton("Settings") { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    if (intent.resolveActivity(activity.packageManager) != null) {
                        activity.startActivity(intent)
                    }
                }
                .show()
        }
    }

    fun getLocationAddress(context: Context, latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(activity, Locale.getDefault())
        val addressList = geoCoder.getFromLocation(latitude, longitude, 1)

        return if (!addressList.isNullOrEmpty()) {
            addressList[0].getAddressLine(0)
        } else{
            "Unable to Fetch Address!"
        }
    }
}