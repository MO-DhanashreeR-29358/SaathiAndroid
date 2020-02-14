package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.activities.LeadViewDetailActivitiesRepository
import com.mosl.tabapp.leadViewDetail.LeadViewActivitiesNavigator
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class LeadViewActivitiesPresenter @Inject constructor(
  view: LeadViewActivitiesView,
  val repository: LeadViewDetailActivitiesRepository,
  val navigator: LeadViewActivitiesNavigator
) : BasePresenter<LeadViewActivitiesView>(view) {

  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .map { view.getActivityRequest() }
        .switchMap {
          repository.getActivityData(it)
              .compose(applyUiModel())
        }
        .subscribe(view::render, Timber::d)
        .addTo(disposable)

    view.onMeetingEventFired()
        .map { it.data }
        .subscribe {
          navigator.openLeadViewActivitiesDetailScreen(it)
        }
        .addTo(disposable)
  }
}