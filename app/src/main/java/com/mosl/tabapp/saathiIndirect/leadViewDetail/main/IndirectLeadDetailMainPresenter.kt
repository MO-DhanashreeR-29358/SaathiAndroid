package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.leadviewdetail.main.LeadViewDetailPagerRepository
import com.mosl.tabapp.saathiIndirect.leadViewDetail.domain.IndirectLeadDetailRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectLeadDetailMainPresenter @Inject constructor(
    view: IndirectLeadDetailMainView,
    val repository: IndirectLeadDetailRepository
    //val navigator: LeadViewDetailNavigator
) : BasePresenter<IndirectLeadDetailMainView>(view) {
    override fun start() {
        onFetchCalled()
            .map { view.getLeadId() }
            .switchMap {
                repository.getLeadDetail(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderLeadDetail, Timber::e)
            .addTo(disposable)
    }
}