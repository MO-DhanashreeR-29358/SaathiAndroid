package com.mosl.app.core.data.setappointment

import com.mosl.mobile.domain.setappointment.ClientDetailRequestInput
import com.mosl.mobile.domain.setappointment.MeetingByIdInput
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest

fun MeetingByIdInput.mapMeetingByIdRequest(): MeetingByIdRequest {
  return MeetingByIdRequest(
    id = id
  )
}

fun ClientDetailRequestInput.mapClientDetailRequest(): ClientDetailRequest {
  return ClientDetailRequest(
    uCID = uCID
  )
}