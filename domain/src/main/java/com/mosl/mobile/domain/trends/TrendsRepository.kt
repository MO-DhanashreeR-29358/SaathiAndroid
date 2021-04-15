package com.mosl.mobile.domain.trends

import io.reactivex.rxjava3.core.Observable

interface TrendsRepository {

  fun getTrendsWidget(request: TrendsInput): Observable<List<TrendsChartModel>>

}