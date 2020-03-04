package com.mosl.app.core.data.trends

import com.mosl.mobile.domain.trends.TrendsInput
import com.mosl.mobile.domain.trends.TrendsRepository
import com.mosl.saathi.api.SaathiApi
import com.mosl.saathi.api.model.dashboard.trends.TrendsTableItemModel
import io.reactivex.Observable
import javax.inject.Inject

class TrendsRepositoryImpl @Inject constructor(
  val api: SaathiApi
) : TrendsRepository {
  override fun getTrendsWidget(request: TrendsInput): Observable<List<TrendsTableItemModel>> {
    val targetRequest = request.requestTransform()
    return api.getTrendsWidgetData(targetRequest).map {

      it.table.responseTransform()

    }
  }
}

