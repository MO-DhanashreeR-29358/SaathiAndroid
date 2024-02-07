package com.mosl.app.core.data.leadviewdetail.main

import com.mosl.mobile.domain.leadviewdetail.main.ClientDetail
import com.mosl.mobile.domain.leadviewdetail.main.ClientDetailAndImage
import com.mosl.mobile.domain.leadviewdetail.main.LeadDetail
import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.clientdetail.ClientDetailRequest
import com.mosl.saathi.api.model.lead.LeadViewRequest
import com.mosl.saathi.api.model.leadviewmain.ClientImageRequest
import com.mosl.saathi.api.model.leadviewmain.RegisterProspectOnlineRequest
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
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

  override fun getClientDetailAndImage(id: String): Observable<ClientDetailAndImage> {
    return Observable.zip(
        getClientDetail(id),
        getClientImage(id),
        BiFunction { detail, image ->
          ClientDetailAndImage(
              clientDetail = detail,
              image = image
          )
        }
    )
  }

  private fun getClientDetail(id: String): Observable<ClientDetail> {
    val request = ClientDetailRequest(id.toInt())
    return saathiApi.getClientDetail(request)
        .map {
          it.toClientDetail()
        }
  }

  private fun getClientImage(id: String): Observable<String> {
    val request = ClientImageRequest(id)
    return saathiApi.getClientImage(request)
        .map {
          it.response.toString()
        }
  }

  override fun getUrlForProspectOnline(
    leadId: String,
    ucId: String
  ): Observable<String> {
    val request = RegisterProspectOnlineRequest(leadId, ucId)
    return saathiApi.getRegisterProspectOnline(request)
        .map {
          it.encryptedURL.toString()
        }
  }
}