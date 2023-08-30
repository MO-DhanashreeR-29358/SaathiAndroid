package com.mosl.tabapp.reactivation.detail

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import timber.log.Timber
import javax.inject.Inject

class ReactivationDetailPresenter @Inject constructor(
    view: ReactivationDetailView,
    val repository: ReactivationRepository,
    val userManager: UserManager
) : BasePresenter<ReactivationDetailView>(view) {

    override fun start() {

        onFetchCalled().
                switchMap {
                    if(userManager.user.isRetailSalesProfile()){
                        repository.getReactivationDetailForSales(view.getPeriodFilter(), view.getCurrentStage())
                            .compose(applyUiModel())
                    }else{
                        repository.getReactivationAssignedHierarchyClientReportList(view.getPeriodFilter())
                            .compose(applyUiModel())
                    }

                }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

        view.fnCallFeedbackSubmit()
            .map{
                view.getFeedbackSubmitData()
            }
            .switchMap {
                    repository.getReactivationFeedback(it) .compose(applyUiModel())
                }
            .subscribe {
                view.renderFeedbackSubmit(it)
            } .addTo(disposable)
    }
}