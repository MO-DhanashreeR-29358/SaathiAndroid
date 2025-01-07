package com.mosl.tabapp.saathiIndirect.dashboard.foco

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodFilterInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class FocoDashboardPresenter @Inject constructor(
    view: FocoDashboardView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: FocoRepository
) : BasePresenter<FocoDashboardView>(view) {
    override fun start() {
            onFetchCalled().switchMap{
                indirectRepo.getPeriodFilter(getWidgetRequest()).compose(applyUiModel())
            }.subscribe {
               view.renderPeriodFilter(it)
            }.addTo(disposable)

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

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", category = "Existing", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }

}
