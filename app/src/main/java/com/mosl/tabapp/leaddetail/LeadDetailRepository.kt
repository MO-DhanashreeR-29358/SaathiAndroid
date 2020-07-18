package com.mosl.tabapp.leaddetail

import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
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
  fun addProduct(request: InsertLeadProductRequest): Observable<List<AddProductData>>

}