package com.mosl.tabapp.notification

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityData
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest
import com.mosl.mobile.domain.leadviewdetail.activities.Appointment
import com.mosl.mobile.domain.notification.NotificationResponseData
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.AppointmentEvent
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.DeleteEvent
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.MeetingEvent
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.RescheduleEvent
import com.mosl.tabapp.leadViewDetail.activity.LeadViewActivitiesUiEvent.UpdateEvent
import com.mosl.tabapp.model.NotificationModel
import io.reactivex.Observable

interface NotificationView : BaseView {

  fun render(uiModel: UiModel<List<NotificationResponseData>>)
}