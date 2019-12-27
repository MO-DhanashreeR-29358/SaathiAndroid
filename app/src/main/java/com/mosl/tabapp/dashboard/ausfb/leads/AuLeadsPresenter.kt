package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.ausfbLead.BankRepository
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AuLeadsPresenter @Inject constructor(view: AuLeadsView, val repository: BankRepository) :
    BasePresenter<AuLeadsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            repository.getBankFilter()
                .compose(applyUiModel())
        }
            .subscribe(view::renderBankFilter).addTo(disposable)

        view.onBankChange()
            .filter(InvalidSelectionPredicate)
            .switchMap {
                repository.getBranchFilter(view.getBank()!!.bankId)
                    .compose(applyUiModel())
            }.subscribe(view::renderBranchFilter).addTo(disposable)

        view.onBranchChange()
            .filter(InvalidSelectionPredicate).switchMap {
                repository.getSubBranchFilter(view.getBranch()!!.branchId)
                    .compose(applyUiModel())
            }.subscribe(view::renderSubBranchFilter).addTo(disposable)
    }

}