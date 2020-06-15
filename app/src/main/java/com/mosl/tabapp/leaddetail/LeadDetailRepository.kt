package com.mosl.tabapp.leaddetail

import io.reactivex.Observable

interface LeadDetailRepository {
  fun getLeadDetail(leadId: String): Observable<LeadDetail>
  fun getClientDetailWithPortfolioAndProfitLoss(ucId: String): Observable<ClientDetailWithPortfolioAndProfitLoss>
}