package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadViewProfilingRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import javax.inject.Named

class LeadProfilingPresenter @Inject constructor(
  view: LeadProfilingView,
  val repository: LeadViewProfilingRepository,
  @Named("leadId") val leadId: String
) : BasePresenter<LeadProfilingView>(view) {

  override fun start() {

    val fetchObs = onFetchCalled().share()

    fetchObs.subscribe { view.render(leadId) }
        .addTo(disposable)

    Observable.merge(fetchObs, onRetryCalled())
        .switchMap {
          repository.getProfileQuestionAnswer(leadId)
              .compose(applyUiModel())
        }
        .subscribe(view::renderProfileParameters)
        .addTo(disposable)


    view.onSubmitButtonClicked()
        .subscribe { view.renderSubmitButtonClicked() }
        .addTo(disposable)

    view.onCancelButtonClicked()
        .subscribe { view.renderCancelButtonClicked() }
        .addTo(disposable)

    view.onSubmitProfileEventFired()
        .map {
          view.getProfileRequestDataList()
        }
        .switchMap {
          repository.saveLeadProfileParameter(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderSubmitProfile)
        .addTo(disposable)
  }
}