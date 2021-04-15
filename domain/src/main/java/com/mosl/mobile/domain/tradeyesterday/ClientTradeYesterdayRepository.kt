package com.mosl.mobile.domain.tradeyesterday

import io.reactivex.rxjava3.core.Observable

interface ClientTradeYesterdayRepository {
  fun getClientsTradedYesterday(request: ClientTradeYesterdayInput): Observable<ClientTradeYesterdayResponseModel>
}