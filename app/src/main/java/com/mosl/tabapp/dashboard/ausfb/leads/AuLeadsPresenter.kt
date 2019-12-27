package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AuLeadsPresenter @Inject constructor(view: AuLeadsView, val repository: AusfbLeadRepository) :
    BasePresenter<AuLeadsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            repository.getBankFilter()
        }.compose(applyUiModel())
            .subscribe(view::renderBankFilter).addTo(disposable)
    }

}