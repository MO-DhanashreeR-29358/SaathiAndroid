package com.mosl.mobile.domain.trends

import io.reactivex.Observable

interface TrendsRepository {

  fun getTrendsWidget(request: TrendsInput): Observable<List<TrendsChartModel>>

}