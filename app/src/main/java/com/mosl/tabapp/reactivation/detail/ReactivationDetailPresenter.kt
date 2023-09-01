package com.mosl.tabapp.reactivation.detail

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ReactivationDetailPresenter @Inject constructor(
    view: ReactivationDetailView,
    val navigator: ScreenNavigatorImpl,
    val repository: ReactivationRepository,
    val userManager: UserManager
) : BasePresenter<ReactivationDetailView>(view) {

    override fun start() {

        Observable.merge(onFetchCalled(), view.onRefreshDetailTable())
            .switchMap {
                    if(userManager.user.isRetailSalesProfile()){
                        repository.getReactivationDetailForSales(view.getPeriodFilter(), view.getCurrentStage())
                            .compose(applyUiModel())
                    }else{
                        repository.getReactivationAssignedHierarchyClientReportList(view.getPeriodFilter(), view.getCurrentStage())
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

        view.onHierarchyRowClick()
            .subscribe {
                val args = Bundle()
                var navigationConstants = NavigationConstants.NAVIGATE_TO_REACTIVATION_DETAIL_TL
                args.putString(MOSLConstants.REACTIVATION_EMP_CODE, it.employeeCode)
                args.putString(MOSLConstants.REACTIVATION_FIRST_REPORTING_TO, it.firstReportingTo)
                args.putString(MOSLConstants.REACTIVATION_TYPE, view.getCurrentStage())
                args.putString(MOSLConstants.REACTIVATION_PERIOD, view.getPeriodFilter())
                navigator.openPage(
                    navigationConstants.toString(),
                    args,
                    addToStack = true,
                    forceReload = false
                )
            } .addTo(disposable)
    }
}