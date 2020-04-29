package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
  view: DashboardView,
  val repository: DashboardRepository,
  val dashboardRequestData: DashboardRequestData
) : BasePresenter<DashboardView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getDashboardMetaData(dashboardRequestData)
              .compose(applyUiModel())
        }
        .subscribe(view::render)
        .addTo(disposable)
  }
}