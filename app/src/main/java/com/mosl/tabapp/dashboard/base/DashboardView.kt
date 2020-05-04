package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboard.DashboardData
import io.reactivex.Observable

interface DashboardView : BaseView {
  fun render(uiModel: UiModel<List<DashboardData>>)
  fun onClientClicked(): Observable<Unit>
  fun onBusinessSnapshotClicked(): Observable<Unit>
  fun onLeadClicked(): Observable<Unit>
  fun onProductivityClicked(): Observable<Unit>
  fun onClientTradedClicked(): Observable<Unit>
  fun onBusinessSnapshotAdvisorClicked(): Observable<Unit>
  fun onLeadCreationClicked(): Observable<Unit>
  fun onFreshRegistrationClicked(): Observable<Unit>
  fun onExistingRegistrationClicked(): Observable<Unit>
  fun onTelephone1Clicked(): Observable<Unit>
  fun onTelephone2Clicked(): Observable<Unit>
  fun onEmailClicked(): Observable<Unit>

  fun loadClientsFragment()
  fun loadClientsTradedFragment()
  fun loadLeadsFragment()
  fun loadLeadBusinessSnapshotFragment()
  fun loadProductivityFragment()
  fun loadBusinessSnapshotAdvisorFragment()

  fun collapseActionsMenu()
  fun showCallPopup()
  fun showEmailDialog()
}