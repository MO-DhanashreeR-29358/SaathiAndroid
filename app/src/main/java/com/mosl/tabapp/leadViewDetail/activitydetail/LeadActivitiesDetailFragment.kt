package com.mosl.tabapp.leadViewDetail.activitydetail

import android.os.Bundle
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.constants.MOSLConstants.DATE
import com.mosl.tabapp.constants.MOSLConstants.EMAIL
import com.mosl.tabapp.constants.MOSLConstants.MOBNO
import com.mosl.tabapp.constants.MOSLConstants.STATUS
import com.mosl.tabapp.constants.MOSLConstants.TIME
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadActDetailBinding

class LeadActivitiesDetailFragment : CommonFragment() {

  private lateinit var binding: FragLeadActDetailBinding

  override fun getLayout(): Int {
    return R.layout.frag_lead_act_detail
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadActDetailBinding.bind(view)
    setAppTitle(arguments?.getString("MeetingReason") ?: "Activities Details")
    setUpView()
  }

  private fun setUpView() {
    binding.apply {

      try {
        arguments?.let {
          tvMonthDate.text = it.getString(DATE)
          tvTime.text = it.getString(TIME)
          tvMobile.text = it.getString(MOBNO)
          tvEmail.text = it.getString(EMAIL)
          tvStatus.text = when (it.getString(STATUS)) {
            "1" -> getString(R.string.txt_label_active)
            else -> getString(R.string.txt_label_inactive)
          }

          tvProduct.text = it.getString("Product")
          tvActivityType.text = it.getString("ActivityType")
          tvMeetingReason.text = it.getString("MeetingReason")
          tvMeetingDuration.text = it.getString("MeetingDuration")
          tvActionable.text = it.getString("Actionable")
          tvCategory.text = it.getString("Category")
          tvQueryDesc.text = it.getString("QueryDesc")
          tvCSR.text = it.getString("CSR")
          tvComments.text = it.getString("Comments")
          tvLocation.text = it.getString("Location")
          tvMeetingAddress.text = it.getString("MeetingAddress")
          tvPincode.text = it.getString("Pincode")
          tvTravellingMode.text = it.getString("TravellingMode")
          tvAmount.text = it.getString("Amount")
          tvGeoMeetingLocation.text = it.getString("GeoMeetingLocation")

          when {
            it.getString("AttendeeEmp", "").isNotEmpty() -> {
              val attendingArray = it.getString("AttendeeEmp")?.trim()?.split(",".toRegex())!!
              tvAttendeeFirst.text = attendingArray[0]
              tvAttendeeSecond.text = attendingArray[1]
              tvAttendeeThird.text = attendingArray[2]
            }
          }

        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }

  }
}