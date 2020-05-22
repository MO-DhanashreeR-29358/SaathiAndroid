package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BasePresenter
import com.mosl.tabapp.ekyc.EkycNavigator
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadViewDetailPagerPresenter @Inject constructor(
  view: LeadViewDetailPagerView,
  val data: LeadViewDetailArgument,
  val ekycNavigator: EkycNavigator
) : BasePresenter<LeadViewDetailPagerView>(view) {

  override fun start() {
    val fetchRetryObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .map { data.ucId }

    fetchRetryObs
        .filter { it != "0" }
        .subscribe()
        .addTo(disposable)

    fetchRetryObs
        .filter { it == "0" }
        .subscribe()
        .addTo(disposable)
  }
}