package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.lead.LeadViewRequest
import io.reactivex.Observable
import javax.inject.Inject

class LeadDetailRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadDetailRepository {

  override fun getLeadDetail(leadId: String): Observable<LeadDetail> {
    val request = LeadViewRequest(leadId)
    return saathiApi.getLeadView(request)
        .map {
          it.leadViewItemToLeadDetail()
        }
  }
}