package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.tabapp.birthdayanniversary.BirthdayAnniversaryUiEvent.*
import io.reactivex.rxjava3.core.Observable

interface BirthdayAnniversaryView : BaseView {
  fun getDayType(): String
  fun getFilterType(): String
  fun getEventType(): String
  fun getProfileValue(): ProfileSpinner
  fun onProfileSpinnerSelection(): Observable<Int>
  fun onEventTypeSpinnerSelection(): Observable<Int>
  fun renderProfileSpinner(uiModel: UiModel<List<LookupItemResponse>>)
  fun getDataList(): Observable<GetDataList>
  fun sendGift(): Observable<SendGift>
  fun sendEmailAll(): Observable<SendEmailAll>
  fun onEmailAllClick(): Observable<Unit>
  fun onButtonTodayClick(): Observable<Unit>
  fun onButtonBelatedClick(): Observable<Unit>
  fun onButtonUpcomingClick(): Observable<Unit>
  fun renderBirthdayAnniversary(uiModel: UiModel<BirthdayAnniversaryItemResponse>)
  fun renderGetData()
  fun renderEmailAllClick()
  fun renderSendEmailAll(uiModel: UiModel<String>)
  fun renderSendGift(uiModel: UiModel<String>)
}