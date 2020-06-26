package com.mosl.tabapp.leaddetail

import io.reactivex.Observable

interface LeadDetailRepository {
  fun getLeadDetail(leadId: String): Observable<LeadDetail>
  fun getClientDetail(ucId: String): Observable<ClientDetail>
  fun getLeadEmpForProduct(
    search: String,
    primaryRm: String
  ): Observable<List<LeadEmployees>>

  fun getLeadEmpForAssign(search: String): Observable<List<LeadEmployees>>
  fun getClientDetailWithPortfolioAndProfitLoss(ucId: String): Observable<ClientDetailWithPortfolioAndProfitLoss>
}