package com.mosl.app.core.data.leadviewdetail.profiling

import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class LeadViewProfilingRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadViewProfilingRepository {
  override fun getProfileQuestionAnswer(leadId: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveLeadProfileParameter(request: List<ProfileRequestData>): Observable<String> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}