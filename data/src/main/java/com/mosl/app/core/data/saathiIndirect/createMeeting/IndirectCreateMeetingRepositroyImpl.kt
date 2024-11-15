package com.mosl.app.core.data.saathiIndirect.createMeeting

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
import com.mosl.app.core.data.saathiIndirect.SetAppointment.SetAppointmentMapper
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.createMeeting.IndirectCreateMeetingRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.LookupRequestNew
import io.reactivex.rxjava3.core.Observable
import java.net.URLEncoder
import javax.inject.Inject

class IndirectCreateMeetingRepositroyImpl  @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
): IndirectCreateMeetingRepository {
    override fun getMeetingReason(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=15,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(
                MEETINGDURATION
            ))
        }
    }

    override fun getMeetingObjective(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=18,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(
                MEETINGOBJECTIVE
            ))
        }
    }

    override fun getLocationAddress(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=117,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(MEETINGADDRESSID))
        }
    }

    override fun getTravellingMode(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId=userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString(),lookupTypeId=118,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(TRAVELLINGMODEID))
        }
    }

    override fun getInteresedIn(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId="21",lookupTypeId=183,
                parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(TRAVELLINGMODEID))
        }
    }

    override fun getAttendeeData(searchText: String): Observable<List<Attendee>> {
        val request = AutoCompleteAttendeeRequest(
            URLEncoder.encode(searchText, "utf-8"),
            URLEncoder.encode(searchText, "utf-8")
        )
        return saathiIndrectApi.getAutoCompleteAttendee(request)
            .map { CreateMeetingMapper.transformAttendeeData(it) }
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