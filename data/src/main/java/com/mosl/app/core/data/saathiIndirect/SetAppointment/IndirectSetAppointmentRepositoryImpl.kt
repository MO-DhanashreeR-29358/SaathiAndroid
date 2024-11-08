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
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.SetAppointment.IndirectSetAppointmentRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.request.LookupRequestTemp
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IndirectSetAppointmentRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
):IndirectSetAppointmentRepository {
    override fun getActivitytype(): Observable<List<LookUp>> {
        return saathiIndrectApi.getAllLookup(
            LookupRequest(businessUnitId="0",lookupTypeId=13,parentId="0",IgnoreBusinessUnit=false)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(ACTIVITYTYPE))
        }
    }

    override fun getMeetingReason(intParnetId: Int): Observable<List<LookUp>> {
        return  saathiIndrectApi.getAllLookupTemp(
            LookupRequestTemp(businessUnitId="0",lookupTypeId=14,parentId=intParnetId.toString(),
                IgnoreBusinessUnit=false,lookupId=0,lookUpName = null, lookupType = 14, sequence = 0, status = 0,
                active = false,sysName = null)
        ).map {
            SetAppointmentMapper.transformLookupData(it.response?: mutableListOf(), getType(ACTIVITYTYPE))
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