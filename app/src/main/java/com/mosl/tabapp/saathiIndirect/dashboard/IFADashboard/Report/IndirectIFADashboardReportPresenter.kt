package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import com.mosl.tabapp.saathiIndirect.dashboard.foco.target.FocoTargetPresenter
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import javax.inject.Inject

class IndirectIFADashboardReportPresenter @Inject constructor(
    view: IndirectIFADashboardReportView,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager,
    val indirectRepo: IndirectIFADashboardRepository
) :BasePresenter<IndirectIFADashboardReportView>(view){

    override fun start() {

        val screenLoadObs = Observable.merge(onFetchCalled(), onRetryCalled())
            .share()

        screenLoadObs
            .filter { view.getWidgetName()  == MOSLConstants.INDIRECT_TODAY_BIRTHDAY }
            .switchMap {
                repository.getActionableTalkTime(getWidgetRequest())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderData, Timber::e)
            .addTo(disposable)


    }

    fun getWidgetRequest() : FocoDashboardInput {
        return  FocoDashboardInput(employeeCode = userManager.getUser()?.employeeCode, frequency = "Last FY", category = "Existing", calculatedPageP = 0,
            pageSizeP = 0, totalCountP = 0, pageP = 0, pageSize = 10, page = 1, calculatedPage = 0, totalCount = 0 )
    }

}