package com.mosl.tabapp.leadViewDetail.updatemeeting

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import io.reactivex.Observable

interface UpdateMeetingView : BaseView {

  fun renderMeetingById(uiModel: UiModel<Meeting>)
  fun renderMeetingObjective(uiModel: UiModel<List<Lookup>>)

  fun onDateLayoutClicked(): Observable<Unit>
  fun onSaveButtonClicked(): Observable<Unit>
  fun onResetButtonClicked(): Observable<Unit>
  fun onAttendeeImageButtonClicked(): Observable<Unit>
  fun onSplAttendeeImageButtonClicked(): Observable<Unit>
  fun onAttendeeCountClicked(): Observable<Unit>
  fun onSplAttendeeCountClicked(): Observable<Unit>

  companion object {
    const val ARG_UPDATE_MEETING = "arg_update_meeting"
  }
}