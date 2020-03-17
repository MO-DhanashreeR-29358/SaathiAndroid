package com.mosl.tabapp.calendar.calendarappoinslist.item

import com.mosl.mobile.domain.calendar.CalenderItemResponse
import com.mosl.mobile.domain.calendar.LeadActivitiesAppointData
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.ItemClientMeetingListNewBinding
import com.xwray.groupie.databinding.BindableItem

class ItemCalendarAppointList(
  val data: CalenderItemResponse
) : BindableItem<ItemClientMeetingListNewBinding>() {
  override fun getLayout(): Int {
    return R.layout.item_client_meeting_list_new
  }

  override fun bind(viewBinding: ItemClientMeetingListNewBinding, position: Int) {
    viewBinding.apply {
      tvClientCode.text = data.leadId
      tvName.text = data.name
      val dateStr = CRMAppUtil.getDateFromString(
        data.meetingDate, MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING,
        "dd-MMM-yyyy"
      )
      tvCompleteDate.text = dateStr
      val timeStr = CRMAppUtil.getDateFromString(
        data.meetingDate, MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING,
        "hh:mm a"
      )
      tvTime.text = timeStr
    }

  }

}