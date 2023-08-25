package com.mosl.tabapp.reactivation.summary

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import timber.log.Timber
import javax.inject.Inject

class ReactivationSummaryPresenter @Inject constructor(
    view: ReactivationSummaryView,
    val repository: ReactivationRepository,
    val userManager: UserManager
) : BasePresenter<ReactivationSummaryView>(view) {

    override fun start() {

        view.onPeriodChanged()
            .filter(InvalidSelectionPredicate)
            .map { view.getSelectedPeriod() }
            .switchMap {
                repository.getReactivationSummary(it.value)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderOnPeriodChange, Timber::e)
            .addTo(disposable)

    }
}