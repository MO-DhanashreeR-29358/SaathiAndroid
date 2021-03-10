package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.ActionableCount
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.CurrentQuarterStatus
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.PerformanceIncentive
import com.mosl.tabapp.ekyc.EkycNavigator
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DashboardPresenter @Inject constructor(
    view: DashboardView,
    val repository: DashboardRepository,
    val navigator: ScreenNavigatorImpl,
    val ekycNavigator: EkycNavigator
) : BasePresenter<DashboardView>(view) {
    override fun start() {
        Observable.merge(onFetchCalled(), onRetryCalled())
            .map { getDashboardRequest() }
            .switchMap {
                repository.getDashboardMetaData(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::render)
            .addTo(disposable)

        view.LoadDashboardData()
            .switchMap {
                repository.getDashboardMetaData(it.dashboardRequestData)
                    .compose(applyUiModel())
            }
            .subscribe(view::render)
            .addTo(disposable)

        view.onClientClicked()
            .subscribe { view.loadClientsFragment() }
            .addTo(disposable)

        view.onBusinessSnapshotClicked()
            .subscribe { view.loadLeadBusinessSnapshotFragment() }
            .addTo(disposable)

        view.onLeadClicked()
            .subscribe { view.loadLeadsFragment() }
            .addTo(disposable)

        view.onProductivityClicked()
            .subscribe { view.loadProductivityFragment() }
            .addTo(disposable)

        view.onBusinessSnapshotAdvisorClicked()
            .subscribe { view.loadBusinessSnapshotAdvisorFragment() }
            .addTo(disposable)

        view.onClientTradedClicked()
            .subscribe { view.loadClientsTradedFragment() }
            .addTo(disposable)

        view.onLeadCreationClicked()
            .subscribe {
                view.collapseActionsMenu()
                navigator.openPage(NavigationConstants.NAVIGATE_TO_LEAD_CREATION.toString(), true)
            }
            .addTo(disposable)

        view.Incentive()
            .switchMap { t: PerformanceIncentive ->
                repository.getIncentive(
                    t.systemName,
                    getDashboardRequest(),
                    t.additionalParameterMap
                )
                    .compose(applyUiModel())
            }
            .subscribe { view.renderIncentiveData(it) }
            .addTo(disposable)

        view.CurrentQuarterStatus()
            .switchMap { t: CurrentQuarterStatus ->
                repository.getCurrentQuarterStatusData(
                    t.systemName, getDashboardRequest(), t.additionalParameterMap
                )
                    .compose(applyUiModel())
            }
            .subscribe { view.renderStatus(it) }
            .addTo(disposable)

        view.ActionableCount()
            .flatMap { t: ActionableCount ->
                repository.getActionableCount(t.systemName, t.additionalParameterMap)
                    .compose(applyUiModel())
            }
            .subscribe {
                view.renderActionableCount(it)
            }
            .addTo(disposable)

        view.onActionableInsightsClicked()
            .subscribe {
                view.showInsightsDialog()
            }
            .addTo(disposable)

        view.onQuarterChanged()
            .filter(InvalidSelectionPredicate)
            .subscribe {
                view.loadIncentiveView(view.getSelectedQuarter())
            }
            .addTo(disposable)

        view.onDetailIncentiveClicked()
            .map { getDashboardRequest() }
            .switchMap {
                repository.getIncentivePolicy(view.getDashboardID(), it)
                    .compose(applyUiModel())
            }
            .subscribe {
                view.renderIncentivePolicy(it)
            }
            .addTo(disposable)

        view.onFreshRegistrationClicked()
            .subscribe {
                view.renderFreshRegistration()
            }
            .addTo(disposable)

        view.onExistingRegistrationClicked()
            .subscribe {
                ekycNavigator.openNewEkycApp(true, "")
            }
            .addTo(disposable)

        view.onTelephone1Clicked()
            .subscribe { view.showCallPopup(it) }
            .addTo(disposable)
        view.onTelephone2Clicked()
            .subscribe { view.showCallPopup(it) }
            .addTo(disposable)
        view.onEmailClicked()
            .subscribe { view.showEmailDialog(it) }
            .addTo(disposable)
    }

    private fun getDashboardRequest(): DashboardRequestData {

        if (view.getBusinessProfileID().isNotEmpty() || view.getEmpCode()
                .isNotEmpty() || view.getProductID().isNotEmpty()
        )
            return DashboardRequestData(
                profileId = view.getBusinessProfileID(), productId = view.getProductID(),
                empCode = view.getEmpCode()
            )

        return DashboardRequestData(null, null, null)
    }
}

sealed class DashboardUiEvent {
    class PerformanceIncentive(
        val systemName: String,
        val additionalParameterMap: Map<String, String>
    ) : DashboardUiEvent()

    class CurrentQuarterStatus(
        val systemName: String,
        val additionalParameterMap: Map<String, String>
    ) : DashboardUiEvent()

    class ActionableCount(
        val systemName: String,
        val additionalParameterMap: Map<String, String>
    ) : DashboardUiEvent()

    class Dashboard(
        val dashboardRequestData: DashboardRequestData
    ) : DashboardUiEvent()
}