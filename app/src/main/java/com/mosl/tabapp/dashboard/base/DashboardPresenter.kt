package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.dashboard.DashboardRepository
import com.mosl.mobile.domain.dashboard.DashboardRequestData
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.OnIncentive
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
  val ekycNavigator: EkycNavigator,
  val dashboardRequestData: DashboardRequestData
) : BasePresenter<DashboardView>(view) {
  override fun start() {
    Observable.merge(onFetchCalled(), onRetryCalled())
        .switchMap {
          repository.getDashboardMetaData(dashboardRequestData)
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
          navigator.openPage(NavigationConstants.NAVIGATE_TO_LEAD_CREATION, true)
        }
        .addTo(disposable)

    view.onIncentive()
        .switchMap { t: OnIncentive ->
          repository.getIncentive(t.systemName, dashboardRequestData, t.additionalParameterMap)
              .compose(applyUiModel())
        }
        .subscribe { view.renderIncentiveData(it) }
        .addTo(disposable)

    view.onQuarterChanged()
        .filter(InvalidSelectionPredicate)
        .subscribe {
          view.loadIncentiveView(view.getSelectedQuarter())
        }
        .addTo(disposable)

    view.onFreshRegistrationClicked()
        .subscribe {
          ekycNavigator.openNewEkycApp(false, "")
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
}

sealed class DashboardUiEvent {
  class OnIncentive(
    val systemName: String,
    val additionalParameterMap: Map<String, String>
  ) : DashboardUiEvent()
}