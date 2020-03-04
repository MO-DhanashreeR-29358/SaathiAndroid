package com.mosl.mobile.domain.trends

import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItemModel
import io.reactivex.Observable

interface TrendsRepository {

  fun getTrendsWidget(request: TrendsInput): Observable<List<TrendsTableItemModel>>

}