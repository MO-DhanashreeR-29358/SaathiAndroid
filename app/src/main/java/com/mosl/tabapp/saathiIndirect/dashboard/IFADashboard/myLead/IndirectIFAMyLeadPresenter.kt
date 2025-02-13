package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import timber.log.Timber
import javax.inject.Inject

class IndirectIFAMyLeadPresenter @Inject constructor(
    view: IndirectIFAMyLeadView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFAMyLeadView>(view) {
    override fun start() {
        view.onDataRenderCalled().map {
            view.fngetFilterValue()
        }.switchMap {
                indirectRepo.fnGetIFaSalesDashMyLead(it).compose(applyUiModel())
        } .subscribe(view::renderData, Timber::e)
            .addTo(disposable)

//        view.onFilterEventCalled().subscribe {
//            view.fnRenderFilterEventCalled(it.strFilterText)
//        }.addTo(disposable)

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)


        view.onTableRowClicked().subscribe {
            val bundle = Bundle()
            bundle.putString(
                MOSLConstants.SEL_CATEGORY,
                it.strCategory
            )
            bundle.putString(
                MOSLConstants.SEL_FILTER,
                it.strFilter
            )
            navigator.openPage(
                NavigationConstants.NAVIGATE_TO_INDIRECT_IFA_MYLEAD_DETAILS.toString(), bundle,
                addToStack = true,
                forceReload = false
            )
        }.addTo(disposable)

    }
}