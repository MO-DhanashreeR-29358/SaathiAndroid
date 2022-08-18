package com.mosl.tabapp.geoLocation

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.*
import com.jakewharton.rxbinding4.view.clicks
import com.mosl.app.core.events.DataAction
import com.mosl.app.core.utils.toast
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onSuccess
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.FusedLocation
import com.mosl.tabapp.databinding.FragmentGeoTaggingBinding
import com.mosl.tabapp.geoLocation.GeoTaggingPresenter
import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent
import com.mosl.tabapp.permission.PermissionCheckHelperFragment
import dagger.android.support.AndroidSupportInjection
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.IOException
import java.util.*
import javax.inject.Inject
import io.reactivex.rxjava3.core.Observable


class GeoTaggingFragment : DialogFragment(), GoogleMap.OnMarkerDragListener, LocationListener,Runnable,
    GeoTaggingView, FusedLocation.LatLongDispatcher{

    internal lateinit var binding: FragmentGeoTaggingBinding
    private val dataActionSubject = PublishSubject.create<DataAction>()
    var uiSubject = PublishSubject.create<CreateMeetingUiEvent>()
    private lateinit var mGoogleMap: GoogleMap
    lateinit var mContext: Context
    private var mCurrentLocation: Location? = null
    private var mSelectedLocation: LatLng? = null
    lateinit var locationManager: LocationManager
    private lateinit var mMarker: Marker
    private var mapValue = GoogleMap.MAP_TYPE_NORMAL
    var handler: Handler? = null
    var countDownTimer: CountDownTimer? = null
    private var mCurrentAddress: String = "No Address Available"
    var location: Location? = null
    var isMockLocationEnabled : Boolean = false
    var isValidDistance : Boolean = true


    @Inject
    lateinit var fusedLocation: FusedLocation

    @Inject
    internal lateinit var presenter: GeoTaggingPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_geo_tagging, container, false)
    }

    fun setData(publishSubject: PublishSubject<CreateMeetingUiEvent>){
        this.uiSubject = publishSubject
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        binding.maps.onStart()
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mContext = context
        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        handler = Handler(Looper.getMainLooper())
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        p0?.let {
            val start = Location("start")
            start.latitude = mCurrentLocation?.latitude ?: 0.0
            start.longitude = mCurrentLocation?.longitude ?: 0.0
            val end = Location("end")
            end.latitude = it.position.latitude
            end.longitude = it.position.longitude
            val distance = start.distanceTo(end)
            if (distance > 700) {
                mMarker.remove()
                addMarker(
                    latLng = LatLng(
                        start.latitude,
                        start.longitude
                    )
                )
                mSelectedLocation = LatLng(start.latitude, start.longitude)
                isValidDistance = false
                mContext.toast("Please select a location less than 700 meters")
            } else {
                mMarker.remove()
                addMarker(it.position)
                mSelectedLocation = it.position
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(it.position))
                isValidDistance = true
                Toast.makeText(
                    mContext,
                    "Distance from current location is : $distance meters",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getAddress(latLng: LatLng): String {
        try {
            val geocoder = Geocoder(mContext, Locale.getDefault())
            val addresses: List<Address>

            addresses = geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            mCurrentAddress = if (!addresses.isNullOrEmpty()) {
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                address
            } else
                "No Address Available"

            setAddress(latLng)
        } catch (io: IOException) {
            io.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mCurrentAddress
    }

    private fun setAddress(latlng: LatLng) {
        binding.txtAddress.text = String.format(
            "Latitude : %.10f \nLongitude : %.10f \nAddress : %s",
            latlng.latitude,
            latlng.longitude,
            mCurrentAddress
        )

    }

    override fun onMarkerDragStart(p0: Marker?) {
    }

    override fun onMarkerDrag(p0: Marker?) {
        p0?.let {
            //            Location.distanceBetween(-34.0, 151.0, it.position.latitude, it.position.longitude, null)
        }
    }

    override fun onLocationChanged(location: Location) {
        mCurrentLocation = location
        mSelectedLocation = LatLng(location.latitude, location.longitude)
        Toast.makeText(
            mContext,
            "Got location " + location.latitude + "###" + location.longitude,
            Toast.LENGTH_SHORT
        ).show()
        locationManager.removeUpdates(this)
        countDownTimer?.cancel()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGeoTaggingBinding.bind(view)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog?.window?.setDimAmount(0f)
        presenter.start()
        dataActionSubject.onNext(DataAction.Fetch)
        fusedLocation.initDispatcher(this)

        binding.apply {
            try {
                maps.onCreate(savedInstanceState)
                maps.onResume() // needed to get the map to display immediately

                MapsInitializer.initialize(requireActivity().applicationContext)
                startTimer()
                maps.getMapAsync { mMaps ->
                    mMaps?.let {
                        mGoogleMap = it
                        mGoogleMap.setOnMarkerDragListener(this@GeoTaggingFragment)

                        binding.linear.visibility = View.VISIBLE
                        handler?.postDelayed(this@GeoTaggingFragment, 200)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    override fun onResume() {
        super.onResume()
        binding.maps.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.maps.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.maps.onStop()
        locationManager.removeUpdates(this)
        handler?.removeCallbacks(this)
        handler = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.maps.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.maps.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.maps.onDestroy()
    }

    override fun onDestroyView() {
        presenter.stop()
        super.onDestroyView()
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onFinish() {
                if (mCurrentLocation == null) {
                    val lat = location?.latitude.toString()
                    val longs = location?.longitude.toString()
                    if (!lat.isEmpty() && !longs.isEmpty() && lat != "0" && longs != "0" && lat != "null" && longs != "null") {
                        val location = Location("LatLngService")
                        location.latitude = lat.toDouble()
                        location.longitude = longs.toDouble()
                        mCurrentLocation = location
                        mSelectedLocation = LatLng(location.latitude, location.longitude)
                        locationManager.removeUpdates(this@GeoTaggingFragment)
                        countDownTimer?.cancel()
                        Timber.d("NO LOCATION FOUND")
                    } else {
                        mContext.toast("Location Not Found.Please move to an open place")
                        locationManager.removeUpdates(this@GeoTaggingFragment)
                        countDownTimer?.cancel()
                        handler?.removeCallbacks(this@GeoTaggingFragment)
                        binding.linear.visibility = View.GONE
                    }
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                Timber.d("COUNTDOWN TIMER LOCATION %s", millisUntilFinished)
            }

        }.start()
    }

    override fun run() {
        Timber.d("LOOKING FOR LOCATION.....")

        if (mCurrentLocation == null) {
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!gps && !network) {
                mContext.toast("Please Enable Location and set it to HighAccuracy Or GPS Only")
                locationManager.removeUpdates(this)
                handler?.removeCallbacks(this)
                countDownTimer?.cancel()
                binding.linear.visibility = View.GONE
                return
            }

            if (gps) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    this
                )
                Timber.d("GPS PROVIDER LOCATION")
            }
            if (network) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    this
                )
                Timber.d("NETWORK PROVIDER LOCATION")
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val gpsLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLastLocation != null) {
                    mCurrentLocation = gpsLastLocation
                    mSelectedLocation = LatLng(gpsLastLocation.latitude, gpsLastLocation.longitude)
                    Timber.d("GOT GPS PROVIDER LOCATION")
                }
            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                val networkLastLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLastLocation != null) {
                    mCurrentLocation = networkLastLocation
                    mSelectedLocation =
                        LatLng(networkLastLocation.latitude, networkLastLocation.longitude)
                    Timber.d("GOT NETWORK PROVIDER LOCATION")
                }
            }

            handler?.postDelayed(this, 200)
        } else {
            afterLocationFetched()
        }

    }

    private fun afterLocationFetched() {
        binding.apply {
            linear.visibility = View.GONE
            linearAddress.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE
            txtNote.visibility = View.VISIBLE

            addMarkerAndZoom()
            handler?.removeCallbacks(this@GeoTaggingFragment)
        }
    }

    private fun addMarkerAndZoom() {
        mCurrentLocation?.let { loc ->
            mGoogleMap.addCircle(
                CircleOptions()
                    .radius(700.0)
                    .center(LatLng(loc.latitude, loc.longitude))
                    .clickable(true)
                    .strokeColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                    .zIndex(122.0f)
                    .visible(true)
            )

            // For showing a move to my location button
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            //need location permission therefore the above check
            mGoogleMap.isMyLocationEnabled = true
            mGoogleMap.uiSettings.let {
                it.isZoomControlsEnabled = true
                it.isZoomGesturesEnabled = true
                it.isCompassEnabled = true
            }

            // For dropping a marker at a point on the Map
            addMarker(
                latLng = LatLng(
                    mCurrentLocation?.latitude ?: 0.0,
                    mCurrentLocation?.longitude ?: 0.0
                )
            )
        }

    }

    private fun addMarker(latLng: LatLng) {
        mMarker = mGoogleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Marker Address")
                .snippet(getAddress(latLng))
                .draggable(true)
        )
        val cameraPos = CameraPosition.Builder()
            .target(latLng).zoom(15f)
            .build()
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos))
    }

    companion object {
        fun getInstance(): GeoTaggingFragment {
            return GeoTaggingFragment()
        }
    }

    override fun onMapTypeClicked(): Observable<Unit> {
        return binding.txtMapType.clicks()
    }

    override fun onSaveClicked(): Observable<Unit> {
        return binding.btnSave.clicks()
    }


    override fun renderMapType(uiModel: UiModel<Unit>) {
        uiModel.onSuccess {
            if (mapValue == GoogleMap.MAP_TYPE_SATELLITE
            ) {
                mapValue = GoogleMap.MAP_TYPE_NORMAL
                binding.txtMapType.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_map_orange_24dp,
                    0,
                    0
                )
                mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            } else if (mapValue == GoogleMap.MAP_TYPE_NORMAL) {
                mapValue = GoogleMap.MAP_TYPE_SATELLITE
                binding.txtMapType.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_satellite_orange_24dp,
                    0,
                    0
                )
                mGoogleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
            }
            binding.maps.invalidate()
        }
    }

    override fun renderSave() {
        if (mCurrentLocation == null) {
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!gps && !network) {
                mContext.toast("Please Enable Location and set it to HighAccuracy Or GPS Only")
                locationManager.removeUpdates(this)
                handler?.removeCallbacks(this)
                countDownTimer?.cancel()
                binding.linear.visibility = View.GONE
                return
            }

            if (gps) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    0f,
                    this
                )
                Timber.d("GPS PROVIDER LOCATION")
            }
            if (network) {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0L,
                    0f,
                    this
                )
                Timber.d("NETWORK PROVIDER LOCATION")
            }

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val gpsLastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpsLastLocation != null) {
                    mCurrentLocation = gpsLastLocation
                    mSelectedLocation = LatLng(gpsLastLocation.latitude, gpsLastLocation.longitude)
                    Timber.d("GOT GPS PROVIDER LOCATION")
                }
            }

            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                val networkLastLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (networkLastLocation != null) {
                    mCurrentLocation = networkLastLocation
                    mSelectedLocation =
                        LatLng(networkLastLocation.latitude, networkLastLocation.longitude)
                    Timber.d("GOT NETWORK PROVIDER LOCATION")
                }
            }

            handler?.postDelayed(this, 200)
        } else {
            afterLocationFetched()
            if(isValidDistance) {
                val locationItem = LocationItem(
                    mSelectedLocation?.latitude.toString(),
                    mSelectedLocation?.longitude.toString(),
                    mCurrentAddress,
                    isMockLocationEnabled
                )
                /* Toast.makeText(
                     requireContext(),
                     "geoTagging - " + locationItem.latitude + "-" + locationItem.longitude + "-" + locationItem.address + "-" + isMockLocationEnabled.toString(),
                     Toast.LENGTH_LONG
                 ).show()*/
                uiSubject.onNext(CreateMeetingUiEvent.OnLocationSaveClick(locationItem))
                dismiss()
            }else{
                mContext.toast("Please select a location less than 700 meters")
            }
        }


    }

    override fun isDataEmpty(): Boolean {
        return false
    }

    override fun onDataAction(): Observable<DataAction> {
        return dataActionSubject
    }

    override fun onLatLongChanged(location: Location?) {
        this.location = location
        this.isMockLocationEnabled = CRMAppUtil.isLocationFromMockProvider(
            mContext,
            location
        )
    }

}
