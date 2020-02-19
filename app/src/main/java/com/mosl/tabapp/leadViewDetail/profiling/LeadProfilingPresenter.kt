package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class LeadProfilingPresenter @Inject constructor(
  view: LeadProfilingView,
  val repository: LeadViewProfilingRepository,
  val leadId: String
) : BasePresenter<LeadProfilingView>(view) {

  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getProfileQuestionAnswer(leadId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderProfileParameters)
        .addTo(disposable)
  }
}