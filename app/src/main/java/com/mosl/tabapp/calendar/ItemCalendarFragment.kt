package com.mosl.tabapp.calendar

import com.mosl.mobile.domain.calendar.LeadActivitiesAppointData
import com.mosl.tabapp.R
import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.databinding.ItemListCalendarNewBinding

import com.xwray.groupie.databinding.BindableItem

class ItemCalendarFragment(
  val data: LeadActivitiesAppointData
) : BindableItem<ItemListCalendarNewBinding>() {
  override fun getLayout(): Int {
    return R.layout.item_list_calendar_new
  }

  override fun bind(viewBinding: ItemListCalendarNewBinding, position: Int) {
    viewBinding.apply {
      tvName.text = data.name
      val dateStr = CRMAppUtil.getDateFromString(
        data.time, MOSLConstants.DATE_FORMAT_TO_SERVER_MEETING,
        "hh:mm a"
      )
      tvTime.text = dateStr
    }

  }

}