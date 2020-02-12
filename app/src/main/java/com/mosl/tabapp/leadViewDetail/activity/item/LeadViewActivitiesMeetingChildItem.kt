package com.mosl.tabapp.leadViewDetail.activity.item

import com.mosl.tabapp.R
import com.mosl.tabapp.databinding.ListMeetingsBinding
import com.mosl.tabapp.model.LeadActivitiesMeetingModel
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

class LeadViewActivitiesMeetingChildItem(
  private val data: LeadActivitiesMeetingModel
) : BindableItem<ListMeetingsBinding>() {

  override fun getLayout(): Int {
    return R.layout.list_meetings
  }

  override fun bind(
    viewBinding: ListMeetingsBinding,
    position: Int
  ) {
    viewBinding.apply {
      try {
        val dateArray = data.date.split("")
        tvMonthName.text = dateArray[1]
        tvMonthDate.text = dateArray[0]
        tvMonthYear.text = dateArray[2]
      } catch (e: Exception) {
        Timber.d(e)
      }

      tvTimeRange.text = data.time
      tvAttFirstName.text = data.attendeeEmp
      tvAppointmentType.text = data.meetingReason

    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as LeadViewActivitiesMeetingChildItem

    if (data != other.data) return false

    return true
  }

  override fun hashCode(): Int {
    return data.hashCode()
  }

}