package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.leaddetail.LeadDetailPageData.LeadDetailArgument
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadDetailPresenter @Inject constructor(
  view: LeadDetailView,
  val data: LeadDetailArgument
) : BasePresenter<LeadDetailView>(view) {
  override fun start() {
    val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .share()

    screenLoadObs
        .subscribe { view.renderArguments(data) }
        .addTo(disposable)

    view.onNestedScrollViewTouched()
        .subscribe { view.renderOnNestedScrollViewTouch() }
        .addTo(disposable)
    view.onRefereeCodeFocusChanged()
        .subscribe { view.renderOnRefereeCodeFocusChange() }
        .addTo(disposable)

  }
}