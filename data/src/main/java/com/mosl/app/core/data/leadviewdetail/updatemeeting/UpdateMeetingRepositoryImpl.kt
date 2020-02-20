package com.mosl.app.core.data.leadviewdetail.updatemeeting

import com.mosl.mobile.domain.leadviewdetail.updatemeeting.Meeting
import com.mosl.mobile.domain.leadviewdetail.updatemeeting.UpdateMeetingRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.createmeeting.MeetingByIdRequest
import io.reactivex.Observable
import javax.inject.Inject

class UpdateMeetingRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : UpdateMeetingRepository {

  override fun getMeeting(id: String): Observable<Meeting> {
    return saathiApi.postMeetingById(MeetingByIdRequest(id))
        .map {
          it.transform()
        }
  }
}