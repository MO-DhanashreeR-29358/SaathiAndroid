package com.mosl.tabapp.leadViewDetail.updatemeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import io.reactivex.Observable

interface UpdateMeetingView : BaseView {

  fun renderScreen(data: UpdateMeetingScreenData)

  fun renderMeetingById(uiModel: UiModel<Meeting>)
  fun renderMeetingObjective(uiModel: UiModel<List<Lookup>>)

  fun onSaveButtonClicked(): Observable<Unit>
  fun onResetButtonClicked(): Observable<Unit>
  fun onAttendeeImageButtonClicked(): Observable<Unit>
  fun onSplAttendeeImageButtonClicked(): Observable<Unit>
  fun onAttendeeCountClicked(): Observable<Unit>
  fun onSplAttendeeCountClicked(): Observable<Unit>

  fun renderAttendeeImageButtonClick()
  fun renderSplAttendeeImageButtonClick()
  fun renderAttendeeCountClick()
  fun renderSplAttendeeCountClick()
  fun renderResetButtonClick()
  fun renderSaveButtonClick()

  fun onCategoryTextChanged(): Observable<String>
  fun renderCategoryTextChanged(text: String)

  fun onSubCategoryTextChanged(): Observable<String>
  fun renderSubCategoryTextChanged(text: String)

  fun onMeetingDescriptionTextChanged(): Observable<String>
  fun renderMeetingDescriptionTextChanged(text: String)

  companion object {
    const val ARG_UPDATE_MEETING = "arg_update_meeting"
  }
}