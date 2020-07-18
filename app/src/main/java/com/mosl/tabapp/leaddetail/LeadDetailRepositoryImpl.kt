package com.mosl.tabapp.leaddetail

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.assignto.UserBusinessProfileRequest
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.productspecialist.ProductSpecialistRequest
import io.reactivex.Observable
import io.reactivex.functions.Function3
import java.net.URLEncoder
import javax.inject.Inject

class LeadDetailRepositoryImpl @Inject constructor(
  val saathiApi: SaathiApi,
  val userManager: UserManager
) : LeadDetailRepository {

  override fun getLeadDetail(leadId: String): Observable<LeadDetail> {
    val request = LeadViewRequest(leadId)
    return saathiApi.getLeadView(request)
        .map {
          it.leadViewItemToLeadDetail()
        }
  }

  override fun getClientDetailWithPortfolioAndProfitLoss(ucId: String): Observable<ClientDetailWithPortfolioAndProfitLoss> {
    return Observable.zip(
        getClientDetail(ucId),
        getClientPortfolio(ucId),
        getClientProfitLoss(ucId),
        Function3 { detail: ClientDetail, portfolio: ClientPortfolio, profitLoss: ClientProfitLoss ->
          ClientDetailWithPortfolioAndProfitLoss(
              detail = detail,
              portfolio = portfolio,
              profitLoss = profitLoss
          )
        }
    )
  }

  override fun addProduct(request: InsertLeadProductRequest): Observable<List<AddProductData>> {
    return saathiApi.insertLeadProductMapping(request)
        .map {
          it.leadProductMappingItemsToAddProductDatas()
        }
  }

  override fun getClientDetail(ucId: String): Observable<ClientDetail> {
    return saathiApi.getClientDetail(ClientDetailRequest(ucId.toInt()))
        .map {
          it.clientDetailItemToClientDetail()
        }
  }

  override fun getLeadEmpForProduct(
    search: String,
    primaryRm: String
  ): Observable<List<LeadEmployees>> {
    val request = ProductSpecialistRequest(
        employeeCode = URLEncoder.encode(search, "utf-8"),
        employeeName = URLEncoder.encode(search, "utf-8"),
        primaryRM = primaryRm
    )
    return saathiApi.getProductSpecialist(request)
        .map {
          it.productItemsToLeadEmployees()
        }
  }

  override fun getLeadEmpForAssign(search: String): Observable<List<LeadEmployees>> {
    val request = UserBusinessProfileRequest(
        employeeCode = URLEncoder.encode(search, "utf-8"),
        employeeName = URLEncoder.encode(search, "utf-8")
    )
    return saathiApi.getUserBusinessProfile(request)
        .map {
          it.profilesToLeadEmployees()
        }
  }

  private fun getClientPortfolio(ucId: String): Observable<ClientPortfolio> {

    return saathiApi.getClientSummary(
        url = "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/GetClientSummary",
        userId = userManager.user?.userId,
        ucid = ucId
    )
        .map {
          it.summaryResponseToClientPortfolio()
        }
  }

  private fun getClientProfitLoss(ucId: String): Observable<ClientProfitLoss> {
    return saathiApi.getClientGainLossSummary(
        url = "https://login.motilaloswal.com/Partner/PartnerMobileWebServices.asmx/GetClientGainLossSummary",
        userId = userManager.user.userId,
        ucid = ucId
    )
        .map {
          it.gainLossSummaryToClientProfitLoss()
        }
  }
}