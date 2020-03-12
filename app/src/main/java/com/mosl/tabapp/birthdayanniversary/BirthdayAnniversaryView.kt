package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.GetDataList
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendEmailAll
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.SendGift
import io.reactivex.Observable

interface BirthdayAnniversaryView : BaseView {
  fun getProfileRequestInput(): LookupRequestInput
  fun getEventType(): String
  fun getProfileValue(): ProfileSpinner
  fun onProfileSpinnerSelection(): Observable<Int>
  fun onEventTypeSpinnerSelection(): Observable<Int>
  fun renderProfileSpinner(uiModel: UiModel<List<LookupItemResponse>>)
  fun getDataList(): Observable<GetDataList>
  fun renderBirthdayAnniversary(uiModel: UiModel<BirthdayAnniversaryItemResponse>)
  fun renderGetData()
  fun onEmailAllClick(): Observable<Unit>
  fun renderEmailAllClick()
  fun sendEmailAll(): Observable<SendEmailAll>
  fun sendGift(): Observable<SendGift>
  fun renderSendEmailAll(uiModel: UiModel<String>)
  fun renderSendGift(uiModel: UiModel<String>)
}