package com.mosl.tabapp.reactivation.summary

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class ReactivationSummaryPresenter @Inject constructor(
    view: ReactivationSummaryView,
    val repository: ReactivationRepository,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) : BasePresenter<ReactivationSummaryView>(view) {

    override fun start() {

        onFetchCalled()
            .switchMap {
                repository.getReactivationSourceList()
                    .compose(applyUiModel())
            }.subscribe(view::renderSourceList, Timber::e)
            .addTo(disposable)

        view.onSearchClick()
            .switchMap {
                if(userManager.user.isRetailSalesProfile()){
                    repository.getReactivationSummaryForSales(view.getSelectedPeriod().value, view.getSelectedSource())
                        .compose(applyUiModel())
                }else{
                    repository.getReactivationSummaryForTL(view.getSelectedPeriod().value, view.getSelectedSource())
                        .compose(applyUiModel())
                }

            }
            .subscribe(view::renderSearchClick, Timber::e)
            .addTo(disposable)

        val onButtonClick = Observable.merge(
            view.onAssignedClick(),
            view.onContactedClick(),
            view.onNonContactedClick(),
            view.onReactivatedClientslick()
        )

        Observable.merge(onButtonClick, view.onTradedClick(), view.onRevenueClick())
            .subscribe {
                val args = Bundle()
                var navigationConstants = NavigationConstants.NAVIGATE_TO_REACTIVATION_DETAIL
                args.putString(MOSLConstants.REACTIVATION_TYPE, view.getCurrentStage())
                args.putString(MOSLConstants.REACTIVATION_PERIOD, view.getSelectedPeriod().value)
                args.putString(MOSLConstants.REACTIVATION_SOURCE, view.getSelectedSource())
                navigator.openPage(
                    navigationConstants.toString(),
                    args,
                    addToStack = true,
                    forceReload = false
                )
            }

    }
}