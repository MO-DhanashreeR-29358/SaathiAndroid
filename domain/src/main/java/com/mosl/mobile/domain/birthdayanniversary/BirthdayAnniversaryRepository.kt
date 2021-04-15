package com.mosl.mobile.domain.birthdayanniversary

import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import io.reactivex.rxjava3.core.Observable

interface BirthdayAnniversaryRepository {
  fun getAllLookUp(request: LookupRequestInput): Observable<List<LookupItemResponse>>
  fun postBirthdayAnniversary(request: BirthdayRequestInput): Observable<BirthdayAnniversaryItemResponse>
  fun postSendEmail(request: SendEmailRequestInput): Observable<String>
  fun postSendGift(ucid: String, eventType: String): Observable<String>
}