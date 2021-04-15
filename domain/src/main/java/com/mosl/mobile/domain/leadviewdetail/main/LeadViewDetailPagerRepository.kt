package com.mosl.mobile.domain.leadviewdetail.main

import io.reactivex.rxjava3.core.Observable

interface LeadViewDetailPagerRepository {
  fun getLeadDetailForId(id: String): Observable<LeadDetail>
  fun getClientDetailAndImage(id: String): Observable<ClientDetailAndImage>
  fun getUrlForProspectOnline(
    leadId: String,
    ucId: String
  ): Observable<String>
}