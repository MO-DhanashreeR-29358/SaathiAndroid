package com.mosl.app.core.data.attendance

import android.content.Context
import com.mosl.app.core.data.util.LocationUtil
import com.mosl.data.attendance.AttendanceMapper
import com.mosl.mobile.data.util.CommonBaseUrls
import com.mosl.mobile.domain.attendance.AttendanceRepository
import com.mosl.mobile.domain.attendance.LatLng
import com.mosl.mobile.domain.attendance.model.MarkAttendanceData
import com.mosl.mobile.domain.attendance.model.OfficeLocation
import com.mosl.mobile.domain.attendance.model.UserLocationInfo
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.model.ProfileInfo
import com.mosl.saathi.api.MyZoneApi
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.attendance.LogAttendancePopUpShownRequest
import com.mosl.saathi.api.model.attendance.MarkAttendanceRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    private val myZoneApi: MyZoneApi,
    val userManager: UserManager,
    val context: Context
) : AttendanceRepository {
  val mapper = AttendanceMapper
  val ALLOWED_RADIUS = 100

  override fun getOfficeLocationList(location: LatLng): Observable<Pair<List<OfficeLocation>, LatLng>> {
    return officeLocation().map { offices ->
      Pair(offices, location)
    }
  }

  override fun getNearestOfficeLocation(location: LatLng): Observable<UserLocationInfo> {
    return officeLocation().map { offices ->
      val nearestOffice = offices.first {
        it.distance(location) < ALLOWED_RADIUS
      }

        UserLocationInfo(
            distance = nearestOffice.distance(location),
            officeLocation = nearestOffice,
            userLocation = location
        )
    }
  }

    override fun getNearestOfficeLocations(location: LatLng): Observable<UserLocationInfo> {
        return officeLocation().map { offices ->
            val nearestOffice = offices.firstOrNull {
                it.distance(location) < ALLOWED_RADIUS
            } ?: OfficeLocation(0.0, 0.0, 1, "")

            UserLocationInfo(
                distance = nearestOffice.distance(location),
                officeLocation = nearestOffice,
                userLocation = location
            )
        }
    }

  fun OfficeLocation.distance(userLocation: LatLng): Double {
    return LocationUtil.distance(
        latitude, longitude,
        userLocation.latitude, userLocation.longitude
    )
  }

  override fun officeLocation(): Observable<List<OfficeLocation>> {
    return myZoneApi.getBranchLocations()
        .map { AttendanceMapper.transform(it) }
  }

  override fun markEmpAttendance(markAttendanceData: MarkAttendanceData): Observable<String> {
    return myZoneApi.markAttendance(
            MarkAttendanceRequest(
                moodId = markAttendanceData.mood,
                latitude = markAttendanceData.officeLocation.latitude,
                longitude = markAttendanceData.officeLocation.longitude,
                geoArea = markAttendanceData.geoArea
            )
        ).map { it.data }
    }

    override fun markOutAttendance(markAttendanceData: MarkAttendanceData): Observable<String> {
        return myZoneApi.markOutAttendance(
            MarkAttendanceRequest(
                moodId = markAttendanceData.mood,
                latitude = markAttendanceData.officeLocation.latitude,
                longitude = markAttendanceData.officeLocation.longitude,
                geoArea = markAttendanceData.geoArea
            )
        ).map { it.data }
    }

    override fun logAttendancePopup(employeeCode: String): Observable<String> {
        val request = LogAttendancePopUpShownRequest(employeeCode)
        return myZoneApi.logAttendancePopShown(request)
    }


}