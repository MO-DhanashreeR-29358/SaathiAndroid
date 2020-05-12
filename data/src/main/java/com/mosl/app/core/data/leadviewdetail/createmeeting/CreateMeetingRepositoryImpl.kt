package com.mosl.app.core.data.leadviewdetail.createmeeting

import com.mosl.mobile.domain.createmeeting.ClientData
import com.mosl.mobile.domain.createmeeting.ClientDetail
import com.mosl.mobile.domain.createmeeting.CreateMeetingData
import com.mosl.mobile.domain.createmeeting.CreateMeetingRepository
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.user.helper.getData
import com.mosl.mobile.user.login.UserManager
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import io.reactivex.Observable
import io.reactivex.functions.Function7
import io.reactivex.functions.Function8
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
    boolean: Boolean?
  ): Observable<List<LookUp>> {
    return saathiApi.getAllLookup(
        LookupRequest(
            userManager.getUser()?.currentBusinessProfile?.businessUnitId!!, type, null, boolean
        )
    )
        .map { CreateMeetingMapper.transformLookupData(it, getType(type)) }
  }

  override fun getInitialPageData(createMeetingData: CreateMeetingData): Observable<ClientData> {
    if (!createMeetingData.ucid.equals("0")) {
      return getClientData(createMeetingData.ucid)
    } else {
      return getLeadData(createMeetingData.leadId)
    }
  }

  fun getClientData(ucid: String): Observable<ClientData> {
    return Observable.zip(
        getClientDetail(ucid.toInt()), getClientImage(ucid.toInt()), getLookup(ACTIVITYTYPE),
        getLookup(MEETINGDURATION), getLookup(INTERESTEDIN), getLookup(MEETINGOBJECTIVE),
        getLookup(TRAVELLINGMODEID, true), getLookup(MEETINGADDRESSID, true),
        Function8 { t1: ClientDetail, t2: String, t3: List<LookUp>, t4: List<LookUp>,
          t5: List<LookUp>, t6: List<LookUp>, t7: List<LookUp>, t8: List<LookUp> ->
          ClientData(t1, t2, t3, t4, t5, t6, t7, t8)
        })
  }

  fun getLeadData(leadId: String): Observable<ClientData> {
    return Observable.zip(getLeadView(leadId), getLookup(ACTIVITYTYPE), getLookup(MEETINGDURATION),
        getLookup(INTERESTEDIN), getLookup(MEETINGOBJECTIVE), getLookup(
        TRAVELLINGMODEID, true
    ), getLookup(MEETINGADDRESSID, true),
        Function7 { t1: ClientDetail, t2: List<LookUp>, t3: List<LookUp>, t4: List<LookUp>, t5: List<LookUp>,
          t6: List<LookUp>, t7: List<LookUp> ->
          ClientData(t1, "", t2, t3, t4, t5, t6, t7)
        })
  }

  fun getType(type: Int): String {
    return when (type) {
      INTERESTEDIN -> "Select Product"
      MEETINGOBJECTIVE -> "Select Meeting Objective"
      MEETINGDURATION -> "Select Meeting Duration"
      MEETINGADDRESSID -> "Select Meeting Location"
      TRAVELLINGMODEID -> "Select Travelling Mode"
      else -> "Select Activity Type"
    }
  }
}

const val INTERESTEDIN = 5
const val MEETINGOBJECTIVE = 18
const val MEETINGDURATION = 15
const val MEETINGADDRESSID = 117
const val TRAVELLINGMODEID = 118
const val ACTIVITYTYPE = 13
