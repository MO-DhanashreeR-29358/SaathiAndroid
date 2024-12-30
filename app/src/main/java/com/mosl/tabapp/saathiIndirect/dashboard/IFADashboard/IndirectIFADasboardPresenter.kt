package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.mobile.domain.saathiIndirect.dashboard.DashboardIndirctRepository
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADasboardPresenter @Inject constructor(
    view: IndirectIFADashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
):BasePresenter<IndirectIFADashboardView>(view) {
    override fun start() {

        onFetchCalled().switchMap {
            indirectRepo.fnGetIFASalesDashboBuisnessSnapshot().compose(applyUiModel())
        }.subscribe {
            it.onFailure {
                Timber.d("Response Error"+it.message)
            }
            it.onSuccess {
                Timber.d("Response "+it)
            }

        }.addTo(disposable)

        view.onLeadCreationClicked()
            .subscribe {
                navigator.openPage(NavigationConstants.NAVIGATE_TO_INDIRECT_LEAD_CREATION.toString(), false)
            }
            .addTo(disposable)
    }
}