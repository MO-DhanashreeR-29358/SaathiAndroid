package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityData
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.*
import io.reactivex.rxjava3.core.Observable

interface LeadViewActivitiesView : BaseView {

  fun getActivityRequest(): ActivityRequest
  fun render(uiModel: UiModel<ActivityData>)

  fun onAppointmentEventFired(): Observable<AppointmentEvent>
  fun onMeetingEventFired(): Observable<MeetingEvent>
  fun onRescheduleEventFired(): Observable<RescheduleEvent>
  fun onUpdateEventFired(): Observable<UpdateEvent>
  fun onDeleteEventFired(): Observable<DeleteEvent>
  fun renderAppointmentClicked(data: Appointment)
  fun renderDeleteAppointment(
    uiModel: UiModel<String>,
    meetingId: String
  )
}