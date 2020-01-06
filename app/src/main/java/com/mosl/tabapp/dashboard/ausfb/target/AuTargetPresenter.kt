package com.mosl.tabapp.dashboard.ausfb.target

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.autarget.APTargetInput
import com.mosl.mobile.domain.autarget.AuTargetInput
import com.mosl.mobile.domain.autarget.AuTargetRepository
import com.mosl.mobile.domain.autarget.TableItemModel
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.tabapp.constants.MOSLConstants.*
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class AuTargetPresenter @Inject constructor(
    view: AuTargetView, val bankRepository: BankRepository,
    val repository: AuTargetRepository,
    val dashboardModel: DashboardModel,
    val navigator: ScreenNavigatorImpl
) :
    BasePresenter<AuTargetView>(view) {
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
                PieChartClick(it.label)
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
                repository.getAuTargetWidget(it)
                    .compose(applyUiModel())
            }.subscribe(view::renderData)
            .addTo(disposable)
    }

    private fun SummaryCountClick(tableItemModel: TableItemModel) {
        val addtionalParameterMap: MutableMap<String, String> = HashMap()
        tableItemModel.displayText?.let { addtionalParameterMap.put(WIDGETTYPE, it) }
        addtionalParameterMap.putAll(view.getAddtionalParameterMap())
        toNavigate(addtionalParameterMap)
    }

    private fun PieChartClick(label: String?) {
        val addtionalParameterMap: MutableMap<String, String> = HashMap()
        label?.let { addtionalParameterMap.put(KEY, it) }
        addtionalParameterMap.putAll(view.getAddtionalParameterMap())
        toNavigate(addtionalParameterMap)
    }

    fun toNavigate(addtionalParameterMap: MutableMap<String, String> = HashMap()) {
        val args = Bundle()
        args.putString(SYSTEM_NAME, view.getWidgetModel()?.systemName)
        args.putString(LABEL, TEAM_TARGET_TITLE + "-" + view.getWidgetModel()?.systemName)
        args.putSerializable(MAP, addtionalParameterMap as Serializable?)
        navigator.openPage(
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args, true,
            false
        )
    }

    private fun getWidgetRequest(): AuTargetInput {
        return AuTargetInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetModel()?.systemName,
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