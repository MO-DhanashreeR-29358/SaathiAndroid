package com.mosl.tabapp.leadViewDetail.history

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.history.MappingHistoryRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class MappingHistoryPresenter @Inject constructor(
  view: MappingHistoryView,
  val repository: MappingHistoryRepository
) : BasePresenter<MappingHistoryView>(view) {
  override fun start() {

    Observable.merge(onFetchCalled(), onRetryCalled(), view.onEventTypeChange()).switchMap {
        repository.getDashboardData(view.getLeadId(), view.getUcId(), view.getEventType())
          .compose(applyUiModel())
      }
      .subscribe(view::renderData)
      .addTo(disposable)

  }

}