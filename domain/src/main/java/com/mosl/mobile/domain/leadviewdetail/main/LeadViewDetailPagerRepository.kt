package com.mosl.mobile.domain.leadviewdetail.main

import io.reactivex.Observable

interface LeadViewDetailPagerRepository {
  fun getLeadDetailForId(id: String): Observable<LeadDetail>
  fun getClientDetailAndImage(id: String): Observable<ClientDetailAndImage>
}