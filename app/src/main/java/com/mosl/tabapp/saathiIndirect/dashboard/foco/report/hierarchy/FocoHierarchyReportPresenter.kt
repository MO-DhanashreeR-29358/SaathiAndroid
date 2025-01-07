package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import android.annotation.SuppressLint
import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.IP_SALES
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.NO_OF_CLIENT_MEETINGS
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter.Companion.REVENUE
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

class FocoHierarchyReportPresenter @Inject constructor(
    view: FocoHierarchyReportView,
    private val repository: FocoRepository,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) : BasePresenter<FocoHierarchyReportView>(view) {
  @SuppressLint("WrongConstant")
  override fun start() {
      val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
          .share()


      screenLoadObs
          .filter { view.getWidgetName()  == REVENUE}
          .switchMap {
              repository.getRevenueHierarchy(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == NO_OF_CLIENT_MEETINGS}
          .switchMap {
              repository.getClientMeetingHierarchy(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      screenLoadObs
          .filter { view.getWidgetName()  == IP_SALES}
          .switchMap {
              repository.getIpSalesHierarchy(getWidgetRequest())
                  .compose(applyUiModel())
          }
          .subscribe(view::renderData, Timber::e)
          .addTo(disposable)

      val reportClickObs = view.onReportClickEvent().share()

      reportClickObs
          .filter { it.widgetName  == REVENUE }
          .subscribe {
              val args = Bundle()
              args.putString(MOSLConstants.SYSTEM_NAME, it.widgetName)
              navigator.openPage(
                  NavigationConstants.NAVIGATE_TO_INDIRECT_FOCO_DASHBOARD_REPORT.toString(), args, true,
                  false
              )
          }.addTo(disposable)

      reportClickObs
          .filter { it.widgetName  == NO_OF_CLIENT_MEETINGS }
          .subscribe {
              val args = Bundle()
              args.putString(MOSLConstants.SYSTEM_NAME, it.widgetName)
              navigator.openPage(
                  NavigationConstants.NAVIGATE_TO_INDIRECT_FOCO_DASHBOARD_REPORT.toString(), args, true,
                  false
              )
          }.addTo(disposable)

      reportClickObs
          .filter { it.widgetName  == IP_SALES }
          .subscribe {
              val args = Bundle()
              args.putString(MOSLConstants.SYSTEM_NAME, it.widgetName)
              navigator.openPage(
                  NavigationConstants.NAVIGATE_TO_INDIRECT_FOCO_DASHBOARD_REPORT.toString(), args, true,
                  false
              )
          }.addTo(disposable)

  }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", category = "Existing", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }

}