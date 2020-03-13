package com.mosl.tabapp.birthdayanniversary

import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.FilterInput
import com.mosl.mobile.domain.birthdayanniversary.SendEmailRequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput

sealed class BirthdayAnniversaryUiEvent {
  data class GetDataList(val filter: FilterInput, val isAdminMode: Boolean) :
    BirthdayAnniversaryUiEvent()

  data class SendGift(val ucid: String, val eventType: String) : BirthdayAnniversaryUiEvent()
  data class SendEmailAll(
    val selectedUcids: String?,
    val checkAll: Boolean,
    val eventType: String,
    val employeeCode: String?,
    val filterType: String?,
    val messageType: String
  ) : BirthdayAnniversaryUiEvent()

}