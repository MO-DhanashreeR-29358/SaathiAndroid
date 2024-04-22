package com.mosl.mobile.domain.attendance

import com.mosl.mobile.domain.attendance.model.MarkAttendanceData
import com.mosl.mobile.domain.attendance.model.OfficeLocation
import com.mosl.mobile.domain.attendance.model.UserLocationInfo
import io.reactivex.rxjava3.core.Observable

interface AttendanceRepository {
  fun getOfficeLocationList(location: LatLng): Observable<Pair<List<OfficeLocation>, LatLng>>
  fun getNearestOfficeLocation(location: LatLng): Observable<UserLocationInfo>
  fun getNearestOfficeLocations(location: LatLng): Observable<UserLocationInfo>

  fun officeLocation(): Observable<List<OfficeLocation>>

  fun markEmpAttendance(markAttendanceData: MarkAttendanceData): Observable<String>

  fun markOutAttendance(markAttendanceData: MarkAttendanceData): Observable<String>

  fun logAttendancePopup(employeeCode: String): Observable<String>


}