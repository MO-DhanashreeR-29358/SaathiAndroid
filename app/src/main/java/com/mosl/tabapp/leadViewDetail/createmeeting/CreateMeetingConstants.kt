package com.mosl.tabapp.leadViewDetail.createmeeting

import com.mosl.mobile.domain.createmeeting.AddedAttendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.createmeeting.MobileListData

object CreateMeetingConstants {
  @JvmField
  val SELECT_ACTIVITY_TYPE = LookUp(0, "Select Activity Type")

  @JvmField
  val SELECT_MOBILE_NUMBER = MobileListData("Select Mobile Number For Verification Code", "0")

  @JvmField
  val SELECT_PRODUCT = LookUp(0, "Select Product")
  val SELECT_ACTIONABLE = LookUp(0, "Select Actionables")
  val SELECT_CATEGORY = LookUp(0, "Select Category")
  val SELECT_SUB_CATEGORY = LookUp(0, "Select Sub Category")

  @JvmField
  val SELECT_MEETING_LOCATION = LookUp(0, "Select Meeting Location")
  val SELECT_MEETING_DURATION = LookUp(0, "Select Meeting Duration")
  val SELECT_MEETING_OBJECTIVE = LookUp(0, "Select Meeting Objective")

  @JvmField
  val SELECT_MEETING_REASON = LookUp(0, "Select Meeting Reason")
  val SELECT_BA_CLIENT_MEETING = LookUp(0, "Select BA Client Meeting")
  val SELECT_OUT_STATION_MEETING = LookUp(0, "Select Out Station Meeting")
  val SELECT_TRAVELLING_MODE = LookUp(0, "Select Travelling Mode")
  val SELECT_TOUCHED_UNTOUCHED = LookUp(0, "Select Touched Untouched")
  val SELECT_FEEDBACK = LookUp(0, "Select Feedback")

  fun hasAttendeeAdded(
    list: ArrayList<AddedAttendee>,
    empCode: String
  ): Boolean {
    var hasDataAdded = false
    if (list.isNotEmpty()) {
      for (addedAttendee in list) {
        if (addedAttendee.attendeeCode.trim() == empCode.trim()) {
          hasDataAdded = true
        }
      }
    }
    return hasDataAdded
  }

  const val MSG_SELECT_ACTIVITY_TYPE = "Select Activity Type"
  const val MSG_SELECT_MOBILE_NUMBER = "Select Mobile Number For Verification Code"
  const val MSG_SELECT_PRODUCT = "Select Product"
  const val MSG_SELECT_ACTIONABLE = "Select Actionables"
  const val MSG_SELECT_CATEGORY = "Select Category"
  const val MSG_SELECT_SUB_CATEGORY = "Select Sub Category"
  const val MSG_SELECT_MEETING_DURATION = "Select Meeting Duration"
  const val MSG_SELECT_MEETING_LOCATION = "Select Meeting Location"
  const val MSG_SELECT_MEETING_OBJECTIVE = "Select Meeting Objective"
  const val MSG_SELECT_MEETING_REASON = "Select Meeting Reason"
  const val MSG_SELECT_BA_CLIENT_MEETING = "Select BA Client Meeting"
  const val MSG_SELECT_OUT_STATION_MEETING = "Select Out Station Meeting"
  const val MSG_SELECT_TRAVELLING_MODE = "Select Travelling Mode"
  const val MSG_MEETING_DESCRIPTION_CANNOT_BE_LEFT_BLANK =
    "Meeting description cannot be left blank"
  const val MSG_ATLEAST_FIFTY_CHARACTERS = "Atleast 50 characters"
  const val MSG_ATLEAST_TWENTY_CHARACTERS = "Atleast 20 characters"
  const val MSG_ATLEAST_TEN_CHARACTERS = "Atleast 10 characters"
  const val MSG_LOCATION_CANNOT_BE_LEFT_BLANK = "Meeting address cannot be left blank"
  const val MSG_INVALID_PINCODE_NUMBER = "Invalid pincode number"
  const val MSG_ENTER_CLIENT_CODE = "Enter Client Code"
  const val MSG_ENTER_CLIENT_NAME = "Enter Client Name"
}