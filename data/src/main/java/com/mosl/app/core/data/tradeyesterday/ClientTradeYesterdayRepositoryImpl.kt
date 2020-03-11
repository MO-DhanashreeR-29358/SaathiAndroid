package com.mosl.app.core.data.tradeyesterday

import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayInput
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayRepository
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayResponseModel
import com.mosl.saathi.api.SaathiApi
import io.reactivex.Observable
import javax.inject.Inject

class ClientTradeYesterdayRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : ClientTradeYesterdayRepository {
  override fun getClientsTradedYesterday(request: ClientTradeYesterdayInput): Observable<ClientTradeYesterdayResponseModel> {
    val leadRequest = request.requestTransform()
    return api.getClientsTradedYesterday(leadRequest).map {
      it.responseTransform()
    }
  }
}