package com.mosl.mobile.domain.leadviewdetail.profiling

import io.reactivex.Observable

interface LeadViewProfilingRepository {
  fun getProfileQuestionAnswer(leadId: String)
  fun saveLeadProfileParameter(request: List<ProfileRequestData>): Observable<String>
}