package com.mosl.app.core.data.setappointment

import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.AutoCompleteAttendeeItemResponse
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.setappointment.SetAppointmentRepository
import com.mosl.mobile.domain.setappointment.clientdetail.ClientDetailIResponseData
import com.mosl.mobile.domain.setappointment.lead.LeadViewItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URLEncoder
import javax.inject.Inject

class SetAppointmentRepositoryImpl @Inject constructor(
  private val saathiApi: SaathiApi
) : SetAppointmentRepository {
  override fun getMeetingById(meetingId: String): Observable<MeetingByIdItemResponse> {
    return saathiApi.getMeetingById((MeetingByIdRequest(meetingId)))
      .map {
        it.responseMeetingByIdItemTransform()
      }
  }

  override fun getClientDetail(uCID: String): Observable<ClientDetailIResponseData> {
    return saathiApi.getClientDetail(ClientDetailRequest(uCID.toInt()))
      .map {
        it.responseClientDetailItemTransform()
      }
  }

  override fun getClientImage(id: String): Observable<String> {
    return saathiApi.getClientImage(ClientImageRequest(id))
      .map {
        it.response.toString()
      }
  }

  override fun getLeadViewById(id: String): Observable<LeadViewItemResponse> {
    return saathiApi.getLeadView(LeadViewRequest(id))
      .map {
        it.responseLeadViewItemTransform()
      }
  }

  override fun getAllLookUp(request: LookupRequestInput): Observable<List<LookupItemResponse>> {
    val lookupRequest = request.mapLookupRequestTransform()
    return saathiApi.getAllLookup(lookupRequest)
      .map {
        it.responseLookupItemTransform()
      }
  }

  override fun setAppointmentMeeting(meetingApptModel: String): Observable<SetAppointmentResponseData> {
    var json = JSONObject(meetingApptModel)
    val request = RequestBody.create(MediaType.parse("text/plain"), json.toString())
    return saathiApi.setAppointmentMeeting(request)
      .map {
        it.responseSetAppointmentTransform()
      }
  }

  override fun getAutoCompleteSplAttendee(charSequence: CharSequence): Observable<List<AutoCompleteAttendeeItemResponse>> {
    val request = AutoCompleteAttendeeRequest(
      URLEncoder.encode(charSequence.toString(), "utf-8"),
      URLEncoder.encode(charSequence.toString(), "utf-8")
    )
    return saathiApi.getAutoCompleteSplAttendee(request)
      .map {
        it.responseAutoCompleteAttendeeItemTransform()
      }
  }

  override fun getAutoCompleteAttendee(charSequence: CharSequence): Observable<List<AutoCompleteAttendeeItemResponse>> {
    val request = AutoCompleteAttendeeRequest(
      URLEncoder.encode(charSequence.toString(), "utf-8"),
      URLEncoder.encode(charSequence.toString(), "utf-8")
    )
    return saathiApi.getAutoCompleteAttendee(request)
      .map {
        it.responseAutoCompleteAttendeeItemTransform()
      }
  }

}