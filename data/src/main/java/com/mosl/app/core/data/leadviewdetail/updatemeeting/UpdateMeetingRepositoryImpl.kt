package com.mosl.app.core.data.leadviewdetail.updatemeeting

import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Lookup
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.UpdateMeetingRepository
import com.mosl.mobile.user.login.UserManagerImpl
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import com.mosl.saathi.api.model.lead.LookupRequest
import io.reactivex.Observable
import javax.inject.Inject

class UpdateMeetingRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi,
  val userManager: UserManagerImpl
) : UpdateMeetingRepository {

  override fun getMeeting(id: String): Observable<Meeting> {
    return saathiApi.postMeetingById(MeetingByIdRequest(id))
        .map {
          it.transform()
        }
  }

  override fun getMeetingObjective(): Observable<List<Lookup>> {
    val request = LookupRequest(
        businessUnitId = userManager.getUser()?.currentBusinessProfile?.businessUnitId ?: "",
        lookupTypeId = MEETING_OBJECTIVE,
        parentId = null,
        IgnoreBusinessUnit = null
    )

    return saathiApi.getAllLookup(request)
        .map {
          it.mapToLookupList()
        }
  }

  companion object {
    const val MEETING_OBJECTIVE = 18
  }
}