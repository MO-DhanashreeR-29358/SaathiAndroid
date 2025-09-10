package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLeadDetials

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import javax.inject.Inject

class IndirectIFAMyLeadDetailsPresenter @Inject constructor(
    view: IndirectIFAMyLeadDetailsView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
): BasePresenter<IndirectIFAMyLeadDetailsView>(view) {
    override fun start() {


        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)

        view.onDataRenderCalled().map {
            view.fnGetFilterData()
        }.switchMap {
            indirectRepo.fngetIFASalesDashMyLeadsDetail(it).compose(applyUiModel())
        } .subscribe{view.renderData(it)}
            .addTo(disposable)



    }
}