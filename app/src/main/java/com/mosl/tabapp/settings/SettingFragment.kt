package com.mosl.tabapp.settings

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mosl.mobile.app_user.TWENTY_FOUR_SEVEN_CHECK
import com.mosl.tabapp.R
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.service.ForegroundService
import com.mosl.tabapp.util.AlertDialogHelper
import com.mosl.tabapp.util.Utility.Companion.initBackgroundState
import dagger.android.AndroidInjection
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class SettingFragment @Inject constructor() : PreferenceFragmentCompat() {

    lateinit var preference: SwitchPreference

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var mService: ForegroundService? = null

//    @field:TwentyFourSevenCheck
//    @Inject
//    lateinit var twentyFourSevenCheck: BooleanPreference

    /**/
    lateinit var sharedPreferences: SharedPreferences
    var editor: SharedPreferences.Editor? = null
    /**/

    @Inject
    internal lateinit var fusedLocation: FusedLocation

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preff_24_7_key),
            Context.MODE_PRIVATE
        )
        editor = sharedPreferences.edit()


        setPreferencesFromResource(R.xml.preference, rootKey)
        preference =
            findPreference<SwitchPreference>(getString(R.string.preference_location_key)) as SwitchPreference

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getLastKnownLocation()

//        preference.isChecked = twentyFourSevenCheck.get()
        preference.isChecked = sharedPreferences.getBoolean(TWENTY_FOUR_SEVEN_CHECK, false)
        preference.setOnPreferenceChangeListener { _: Preference, newValue: Any ->
            if (newValue is Boolean) {
                if (::fusedLocation.isInitialized)
                    fusedLocation.initV2()
                if (newValue) {
//                    showPrefrenceDialog(preference)
                    Toast.makeText(
                        requireContext(), "Location tracking started", Toast.LENGTH_SHORT
                    )
                        .show()
//                twentyFourSevenCheck.set(true)
                    editor?.putBoolean(TWENTY_FOUR_SEVEN_CHECK, true)
                    editor?.commit()
                    startService()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location tracking stopped",
                        Toast.LENGTH_SHORT
                    ).show()

//                    twentyFourSevenCheck.set(false)
                    editor?.putBoolean(TWENTY_FOUR_SEVEN_CHECK, false)
                    editor?.commit()
                    stopService()
                }
            }
            return@setOnPreferenceChangeListener true
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(
                requireContext(),
                "Please Provide Location Permission!",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        updateLongLat(location.longitude, location.latitude)
                    }
                }
        }
    }

    private fun updateLongLat(
        longitude: Double,
        latitude: Double
    ) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addressList = geocoder.getFromLocation(latitude, longitude, 1)

        val prefLatitude = findPreference<Preference>(getString(R.string.preference_latitude_key))
        prefLatitude?.summary = latitude.toString()

        val prefLongitude =
            findPreference<Preference>(getString(R.string.preference_longitude_key))
        prefLongitude?.summary = longitude.toString()

        val prefLocation = findPreference<Preference>(getString(R.string.preference_loc_key))
        if (addressList.size > 0) {
            prefLocation?.summary = addressList[0].getAddressLine(0)
        }

        Timber.d("lat n long %s%s", latitude, longitude)


//        }
    }

    private fun showPrefrenceDialog(preference: Preference) {
        AlertDialogHelper.alertDialog(
            requireContext(), 0,
            resources.getString(R.string.alert_dialog_title),
            resources.getString(R.string.alert_dialog_location_message),
            "I Agree", "Cancel", false
        ) { action, _ ->
            if (action === AlertDialogHelper.Action.OK) {
                Timber.d("I Agree Clicked")
                Toast.makeText(
                    requireContext(), "Location tracking started", Toast.LENGTH_SHORT
                )
                    .show()
//                twentyFourSevenCheck.set(true)
                editor?.putBoolean(TWENTY_FOUR_SEVEN_CHECK, true)
                editor?.commit()
                startService()
            } else if (action === AlertDialogHelper.Action.CANCEL) {
                Timber.d("Cancel Clicked")
                if (preference is SwitchPreference) {
                    preference.isChecked = false
                }
            }
        }
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
}