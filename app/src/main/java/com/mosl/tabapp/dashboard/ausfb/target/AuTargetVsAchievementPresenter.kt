package com.mosl.tabapp.dashboard.ausfb.target

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.autarget.APTargetInput
import com.mosl.mobile.domain.autarget.AuTargetInput
import com.mosl.mobile.domain.autarget.AuTargetRepository
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.tabapp.model.dashboard.DashboardModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AuTargetVsAchievementPresenter @Inject constructor(
    view: AuTargetVsAchievementView, val bankRepository: BankRepository,
    val repository: AuTargetRepository,
    val dashboardModel: DashboardModel
) :
    BasePresenter<AuTargetVsAchievementView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            bankRepository.getBankFilter()
                .compose(applyUiModel())
        }
            .subscribe(view::renderBankFilter).addTo(disposable)

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

        val widgetItemClickedObsc = view.onWidgetItemClicked().share()

        widgetItemClickedObsc
            .subscribe {
                view.renderWidgetData(it.widgetItemInfo, it.currentPosition)
            }.addTo(disposable)

        val bankChange = Observable.merge(
            bankChangeObsc,
            branchChangeObsc, view.onSubBranchChange()
        ).share()

        Observable.merge(
            bankChange,
            view.onPeriodChange(),
            widgetItemClickedObsc
        ).filter { view.getWidgetModel() != null }
            .map { getWidgetRequest() }
            .switchMap {
                repository.getAuTargetWidget(it)
                    .compose(applyUiModel())
            }.subscribe(view::renderData)
            .addTo(disposable)
    }


    private fun getWidgetRequest(): AuTargetInput {
        return AuTargetInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = APTargetInput(
                fltBankName = view.getBank()?.bankId.toString(),
                fltBankBranchCode = view.getBranch()?.branchId.toString(),
                fltBankSubBrokerCode = view.getSubBranch()?.bankBranchId.toString(),
                fltPeriod = view.getPeriod()?.value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

}