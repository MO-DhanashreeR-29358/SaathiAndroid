package com.mosl.tabapp.leadViewDetail.activitydetail

import android.os.Bundle
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadActDetailConstraintBinding
import com.mosl.tabapp.leadViewDetail.activity.MeetingParcel

class LeadActivitiesDetailFragment : CommonFragment() {

  private lateinit var binding: FragLeadActDetailConstraintBinding

  override fun getLayout(): Int {
    return R.layout.frag_lead_act_detail_constraint
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadActDetailConstraintBinding.bind(view)
    setAppTitle(arguments?.getString("MeetingReason") ?: "Activities Details")
    setUpView()
  }

  private fun setUpView() {

    try {
      val data = arguments?.getParcelable<MeetingParcel>(ARG_MEETING_DETAILS)
      data?.let {

        binding.apply {
          tvMonthDate.text = it.date
          tvTime.text = it.time
          tvMobile.text = it.mobileNumber
          tvEmail.text = it.email
          tvStatus.text = when (it.status) {
            "1" -> getString(R.string.txt_label_active)
            else -> getString(R.string.txt_label_inactive)
          }

          tvProduct.text = it.product
          tvActivityType.text = it.activityType
          tvMeetingReason.text = it.meetingReason
          tvMeetingDuration.text = it.meetingDuration
          tvActionable.text = it.actionable
          tvCategory.text = it.category
          tvQueryDesc.text = it.queryDescription
          tvComments.text = it.comment
          tvLocation.text = it.location
          tvMeetingAddress.text = it.meetingAddress
          tvPincode.text = it.pincode
          tvTravellingMode.text = it.travellingMode
          tvAmount.text = it.amount
          tvGeoMeetingLocation.text = it.location

          when {
            it.attendeeEmployee.isNotEmpty() -> {
              val attendingArray = it.attendeeEmployee.trim()
                  .split(",".toRegex())
              tvAttendeeFirst.text = attendingArray[0]
              tvAttendeeSecond.text = attendingArray[1]
              tvAttendeeThird.text = attendingArray[2]
            }
          }

        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }

  }

  companion object {
    const val ARG_MEETING_DETAILS = "arg_meeting_details"
  }
}