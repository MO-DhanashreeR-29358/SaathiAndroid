package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.tabapp.ekyc.EkycNavigator
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class LeadViewDetailPagerPresenter @Inject constructor(
  view: LeadViewDetailPagerView,
  val data: LeadViewDetailArgument,
  val repository: LeadViewDetailPagerRepository,
  val ekycNavigator: EkycNavigator,
  val navigator: LeadViewDetailNavigator
) : BasePresenter<LeadViewDetailPagerView>(view) {

  override fun start() {
    val fetchRetryObs = Observable.merge(onFetchCalled(), onRetryCalled())
        .map { data.ucId }

    fetchRetryObs
        .filter { it != "0" }
        .switchMap {
          repository.getClientDetailAndImage(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderClientDetailAndImage, Timber::e)
        .addTo(disposable)

    fetchRetryObs
        .filter { it == "0" }
        .map { data.leadId }
        .switchMap {
          repository.getLeadDetailForId(it)
              .compose(applyUiModel())
        }
        .subscribe(view::renderLeadDetail, Timber::e)
        .addTo(disposable)

    view.onThreeVerticalDotsClicked()
        .subscribe { view.showLeadViewDialog() }
        .addTo(disposable)

    view.onShareLinkClicked()
        .subscribe { view.renderOnShareLinkClick(data.leadId) }
        .addTo(disposable)

    view.onSuggestMeClicked()
        .map { data }
        .switchMap {
          repository.getUrlForProspectOnline(data.leadId, data.ucId)
              .compose(applyUiModel())
        }
        .subscribe { uiModel ->
          view.renderOnSuggestMeClick(uiModel)
          uiModel.onSuccess {
            navigator.navigateToSuggestMe(it)
          }
        }
        .addTo(disposable)
  }
}