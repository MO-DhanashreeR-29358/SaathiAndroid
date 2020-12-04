package com.mosl.tabapp.settings

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mosl.app.core.prefs.BooleanPreference
import com.mosl.mobile.app_user.TWENTY_FOUR_SEVEN_CHECK
import com.mosl.mobile.user.annotation.TwentyFourSevenCheck
import com.mosl.shared.util.isLocationFromMockProvider
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.service.ForegroundService
import com.mosl.tabapp.util.Utility.Companion.initBackgroundState
import dagger.android.support.AndroidSupportInjection
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SettingFragment : PreferenceFragmentCompat() {

    lateinit var preference: SwitchPreference

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var mService: ForegroundService? = null

    @Inject
    internal lateinit var fusedLocation: FusedLocation

    @field:TwentyFourSevenCheck
    @Inject
    lateinit var twentyFourSevenCheck: BooleanPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference, rootKey)
        preference =
            findPreference<SwitchPreference>(getString(R.string.preference_location_key)) as SwitchPreference
        fusedLocation.initGpsCallBack(gpsCallBack)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastKnownLocation()

        preference.isChecked = twentyFourSevenCheck.get()
        if (!isGpsEnabled())
            disableToggleButton()
        preference.setOnPreferenceChangeListener { _: Preference, newValue: Any ->
            if (newValue is Boolean) {
                if (newValue)
                    if (isGpsEnabled())
                        enableToggleButton()
                    else
                        fusedLocation.init()
                else
                    disableToggleButton()
            }
            return@setOnPreferenceChangeListener true
        }
    }

    private fun isGpsEnabled(): Boolean =
        (requireContext().getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager).isProviderEnabled(
            android.location.LocationManager.GPS_PROVIDER
        )


    private fun disableToggleButton() {
        Toast.makeText(
            requireContext(),
            "Location tracking stopped",
            Toast.LENGTH_SHORT
        ).show()

        twentyFourSevenCheck.set(false)
        stopService()
        preference.isChecked = false
    }

    private fun enableToggleButton() {
        Toast.makeText(
            requireContext(), "Location tracking started", Toast.LENGTH_SHORT
        )
            .show()
        twentyFourSevenCheck.set(true)
        startService()
    }


    private val gpsCallBack = GpsCallBack {
        if (it)
            enableToggleButton()
        else
            disableToggleButton()
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                requireContext(),
                "Please Provide Location Permission!",
                Toast.LENGTH_SHORT
            ).show()
            fusedLocation.requestPermissions()
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        updateLongLat(location.longitude, location.latitude, location)
                    }
                }
        }
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    private fun updateLongLat(
        longitude: Double,
        latitude: Double,
        location: Location
    ) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)

        val prefLatitude = findPreference<Preference>(getString(R.string.preference_latitude_key))
        val prefLongitude = findPreference<Preference>(getString(R.string.preference_longitude_key))

        if (!isLocationFromMockProvider(requireContext(), location)) {
            prefLatitude?.summary = latitude.toString()
            prefLongitude?.summary = longitude.toString()
        } else {
            showToast(getString(R.string.mock_location_label))
        }

        val prefLocation = findPreference<Preference>(getString(R.string.preference_loc_key))
        if (addressList.size > 0 && !isLocationFromMockProvider(requireContext(), location)) {
            prefLocation?.summary = addressList[0].getAddressLine(0)
        }

        Timber.d("lat n long %s%s", latitude, longitude)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ForegroundService.LocalBinder
            mService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mService = null
        }
    }

    private fun startService() {
        initBackgroundState(requireContext())
        requireActivity().bindService(
            Intent(requireContext(), ForegroundService::class.java),
            mServiceConnection,
            Context.BIND_AUTO_CREATE
        )
        requireActivity().startService(Intent(requireContext(), ForegroundService::class.java))
    }

    private fun stopService() {
        val serviceIntent = Intent(requireContext(), ForegroundService::class.java)
        activity?.stopService(serviceIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != FusedLocation.REQUEST_PERMISSIONS_REQUEST_CODE)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        else fusedLocation.onRequestPermissionsResult(
            requestCode,
            permissions as Array<String>, grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != FusedLocation.REQUEST_CHECK_SETTINGS)
            super.onActivityResult(requestCode, resultCode, data)
        else
            fusedLocation.onActivityResult(requestCode, resultCode, data)
    }
}