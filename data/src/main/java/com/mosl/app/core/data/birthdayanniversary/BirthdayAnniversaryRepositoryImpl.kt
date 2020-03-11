package com.mosl.app.core.data.birthdayanniversary

import com.mosl.app.core.data.setappointment.mapLookupRequestTransform
import com.mosl.app.core.data.setappointment.responseLookupItemTransform
import com.mosl.commons.rx.safeMap
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryItemResponse
import com.mosl.mobile.domain.birthdayanniversary.BirthdayAnniversaryRepository
import com.mosl.mobile.domain.birthdayanniversary.BirthdayRequestInput
import com.mosl.mobile.domain.setappointment.lead.LookupItemResponse
import com.mosl.mobile.domain.setappointment.lead.LookupRequestInput
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.birthdayanniversary.BirthdayAnniversaryRequest
import io.reactivex.Observable
import javax.inject.Inject

class BirthdayAnniversaryRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : BirthdayAnniversaryRepository {
  override fun getAllLookUp(request: LookupRequestInput): Observable<List<LookupItemResponse>> {
    val lookupRequest = request.mapLookupRequestTransform()
    return saathiApi.getAllLookup(lookupRequest)
      .safeMap {
        it.responseLookupItemTransform()
      }
  }

  override fun postBirthdayAnniversary(request: BirthdayRequestInput): Observable<BirthdayAnniversaryItemResponse> {
    val birthdayRequest = request.mapBirthdayRequest()
    return saathiApi.postBirthdayAnniversary(birthdayRequest).map {
      it.mapBirthdayResponseTransform()
    }
  }
}