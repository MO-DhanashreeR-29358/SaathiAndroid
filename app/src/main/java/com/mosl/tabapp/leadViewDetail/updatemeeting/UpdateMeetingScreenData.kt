package com.mosl.tabapp.leadViewDetail.updatemeeting

import android.os.Parcelable
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UpdateMeetingScreenData(
  @JvmField val meetingId: String,
  @JvmField val leadId: String,
  @JvmField val date: String?,
  @JvmField val time: String,
  @JvmField val product: String,
  @JvmField val ucid: String
) : Parcelable

fun Appointment.mapToUpdateMeetingScreenData(): UpdateMeetingScreenData {
  return UpdateMeetingScreenData(
      meetingId = meetingId,
      leadId = leadId,
      date = date,
      time = time,
      product = product,
      ucid = ucid
  )
}