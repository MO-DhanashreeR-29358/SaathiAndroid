package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import android.annotation.SuppressLint
import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.commons.rx.addTo
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.CLIENT_REACH
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.IP_SALES
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.NO_OF_CLIENT_MEETINGS
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.ONLINE_REVENUE
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.REVENUE
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.REVENUE_GROWTH_DEGROWTH
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.TOTAL_MOB_APP
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

class FocoDashboardReportPresenter @Inject constructor(
    view: FocoDashboardReportView,
    private val repository: FocoRepository,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) : BasePresenter<FocoDashboardReportView>(view) {
  @SuppressLint("WrongConstant")
  override fun start() {
      onFetchCalled().switchMap{
          repository.getPeriodFilter(getWidgetRequest()).compose(applyUiModel())
      }.subscribe {
          view.renderPeriodFilter(it)
      }.addTo(disposable)

      val periodChangedObs = view.onPeriodChange()
          .filter(InvalidSelectionPredicate)
          .share()

      val clientReactChangedObs = view.onClientReactivationFilterChange()
          .filter(InvalidSelectionPredicate)
          .share()

      val userTypeChangedObs = view.onUserTypeChange()
          .filter(InvalidSelectionPredicate)
          .share()

      val screenLoadObs = Observable.merge(
          periodChangedObs,
          clientReactChangedObs,
          userTypeChangedObs
      )

      screenLoadObs
          .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TALK_TIME }
          .switchMap {
              repository.getActionableTalkTime(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TOP_INCENTIVE }
          .switchMap {
              repository.getActionableTopIncentive(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_CLIENT_REACTIVATION }
          .switchMap {
              repository.getActionableClientReactivation(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TOP_CLIENT_LIST }
          .switchMap {
              repository.getActionableTopClientList(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_RUNRATE_FOR_ADVISOR }
          .switchMap {
              repository.getFocoDashRunrateForAdvisorWidgetData(getWidgetRequestForRunrate())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == REVENUE }
          .switchMap {
              repository.getRevenueDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == NO_OF_CLIENT_MEETINGS }
          .switchMap {
              repository.getClientMeetingDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == ONLINE_REVENUE }
          .switchMap {
              repository.getOnlineRevenueDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == CLIENT_REACH }
          .switchMap {
              repository.getClientReachDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == IP_SALES }
          .switchMap {
              repository.getIpSalesDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == TOTAL_MOB_APP }
          .switchMap {
              repository.getTotalMobAppDetail(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

  }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = view.getEmployeeCode(), frequency = view.getPeriod()?.displayText, category = view.getClientReactivationFilter(), fltUserStatus = view.getUserType(), filter = "", calculatedPageP = 0,
            pageSizeP = 20,  pageP = 1, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }

    fun getWidgetRequestForRunrate() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = view.getEmployeeCode(), frequency = "", category = "", fltUserStatus = view.getUserType(), filter = "", calculatedPageP = 0,
            pageSizeP = 20,  pageP = 1, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }
}