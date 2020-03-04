package com.mosl.app.core.data.setappointment

import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.setappointment.AutoCompleteAttendeeRequestInput
import com.mosl.mobile.domain.setappointment.LookupRequestInput
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import io.reactivex.Observable
import javax.inject.Inject

class SetAppointmentRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi
) : SetAppointmentRepository {
  override fun getMeetingById(meetingId: String): Observable<MeetingByIdItemResponse> {
    return saathiApi.getMeetingById((MeetingByIdRequest(meetingId))).map {
      it.responseMeetingByIdItemTransform()
    }
  }

  override fun getClientDetail(uCID: String): Observable<ClientDetailIResponseData> {
    return saathiApi.getClientDetail(ClientDetailRequest(uCID.toInt())).map {
      it.responseClientDetailItemTransform()
    }
  }

  override fun getClientImage(id: String): Observable<String> {
    return saathiApi.getClientImage(ClientImageRequest(id)).map {
      it.response
    }
  }

  override fun getLeadViewById(id: String): Observable<LeadViewItemResponse> {
    return saathiApi.getLeadView(LeadViewRequest(id)).map {
      it.responseLeadViewItemTransform()
    }
  }

  override fun getAllLookUp(request: LookupRequestInput): Observable<List<LookupItemResponse>> {
    val lookupRequest = request.mapLookupRequestTransform()
    return saathiApi.getAllLookup(lookupRequest).map {
     it.responseLookupItemTransform()
    }
  }

  override fun setAppointmentMeeting() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAutoCompleteSplAttendee(request: AutoCompleteAttendeeRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getAutoCompleteAttendee(request: AutoCompleteAttendeeRequestInput) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}