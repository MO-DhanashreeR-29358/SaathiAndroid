package com.mosl.tabapp.leaddetail

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import io.reactivex.Observable
import io.reactivex.functions.Function3
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

  override fun getClientDetail(ucId: String): Observable<ClientDetail> {
    return saathiApi.getClientDetail(ClientDetailRequest(ucId.toInt()))
        .map {
          it.clientDetailItemToClientDetail()
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