package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboard.DashboardData
import com.mosl.mobile.domain.dashboard.Incentive
import com.mosl.mobile.domain.dashboard.IncentivePolicy
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.ActionableCount
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.CurrentQuarterStatus
import com.mosl.tabapp.dashboard.base.DashboardUiEvent.PerformanceIncentive
import io.reactivex.Observable

interface DashboardView : BaseView {
  fun render(uiModel: UiModel<List<DashboardData>>)
  fun renderIncentiveData(uiModel: UiModel<Incentive>)
  fun renderIncentivePolicy(uiModel: UiModel<IncentivePolicy>)
  fun renderStatus(uiModel: UiModel<String>)
  fun renderActionableCount(uiModel: UiModel<Pair<com.mosl.mobile.domain.dashboard.ActionableCount, String>>)
  fun onActionableInsightsClicked(): Observable<Unit>
  fun onClientClicked(): Observable<Unit>
  fun onBusinessSnapshotClicked(): Observable<Unit>
  fun onLeadClicked(): Observable<Unit>
  fun onProductivityClicked(): Observable<Unit>
  fun onClientTradedClicked(): Observable<Unit>
  fun onBusinessSnapshotAdvisorClicked(): Observable<Unit>
  fun onLeadCreationClicked(): Observable<Unit>
  fun onQuarterChanged(): Observable<Int>
  fun onFreshRegistrationClicked(): Observable<Unit>
  fun onExistingRegistrationClicked(): Observable<Unit>
  fun onTelephone1Clicked(): Observable<String>
  fun onTelephone2Clicked(): Observable<String>
  fun onEmailClicked(): Observable<String>
  fun onDetailIncentiveClicked(): Observable<Unit>

  fun loadClientsFragment()
  fun loadClientsTradedFragment()
  fun loadLeadsFragment()
  fun loadLeadBusinessSnapshotFragment()
  fun loadProductivityFragment()
  fun loadBusinessSnapshotAdvisorFragment()
  fun loadIncentiveView(value: String)

  fun collapseActionsMenu()
  fun showCallPopup(number: String)
  fun showEmailDialog(email: String)
  fun showInsightsDialog()
  fun Incentive(): Observable<PerformanceIncentive>
  fun CurrentQuarterStatus(): Observable<CurrentQuarterStatus>
  fun ActionableCount(): Observable<ActionableCount>
  fun getSelectedQuarter(): String
  fun getDashboardID(): String
}