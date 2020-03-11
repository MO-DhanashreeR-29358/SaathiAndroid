package com.mosl.mobile.domain.tradeyesterday

import io.reactivex.Observable

interface ClientTradeYesterdayRepository {
  fun getClientsTradedYesterday(request: ClientTradeYesterdayInput): Observable<ClientTradeYesterdayResponseModel>
}