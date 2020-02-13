package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.activities.LeadViewDetailActivitiesRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadViewActivitiesPresenter @Inject constructor(
  view: LeadViewActivitiesView,
  val repository: LeadViewDetailActivitiesRepository
) : BasePresenter<LeadViewActivitiesView>(view) {

  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .map { view.getActivityRequest() }
        .switchMap {
          repository.getActivityData(it)
              .compose(applyUiModel())
        }
        .subscribe(view::render)
        .addTo(disposable)
  }
}