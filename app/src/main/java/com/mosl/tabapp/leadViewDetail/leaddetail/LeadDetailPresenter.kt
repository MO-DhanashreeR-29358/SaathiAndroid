package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.app.core.base.BasePresenter
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadDetailPresenter @Inject constructor(
  view: LeadDetailView
) : BasePresenter<LeadDetailView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .subscribe()
        .addTo(disposable)
  }
}