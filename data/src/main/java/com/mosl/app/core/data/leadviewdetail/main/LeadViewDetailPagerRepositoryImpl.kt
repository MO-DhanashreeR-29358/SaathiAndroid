package com.mosl.app.core.data.leadviewdetail.main

import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.lead.LeadViewRequest
import io.reactivex.Observable
import javax.inject.Inject

class LeadViewDetailPagerRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi
) : LeadViewDetailPagerRepository {

  override fun getLeadDetailForId(id: String): Observable<LeadDetail> {
    val request = LeadViewRequest(id)
    return saathiApi.getLeadView(request)
        .map {
          it.leadViewItemToLeadDetail()
        }
  }
}