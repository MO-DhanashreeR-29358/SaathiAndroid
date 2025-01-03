package com.mosl.tabapp.saathiIndirect.dashboard.foco.report

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoRepository
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

class FocoDashboardReportPresenter @Inject constructor(
    view: FocoDashboardReportView,
    private val repository: FocoRepository,
    val data: FocoDashboardReportData,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) : BasePresenter<FocoDashboardReportView>(view) {
  override fun start() {
      val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
          .share()

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
  }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", category = "Existing", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }


  companion object {
    const val LABEL = "Label"
    const val WidgetSystemName = "WidgetSystemName"
    const val ADDITIONALPARAMETERS = "AdditionalParameters"
    const val PageSize = "PageSize"
    const val Page = "Page"
    const val FltLevel = "flt_level"
    const val Flg_Hierarchy = "flg_Hierarchy"
    const val FlgEmpcode = "flg_empcode"
      const val FltHierarchyEmpCode = "flt_Hierarchy_empCode"
      const val FLT_HIERARCHY_BRANCH_CODE = "flt_Hierarchy_Branch_Code"
      const val DASHBOARD_REPORT_PAGE_SIZE = "100"
    const val PAGE = "1"
    const val WIDGETNAME = "Widget name"
    const val SYSTEM_NAME = "system_name"
    const val MAP = "map"
    const val FILTER_LIST = "filter_list"
    const val SPINNER_LIST = "spinner_list"
    const val FLT_DATE = "flt_date"
    const val FLT_PERIOD = "flt_period"
    const val COL_FILTER = "colfilter"
    const val ROW_FILTER = "rowfilter"
    const val ACTIONABLE = "Actionable"
    const val WIDGETTYPE = "WidgetType"

  }
}