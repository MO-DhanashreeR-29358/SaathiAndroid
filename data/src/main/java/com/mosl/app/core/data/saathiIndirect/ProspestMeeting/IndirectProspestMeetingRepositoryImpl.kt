package com.mosl.app.core.data.saathiIndirect.ProspestMeeting

import com.mosl.app.core.data.leadviewdetail.createmeeting.ACTIONABLES
import com.mosl.app.core.data.leadviewdetail.createmeeting.ACTIVITYTYPE
import com.mosl.app.core.data.leadviewdetail.createmeeting.BACLIENTMEETING
import com.mosl.app.core.data.leadviewdetail.createmeeting.CATEGORY
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
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.ClientSearchModel
import com.mosl.mobile.domain.saathiIndirect.ProspestMeeting.IndirectProspestMeetingRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiIndirectApi
import com.mosl.saathi.api.model.saathiIndirect.prospestMeeting.ProspestMeetingSearchRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class IndirectProspestMeetingRepositoryImpl @Inject constructor(
    val userManager: UserManagerImpl,
    val saathiIndrectApi: SaathiIndirectApi
):IndirectProspestMeetingRepository {

    override fun fnSearchProspectMeetingClient(strClientCode:String?, strFranchieeCode:String?, strLeadId:String?): Observable<List<ClientSearchModel>> {
        return saathiIndrectApi.fnSearchProspectMeetingClient(ProspestMeetingSearchRequest(
            clientCODE = strClientCode, franchiseeCode = strFranchieeCode, leadId = strLeadId, businessUnitId = userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString()
        )).map {
                ProspestMeetingMapper.transformListData(it.response?: mutableListOf(), getType(
                    ACTIVITYTYPE
                ))
        }
    }

    override fun fnSearchProspectMeetingFRCode(
        strClientCode: String?,
        strFranchieeCode: String?,
        strLeadId: String?
    ): Observable<List<ClientSearchModel>> {
        return saathiIndrectApi.fnSearchProspectMeetingFRCode(ProspestMeetingSearchRequest(
            clientCODE = strClientCode, franchiseeCode = strFranchieeCode, leadId = strLeadId, businessUnitId = userManager.getUser()?.currentBusinessProfile?.businessUnitId.toString()
        )).map {
            ProspestMeetingMapper.transformListData(it.response?: mutableListOf(), getType(
                ACTIVITYTYPE
            ))
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