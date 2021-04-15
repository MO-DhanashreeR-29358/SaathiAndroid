package com.mosl.app.core.data.leadviewdetail.profiling

import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfilingResponseModel
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.leadprofile.LeadProfileRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LeadViewProfilingRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadViewProfilingRepository {

  override fun getProfileQuestionAnswer(leadId: String): Observable<List<LeadProfile>> {
    return saathiApi.getLeadProfile(LeadProfileRequest(leadId))
        .map {
          it.transform()
        }
  }

  override fun saveLeadProfileParameter(request: List<ProfileRequestData>): Observable<ProfilingResponseModel> {
    val saveLeadProfileRequest = request.mapToSaveLeadProfileRequestList()
    return saathiApi.postLeadProfile(saveLeadProfileRequest).map {
      it.responseTransform()
    }
  }
}