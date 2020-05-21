package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.LookUp

object CreateMeetingConstants {
  @JvmField
  val SELECT_ACTIVITY_TYPE = LookUp(0, "Select Activity Type")

  @JvmField
  val SELECT_MEETING_LOCATION = LookUp(0, "Select Meeting Location")

  @JvmField
  val SELECT_MEETING_REASON = LookUp(0, "Select Meeting Reason")

  fun hasAttendeeAdded(
    list: ArrayList<AddedAttendee>,
    empCode: String
  ): Boolean {
    var hasDataAdded = false
    if (list.isNotEmpty()) {
      for (addedAttendee in list) {
        if (addedAttendee.attendeeCode.trim().equals(empCode.trim())) {
          hasDataAdded = true
        }
      }
    }
    return hasDataAdded
  }
}