package com.mosl.mobile.domain.leadviewdetail.profiling

import io.reactivex.rxjava3.core.Observable

interface LeadViewProfilingRepository {
  fun getProfileQuestionAnswer(leadId: String): Observable<List<LeadProfile>>
  fun saveLeadProfileParameter(request: List<ProfileRequestData>): Observable<ProfilingResponseModel>
}