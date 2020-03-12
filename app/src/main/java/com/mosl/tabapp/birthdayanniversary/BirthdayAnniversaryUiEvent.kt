package com.mosl.tabapp.birthdayanniversary

import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.birthdayanniversary.SendEmailRequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput

sealed class BirthdayAnniversaryUiEvent {
  data class GetDataList(val request: BirthdayRequestInput) : BirthdayAnniversaryUiEvent()
  data class SendGift(val ucid: String, val eventType: String) : BirthdayAnniversaryUiEvent()
  data class SendEmailAll(val request: SendEmailRequestInput) : BirthdayAnniversaryUiEvent()
  data class GetProfile(val businessId: String, val eventType: String) : BirthdayAnniversaryUiEvent()

}