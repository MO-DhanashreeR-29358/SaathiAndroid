package com.mosl.app.core.data.leadviewdetail.createmeeting

import com.mosl.app.core.data.leadviewdetail.createmeeting.CreateMeetingMapper.mapToSendOtpResponse
import com.mosl.app.core.data.leadviewdetail.createmeeting.CreateMeetingMapper.transformEventChipsList
import com.mosl.app.core.data.leadviewdetail.createmeeting.CreateMeetingMapper.transformMobileList
import com.mosl.app.core.data.pendingapproval.report.mapToMultiUpdateApprovalresponse
import com.mosl.mobile.domain.createmeeting.*
import com.mosl.mobile.user.helper.getData
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.common.SaathiCommonResponse
import com.mosl.saathi.api.model.createmeeting.AddressByLeadIdRequest
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeRequest
import com.mosl.saathi.api.model.createmeeting.ChipsEventsRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.lead.createmeeting.MobileListRequest
import com.mosl.saathi.api.model.lead.createmeeting.SendOTPRequest
import com.mosl.saathi.api.model.lead.createmeeting.SurveyLinkRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function8
import io.reactivex.rxjava3.functions.Function9
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.net.URLEncoder
import javax.inject.Inject

class CreateMeetingRepositoryImpl @Inject constructor(
    val saathiApi: SaathiApi,
    val userManager: UserManager
) : CreateMeetingRepository {
    override fun getClientDetail(ucid: Int): Observable<ClientDetail> {
        return saathiApi.getClientDetail(ClientDetailRequest(ucid))
            .map { CreateMeetingMapper.transformClientDetail(it) }
    }

    override fun getClientImage(ucid: Int): Observable<String> {
        return saathiApi.getClientImage(ClientImageRequest(ucid.toString()))
            .map { it.getData() }
    }

    override fun getLeadView(leadId: String): Observable<ClientDetail> {
        return saathiApi.getLeadView(LeadViewRequest(leadId))
            .map { CreateMeetingMapper.transformLeadView(it) }
    }

    override fun getLookup(
        type: Int,
        boolean: Boolean?,
        parentId: String?
    ): Observable<List<LookUp>> {
        return if (type.equals(TRAVELLINGMODEID) || type.equals(MEETINGADDRESSID)) {
            saathiApi.getAllLookup(
                LookupRequest("0", type, null, true)
            )
        } else {
            saathiApi.getAllLookup(
                LookupRequest(
                    userManager.getUser()?.currentBusinessProfile?.businessUnitId!!, type, parentId,
                    boolean
                )
            )
        }.map { CreateMeetingMapper.transformLookupData(it, getType(type)) }
    }

    override fun getInitialPageData(createMeetingData: CreateMeetingData): Observable<ClientData> {
        if (createMeetingData.ucid.isNotEmpty() && createMeetingData.ucid != "0") {
            return getClientData(createMeetingData.ucid)
        } else {
            return getLeadData(createMeetingData.leadId)
        }
    }

    fun getClientData(ucid: String): Observable<ClientData> {
        return Observable.zip(
            getClientDetail(ucid.toInt()),
            getClientImage(ucid.toInt()),
            getLookup(ACTIVITYTYPE),
            getMobileList(leadId = "0", ucid = ucid),
            getLookup(MEETINGDURATION),
            getLookup(INTERESTEDIN),
            getLookup(MEETINGOBJECTIVE),
            getLookup(TRAVELLINGMODEID, true),
            getLookup(MEETINGADDRESSID, true),
            Function9 { t1: ClientDetail, t2: String, t3: List<LookUp>, t4: List<MobileListData>,
                        t5: List<LookUp>, t6: List<LookUp>, t7: List<LookUp>, t8: List<LookUp>, t9: List<LookUp> ->
                ClientData(t1, t2, t3, t4, t5, t6, t7, t8, t9)
            })
    }


    fun getLeadData(leadId: String): Observable<ClientData> {
        return Observable.zip(getLeadView(leadId),
            getLookup(ACTIVITYTYPE),
            getMobileList(leadId = leadId, ucid = "0"),
            getLookup(MEETINGDURATION),
            getLookup(INTERESTEDIN),
            getLookup(MEETINGOBJECTIVE),
            getLookup(
                TRAVELLINGMODEID, true
            ),
            getLookup(MEETINGADDRESSID, true),
            Function8 { t1: ClientDetail, t2: List<LookUp>, t3: List<MobileListData>, t4: List<LookUp>, t5: List<LookUp>,
                        t6: List<LookUp>, t7: List<LookUp>, t8: List<LookUp> ->
                ClientData(t1, "", t2, t3, t4, t5, t6, t7, t8)
            })
    }

    override fun getAddress(
        meetingID: String,
        leadId: String
    ): Observable<String> {
        return saathiApi.getAddressByLeadId(AddressByLeadIdRequest(leadId, meetingID))
            .map { it.meetingAddress }
    }

    override fun getMobileList(
        ucid: String,
        leadId: String
    ): Observable<List<MobileListData>> {
        return saathiApi.getMobileList(MobileListRequest(leadId, ucid))
            .map { transformMobileList(it) }
    }

    override fun getChipsList(): Observable<List<String>> {
        return saathiApi.getChipsList(ChipsEventsRequest(userManager.getUser()!!.employeeCode))
            .map { transformEventChipsList(it) }
    }

    override fun getCustomerServiceData(): Observable<Pair<List<LookUp>, List<LookUp>>> {
        return Observable.zip(
            getLookup(CATEGORY), getLookup(ACTIONABLES),
            BiFunction { t1: List<LookUp>, t2: List<LookUp> ->
                Pair(t1, t2)
            })
    }

    override fun getFranchiseIFAUserData(): Observable<Pair<List<LookUp>, List<LookUp>>> {
        return Observable.zip(
            getLookup(OUTSTATIONMEETING), getLookup(BACLIENTMEETING),
            BiFunction { t1: List<LookUp>, t2: List<LookUp> ->
                Pair(t1, t2)
            }
        )
    }

    override fun getAttendeeData(searchText: String): Observable<List<Attendee>> {
        val request = AutoCompleteAttendeeRequest(
            URLEncoder.encode(searchText, "utf-8"),
            URLEncoder.encode(searchText, "utf-8")
        )
        return saathiApi.getAutoCompleteAttendee(request)
            .map { CreateMeetingMapper.transformAttendeeData(it) }
    }

    override fun getSplAttendeeData(searchText: String): Observable<List<Attendee>> {
        val request = AutoCompleteAttendeeRequest(
            URLEncoder.encode(searchText, "utf-8"),
            URLEncoder.encode(searchText, "utf-8")
        )
        return saathiApi.getAutoCompleteSplAttendee(request)
            .map { CreateMeetingMapper.transformAttendeeData(it) }
    }

    override fun sendOTP(data: OTPRequestData): Observable<SendOtpResponse> {
        val request = SendOTPRequest(
            employeeCode = userManager.getUser()!!.employeeCode,
            latitude = data.latitude,
            leadId = data.leadid,
            longitude = data.longitude,
            geoArea = data.geoArea,
            ucid = data.ucid,
            mobileNumber = data.mobileNumber

        )
        return saathiApi.getStartMeeting(request)
            .map {
                mapToSendOtpResponse(it.status, it.getMessage())
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

    override fun saveRequest(data: String): Observable<AddUpdateMeeting> {
        val request = RequestBody.create("text/plain".toMediaTypeOrNull(), data)
        return saathiApi.postAddUpdateMeeting(request)
            .map {
                CreateMeetingMapper.transformAddUpdateMeeting(it)
            }
    }
}

const val INTERESTEDIN = 5
const val MEETINGOBJECTIVE = 18
const val MEETINGDURATION = 15
const val MEETINGADDRESSID = 117
const val TRAVELLINGMODEID = 118
const val ACTIVITYTYPE = 13
const val MEETINGREASON = 14
const val CATEGORY = 21
const val SUBCATEGORY = 22
const val ACTIONABLES = 20
const val OUTSTATIONMEETING = 17
const val BACLIENTMEETING = 16
const val TOUCHEDUNTOUCHED = 132
const val FEEDBACK = 133
