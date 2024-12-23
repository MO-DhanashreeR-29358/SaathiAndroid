package com.mosl.app.core.data.saathiIndirect.SetAppointment

import com.mosl.app.core.data.leadviewdetail.createmeeting.ACTIONABLES
import com.mosl.app.core.data.leadviewdetail.createmeeting.ACTIVITYTYPE
import com.mosl.app.core.data.leadviewdetail.createmeeting.BACLIENTMEETING
import com.mosl.app.core.data.leadviewdetail.createmeeting.CATEGORY
import com.mosl.app.core.data.leadviewdetail.createmeeting.CreateMeetingMapper
import com.mosl.app.core.data.leadviewdetail.createmeeting.FEEDBACK
import com.mosl.app.core.data.leadviewdetail.createmeeting.INTERESTEDIN
import com.mosl.app.core.data.leadviewdetail.createmeeting.MEETINGADDRESSID
import com.mosl.app.core.data.leadviewdetail.createmeeting.MEETINGDURATION
import com.mosl.app.core.data.leadviewdetail.createmeeting.MEETINGOBJECTIVE
import com.mosl.app.core.data.leadviewdetail.createmeeting.MEETINGREASON
import com.mosl.app.core.data.leadviewdetail.createmeeting.OUTSTATIONMEETING
import com.mosl.app.core.data.leadviewdetail.createmeeting.SUBCATEGORY
import com.mosl.app.core.data.leadviewdetail.createmeeting.TOUCHEDUNTOUCHED
import com.mosl.app.core.data.leadviewdetail.createmeeting.TRAVELLINGMODEID
import com.mosl.app.core.data.setappointment.responseMeetingByIdItemTransform
import com.mosl.app.core.data.setappointment.responseSetAppointmentTransform
import com.mosl.mobile.domain.addupdatemeeting.SetAppointmentResponseData
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.createmeeting.MeetingByIdItemResponse
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import io.reactivex.rxjava3.core.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.net.URLEncoder
import javax.inject.Inject

class IndirectSetAppointmentRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
):IndirectSetAppointmentRepository {
    override fun getActivitytype(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=13,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(ACTIVITYTYPE))
        }
    }

    override fun getMeetingReason(intParnetId: Int): Observable<List<LookUp>> {
        return  saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=14,parentId=intParnetId.toString(),
                IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(MEETINGREASON))
        }
    }


    override fun getAttendeeData(searchText: String): Observable<List<Attendee>> {
        val request = AutoCompleteAttendeeRequest(
            URLEncoder.encode(searchText, "utf-8"),
            URLEncoder.encode(searchText, "utf-8")
        )
        return saathiIndrectApi.getAutoCompleteAttendee(request)
            .map {  CreateMeetingMapper.transformAttendeeData(it.response?: mutableListOf()) }
    }

    override fun getSplAttendeeData(searchText: String): Observable<List<Attendee>> {
        val request = AutoCompleteAttendeeRequest(
            URLEncoder.encode(searchText, "utf-8"),
            URLEncoder.encode(searchText, "utf-8")
        )
        return saathiIndrectApi.getAutoCompleteSplAttendee(request)
            .map { CreateMeetingMapper.transformAttendeeData(it.response?: mutableListOf()) }
    }


    override fun setAppointmentMeeting(meetingApptModel: String): Observable<SetAppointmentResponseData> {
        var json = JSONObject(meetingApptModel)
        val request = RequestBody.create(MediaType.parse("text/plain"), json.toString())
        return saathiIndrectApi.setAppointmentMeeting(request)
            .map {
                it.responseSetAppointmentTransform()
            }
    }

    override fun getMeetingById(meetingId: String): Observable<MeetingByIdItemResponse> {
        return saathiIndrectApi.getMeetingById((MeetingByIdRequest(meetingId)))
            .map {
                it.response?.responseMeetingByIdItemTransform()!!
            }
    }


    fun getType(type: Int): String {
        return when (type) {
            INTERESTEDIN -> "Select Product"
            MEETINGOBJECTIVE -> "Select Meeting Objective"
            MEETINGDURATION -> "Select Meeting Duration"
            MEETINGADDRESSID -> "Select Meeting Location"
            TRAVELLINGMODEID -> "Select Travelling Mode"
            MEETINGREASON -> "Select Meeting Reason"
            CATEGORY -> "Select Category"
            SUBCATEGORY -> "Select Sub Category"
            ACTIONABLES -> "Select Actionables"
            BACLIENTMEETING -> "Select BA Client Meeting"
            OUTSTATIONMEETING -> "Select Out Station Meeting"
            TOUCHEDUNTOUCHED -> "Select Touched Untouched"
            FEEDBACK -> "Select Feedback"
            else -> "Select Activity Type"
        }
    }
}