package com.mosl.tabapp.reactivation.detailTL

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.reactivation.ReactivationRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import timber.log.Timber
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ReactivationDetailTLPresenter @Inject constructor(
    view: ReactivationDetailTLView,
    val repository: ReactivationRepository,
    val userManager: UserManager
) : BasePresenter<ReactivationDetailTLView>(view) {

    override fun start() {

        onFetchCalled().switchMap {
            repository.getReactivationDetailForTL(
                view.getEmpCode(),
                view.getFirstReportingTo(),
                view.getPeriodFilter(),
                view.getCurrentStage())
                .compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

    }
}