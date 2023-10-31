package com.mosl.tabapp.leadViewDetail.activity

import android.os.Parcelable
import com.mosl.mobile.domain.leadviewdetail.activities.Meeting
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MeetingParcel(
  @JvmField val mobileNumber: String,
  @JvmField val category: String,
  @JvmField val actionable: String,
  @JvmField val product: String,
  @JvmField val email: String,
  @JvmField val name: String,
  @JvmField val attendeeEmployee: String,
  @JvmField val travellingMode: String,
  @JvmField val meetingAddress: String,
  @JvmField val meetingId: String,
  @JvmField val stage: String,
  @JvmField val meetingAddressType: String,
  @JvmField val customerServiceRemark: String,
  @JvmField val meetingReason: String,
  @JvmField val status: String,
  @JvmField val queryDescription: String,
  @JvmField val comment: String,
  @JvmField val amount: String,
  @JvmField val activityType: String,
  @JvmField val time: String,
  @JvmField val statusName: String,
  @JvmField val date: String?,
  @JvmField val leadId: String,
  @JvmField val meetingDuration: String,
  @JvmField val pincode: String,
  @JvmField val location: String,
  @JvmField val touchedUntouched: String,
  @JvmField val feedback: String,
  @JvmField val geoTag: String,
  @JvmField val eventName: String
) : Parcelable

fun Meeting.mapToMeetingParcel(): MeetingParcel {
  return MeetingParcel(
      mobileNumber = mobileNumber,
      category = category,
      actionable = actionable,
      product = product,
      email = email,
      name = name,
      attendeeEmployee = attendeeEmployee,
      travellingMode = travellingMode,
      meetingAddress = meetingAddress ?: "",
      meetingId = meetingId,
      stage = stage,
      meetingAddressType = meetingAddressType,
      customerServiceRemark = customerServiceRemark,
      meetingReason = meetingReason,
      status = status,
      queryDescription = queryDescription,
      comment = comment,
      amount = amount,
      activityType = activityType,
      time = time,
      statusName = statusName,
      date = date,
      leadId = leadId,
      meetingDuration = meetingDuration,
      pincode = pincode,
      location = location,
      touchedUntouched = touchedUntouched,
      feedback = feedback,
      geoTag = geoTag,
      eventName = eventName
  )
}