package com.mosl.tabapp.attendancedialog

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Pair
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mosl.app.core.data.util.LocationUtil
import com.mosl.app.core.rx.applyUiModel
import com.mosl.commons.data.UiModel
import com.mosl.commons.data.onFailure
import com.mosl.mobile.domain.attendance.AttendanceRepository
import com.mosl.mobile.domain.attendance.LatLng
import com.mosl.mobile.domain.attendance.model.OfficeLocation
import com.mosl.mobile.domain.attendance.model.UserLocationInfo
import com.mosl.mobile.user.login.LoginRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.model.ProfileInfo
import com.mosl.tabapp.R
import com.mosl.tabapp.attendancedialog.AttendanceDialogFragment.Companion.ARGS_USER_AREA
import com.mosl.tabapp.attendancedialog.AttendanceDialogFragment.Companion.ARGS_USER_LAT
import com.mosl.tabapp.attendancedialog.AttendanceDialogFragment.Companion.ARGS_USER_LOGIN_BRANCH
import com.mosl.tabapp.attendancedialog.AttendanceDialogFragment.Companion.ARGS_USER_LONG
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class AttendanceHelper @Inject constructor(
    val userLocationHelper: UserLocationHelper,
    val loginRepo: LoginRepository,
    val attendanceRepo: AttendanceRepository,
    val userManager: UserManager,
    val activity: AppCompatActivity,
    val gson: Gson,
    val screenNavigator: ScreenNavigatorImpl
) : AttendanceDialogFragment.SkipListener {
    lateinit var attendanceFragment: AttendanceDialogFragment
    lateinit var clickListener: ClickListener
    val disposable = CompositeDisposable()
    private var checkSkipStatus = false


    fun handleAttendanceAndLocation(clickListener: ClickListener) {
            attendanceFragment = AttendanceDialogFragment()
            this.clickListener = clickListener
            showMockDialogIfLocationFromMockProvider()

            val attendanceObs = userLocationHelper.locationEvent.distinctUntilChanged()
                .filter { location ->
                    !MockLocationDetector.isLocationFromMockProvider(
                        activity,
                        location
                    )
                }
                .map { loc -> LatLng(loc.latitude, loc.longitude) }
                .switchMap<Pair<ProfileInfo, LatLng>> { location ->
                    loginRepo.getProfileInfo()
                        .map { Pair.create(it, location) }
                }
                .share()
            handleAttendanceData(attendanceObs)
    }

    private fun handleAttendanceData(latLngObservable: Observable<Pair<ProfileInfo, LatLng>>) {

        disposable.add(latLngObservable
            .filter { t -> t.first.attendanceType == 1 && t.first.attendanceCount != 1 }
            .map { t -> t.second }
            .switchMap { location ->
                attendanceRepo.getNearestOfficeLocation(location)
                    .compose(applyUiModel())
            }
            .subscribe(this::showAttendancePopupGeoFancing))

        disposable.add(latLngObservable
            .filter { t -> t.first.attendanceType == 2 && t.first.attendanceCount != 1 }
            .map { t -> t.second }
            .switchMap { location ->
                attendanceRepo.getOfficeLocationList(location)
                    .compose(applyUiModel())
            }
            .subscribe(this::showAttendancePopupWFH))

        disposable.add(latLngObservable
            .filter { t -> t.first.attendanceCount == 1 }
            .map { it -> it.first }
            .compose<UiModel<ProfileInfo>>(applyUiModel<ProfileInfo>())
            .subscribe {
                clickListener.setInTime(it)
            })

        disposable.add(latLngObservable
                .filter { t -> t.first.attendanceType == 3 }
                .map { it -> it.first }
                .compose<UiModel<ProfileInfo>>(applyUiModel<ProfileInfo>())
                .subscribe {
                    clickListener.setMessageForAttendanceTypeThree(it)
                })
    }

    fun OfficeLocation.distance(userLocation: LatLng): Double {
        return LocationUtil.distance(
            latitude, longitude,
            userLocation.latitude, userLocation.longitude
        )
    }

    private fun showAttendancePopupGeoFancing(uiModel: UiModel<UserLocationInfo>) {

        uiModel.onFailure {
            userLocationHelper.stop()
        }
        uiModel.data?.let { data ->

            val supportFragmentManager = activity.supportFragmentManager
            if (!attendanceFragment.isAdded && !supportFragmentManager.isStateSaved) {
                val bundle = Bundle()
                if (userManager.getUser()!!.collectionOfficer) {
                    userLocationHelper.stop()
                    bundle.putString(
                        AttendanceDialogFragment.ARGS_EMP_ACTION,
                        AttendanceDialogView.COLLECTION_OFFICER
                    )
                } else {
                    bundle.putString(
                        AttendanceDialogFragment.ARGS_EMP_ACTION,
                        AttendanceDialogView.SIGN_IN
                    )
                }
                bundle.putDouble(ARGS_USER_LAT, data.userLocation.latitude)
                bundle.putDouble(ARGS_USER_LONG, data.userLocation.longitude)
                bundle.putString(
                    ARGS_USER_LOGIN_BRANCH, ""

                )
                bundle.putString(ARGS_USER_AREA, data.officeLocation.locationName)
                attendanceFragment.arguments = bundle
                attendanceFragment.onSkipEvent(this)
                attendanceFragment.show(supportFragmentManager, "Attendance")
            }
        }
    }

    private fun showAttendancePopupWFH(uiModel: UiModel<kotlin.Pair<List<OfficeLocation>, LatLng>>) {
        uiModel.onFailure {
            userLocationHelper.stop()
        }
        uiModel.data?.let { data ->
            var nearestOffice = OfficeLocation(0.0, 0.0, 1, "")
            try {
                nearestOffice = data.first.first {
                    it.distance(data.second) < 100
                }
            } catch (e: Exception) {

            }
            val supportFragmentManager = activity.supportFragmentManager
            if (!attendanceFragment.isAdded && !supportFragmentManager.isStateSaved) {
                val bundle = Bundle()
                var geoArea = ""
                val geocoder = Geocoder(activity.applicationContext, Locale.getDefault())
                val addressList =
                    geocoder.getFromLocation(data.second.latitude, data.second.longitude, 1)
                if (!addressList.isNullOrEmpty()) {
                    geoArea = addressList[0].getAddressLine(0).toString()
                }
                if (userManager.getUser()!!.collectionOfficer) {
                    userLocationHelper.stop()
                    bundle.putString(
                        AttendanceDialogFragment.ARGS_EMP_ACTION,
                        AttendanceDialogView.COLLECTION_OFFICER
                    )
                } else {
                    bundle.putString(
                        AttendanceDialogFragment.ARGS_EMP_ACTION,
                        AttendanceDialogView.SIGN_IN
                    )
                }
                bundle.putDouble(ARGS_USER_LAT, data.second.latitude)
                bundle.putDouble(ARGS_USER_LONG, data.second.longitude)
                bundle.putString(
                    ARGS_USER_LOGIN_BRANCH, ""

                )
                if (nearestOffice.locationName.isEmpty()) {
                    bundle.putString(ARGS_USER_AREA, geoArea)
                } else {
                    bundle.putString(ARGS_USER_AREA, nearestOffice.locationName)
                }
                attendanceFragment.arguments = bundle
                attendanceFragment.onSkipEvent(this)
                attendanceFragment.show(supportFragmentManager, "Attendance")
            }
        }
    }

    private fun showMockDialogIfLocationFromMockProvider() {
        disposable.add(
            userLocationHelper.locationEvent
                .distinctUntilChanged()
                .filter { location ->
                    MockLocationDetector.isLocationFromMockProvider(
                        activity, location
                    )
                }
                .take(1)
                .subscribe { showMockLocationDialog(activity) }
        )
    }

    internal fun showMockLocationDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.alert_dialog_title))
            .setMessage(context.getString(R.string.mock_location_enabled))
            .setPositiveButton(
                context.getString(R.string.exit)) { _, _ -> clickListener.exit() }
            .setCancelable(false)
            .show()
        alertDialog.setCanceledOnTouchOutside(false)
    }

    interface ClickListener {
        fun exit()
        fun setInTime(uiModel: UiModel<ProfileInfo>)

        fun setMessageForAttendanceTypeThree(uiModel: UiModel<ProfileInfo>)
    }

    override fun skipClick(status: Boolean) {
        checkSkipStatus = status
    }

}