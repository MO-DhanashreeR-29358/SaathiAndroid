package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class IndirectIFADasboardPresenter @Inject constructor(
    view: IndirectIFADashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFADashboardView>(view) {
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFADashboardFilter().compose(applyUiModel())
        }.subscribe {
            view.fnRenderFilterList(it)
        }.addTo(disposable)


//        onFetchCalled().switchMap {
//            indirectRepo.fnGetIFASalesDashMyBuisnessSnapIP().compose(applyUiModel())
//        }.subscribe {
//            it.onFailure {
//                Timber.d("ResponseError"+it.message)
//            }
//            it.onSuccess {
//                Timber.d("ResponseIFA "+it)
//            }
//
//        }.addTo(disposable)

        view.onLeadCreationClicked()
            .subscribe {
                navigator.openPage(NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_CREATION.toString(), false)
            }
            .addTo(disposable)

        view.onActionableInsightsClicked()
            .subscribe {
                view.showInsightsDialog()
            }
            .addTo(disposable)
    }
}