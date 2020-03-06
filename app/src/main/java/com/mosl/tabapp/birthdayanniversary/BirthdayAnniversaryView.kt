package com.mosl.tabapp.birthdayanniversary

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.birthdayanniversary.ProfileSpinner
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import io.reactivex.Observable

interface BirthdayAnniversaryView : BaseView {
  fun getProfileRequestInput(): LookupRequestInput
  fun getEventType(): String
  fun getProfileValue(): ProfileSpinner
  fun onProfileSpinnerSelection(): Observable<Int>
  fun onEventTypeSpinnerSelection(): Observable<Int>
  fun renderProfileSpinner(uiModel: UiModel<List<LookupItemResponse>>)
}