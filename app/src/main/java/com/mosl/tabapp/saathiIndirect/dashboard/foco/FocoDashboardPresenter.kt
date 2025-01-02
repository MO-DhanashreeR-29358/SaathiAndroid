package com.mosl.tabapp.saathiIndirect.dashboard.foco

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.data.onFailure
import com.mosl.commons.data.onSuccess
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.mobile.domain.saathiIndirect.dashboard.DashboardIndirctRepository
import com.mosl.mobile.domain.target.AditionalParametersTarget
import com.mosl.mobile.domain.target.TargetInput
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.*
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.foco.domain.FocoPeriodFilterInput
import com.mosl.tabapp.saathiIndirect.dashboard.foco.domain.FocoRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
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

    private fun getWidgetRequest(): FocoPeriodFilterInput {
        return FocoPeriodFilterInput(
            employeeCode = userManager.getUser()?.employeeCode,

        )
    }

}
