package com.mosl.tabapp.dashboard.teamtarget

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.teamtarget.AditionalParametersTeamTarget
import com.mosl.mobile.domain.teamtarget.TeamTableItem3Model
import com.mosl.mobile.domain.teamtarget.TeamTargetInput
import com.mosl.mobile.domain.teamtarget.TeamTargetRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class TeamTargetPresenter @Inject constructor(
  view: TeamTargetView, val bankRepository: BankRepository,
  val repository: TeamTargetRepository,
  val dashboardModel: DashboardModel,
  val navigator: ScreenNavigatorImpl,
  val userManager: UserManager
) :
  BasePresenter<TeamTargetView>(view) {
  override fun start() {
    if (userManager.user.isAuBankSalesSupport()) {
      onFetchCalled().switchMap {
        bankRepository.getBankFilter()
          .compose(applyUiModel())
      }
        .subscribe(view::renderBankFilter).addTo(disposable)
    }
    val bankChangeObsc = view.onBankChange().share()
    val branchChangeObsc = view.onBranchChange().share()

    bankChangeObsc
      .filter(InvalidSelectionPredicate)
      .switchMap {
        bankRepository.getBranchFilter(view.getBank()!!.bankId)
          .compose(applyUiModel())
      }.subscribe(view::renderBranchFilter).addTo(disposable)

    branchChangeObsc
      .filter(InvalidSelectionPredicate).switchMap {
        bankRepository.getSubBranchFilter(view.getBranch()!!.branchId)
          .compose(applyUiModel())
      }.subscribe(view::renderSubBranchFilter).addTo(disposable)

    val widgetItemClickObsc = view.onWidgetItemClick().share()

    widgetItemClickObsc
      .subscribe {
        view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
      }.addTo(disposable)

    view.onSummaryCountClick()
      .subscribe {
        SummaryCountClick(it.tabelItemInfo)
      }.addTo(disposable)

    view.onPieChartClick()
      .subscribe {
        PieChartClick(it.label, it.widgetType)
      }.addTo(disposable)

    val bankChange = Observable.merge(
      bankChangeObsc,
      branchChangeObsc, view.onSubBranchChange()
    ).share()

    Observable.merge(
      bankChange,
      view.onPeriodChange(),
      widgetItemClickObsc
    ).filter { view.getWidgetModel() != null }
      .map { getWidgetRequest() }
      .switchMap {
        repository.getTeamTargetWidget(it)
          .compose(applyUiModel())
      }.subscribe(view::renderData)
      .addTo(disposable)
  }

  private fun SummaryCountClick(tableItemModel: TeamTableItem3Model) {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()
    tableItemModel.displayText?.let { addtionalParameterMap.put(WIDGETTYPE, it) }
    addtionalParameterMap.putAll(view.getAddtionalParameterMap())
    toNavigate(addtionalParameterMap)
  }

  private fun PieChartClick(label: String?, widgetType: String?) {
    val addtionalParameterMap: MutableMap<String, String> = HashMap()
    label?.let { addtionalParameterMap.put(KEY, it) }
    widgetType?.let { addtionalParameterMap.put(WIDGETTYPE, it) }
    addtionalParameterMap.putAll(view.getAddtionalParameterMap())
    toNavigate(addtionalParameterMap)
  }

  fun toNavigate(addtionalParameterMap: MutableMap<String, String> = HashMap()) {
    val args = Bundle()
    args.putString(SYSTEM_NAME, view.getWidgetModel()?.systemName)
    args.putString(LABEL, TEAM_TARGET_TITLE + "-" + view.getWidgetModel()?.name)
    args.putSerializable(MAP, addtionalParameterMap as Serializable?)
    navigator.openPage(
        NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD.toString(), args, true,
        false
    )
  }

  private fun getWidgetRequest(): TeamTargetInput {
    return TeamTargetInput(
      businessProfileId = dashboardModel.businessProfileIdForDashboard,
      widgetSystemName = view.getWidgetModel()?.systemName,
      productId = dashboardModel.productId,
      employeeCode = dashboardModel.employeeCode,
      addtionalParameters = AditionalParametersTeamTarget(
        fltBankName = view.getBank()?.bankId.toString(),
        fltBankBranchCode = view.getBranch()?.branchId.toString(),
        fltBankSubBrokerCode = view.getSubBranch()?.bankBranchId.toString(),
        fltPeriod = view.getPeriod()?.value.toString(),
        fltHierarchy = dashboardModel.dashboardFor
      )
    )
  }

  companion object {
    const val WIDGETTYPE = "WidgetType"
    const val TEAM_TARGET_TITLE = "Target Vs Achievement"
  }

}