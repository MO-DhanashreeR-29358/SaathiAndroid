package com.mosl.data.attendance

import com.mosl.saathi.api.model.attendance.BranchLocationResponse
import com.mosl.app.core.data.attendance.model.SelfApprovalResponse
import com.mosl.app.core.data.attendance.model.SelfAttendanceResponse
import com.mosl.mobile.domain.attendance.model.OfficeLocation
import com.mosl.mobile.domain.attendance.model.SelfApproval
import com.mosl.mobile.domain.attendance.model.SelfAttendance

object AttendanceMapper {

  fun transform(location: List<BranchLocationResponse>): List<OfficeLocation> {
    return location.map {
        OfficeLocation(
            id = it.branchId,
            latitude = if (it.latitide.isNullOrEmpty()) 0.0 else it.latitide!!.toDouble(),
            locationName = it.branchName,
            longitude = if (it.longitude.isNullOrEmpty()) 0.0 else it.longitude!!.toDouble()
        )
    }
  }

  fun transformSelfApproval(location: List<SelfApprovalResponse>): List<SelfApproval> {
    return location.map {
        SelfApproval(
            heading = it.heading,
            count = it.count,
            url = it.url
        )
    }
  }

  fun transform(it: SelfAttendanceResponse): SelfAttendance {
    return SelfAttendance(
        present = it.present.toInt(),
        absent = it.absent.toInt(),
        balanceLeave = it.balanceLeave.toInt(),
        holiday = it.holiday.toInt(),
        lateIn = it.lateIn.toInt(),
        leaveTaken = it.leaveTaken.toInt(),
        outDoor = it.outDoor.toInt(),
        pendingLeave = it.pendingLeave.toInt()
    )
  }
}