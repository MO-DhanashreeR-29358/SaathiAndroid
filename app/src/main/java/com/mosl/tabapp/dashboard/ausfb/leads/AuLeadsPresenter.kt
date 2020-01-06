package com.mosl.tabapp.dashboard.ausfb.leads

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.aulead.AdditionalParametersInput
import com.mosl.mobile.domain.aulead.AusfbLeadInput
import com.mosl.mobile.domain.aulead.AusfbLeadRepository
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class AuLeadsPresenter @Inject constructor(
    view: AuLeadsView, val bankRepository: BankRepository,
    val ausfbLeadRepository: AusfbLeadRepository,
    val dashboardModel: DashboardModel,
    val navigator: ScreenNavigatorImpl
) :
    BasePresenter<AuLeadsView>(view) {
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
            .filter(InvalidSelectionPredicate)
            .switchMap {
                bankRepository.getSubBranchFilter(view.getBranch()!!.branchId)
                    .compose(applyUiModel())
            }.subscribe(view::renderSubBranchFilter).addTo(disposable)

        val bankChange = Observable.merge(
            bankChangeObsc,
            branchChangeObsc, view.onSubBranchChange()
        ).share()

        Observable.merge(
            view.onPeriodChange(), view.onProductChange(),
            bankChange
        ).filter(InvalidSelectionPredicate)
            .map { getWidgetRequest() }
            .switchMap {
                ausfbLeadRepository.getAusfbLeadWidget(it)
                    .compose(applyUiModel())
            }.subscribe(view::renderData).addTo(disposable)

        view.onBarChartClick().subscribe { toNavigate(it.lable, it.xAxisValue) }
            .addTo(disposable)
    }

    fun toNavigate(lable: String, xAxisValue: String) {
        val addtionalParameterMap: MutableMap<String, String> = HashMap()
        addtionalParameterMap.put(MOSLConstants.COL_FILTER, xAxisValue)
        addtionalParameterMap.put(MOSLConstants.ROW_FILTER, lable)
        addtionalParameterMap.putAll(view.getAddtionalParameterMap())
        val args = Bundle()
        args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
        args.putString(MOSLConstants.LABEL, MOSLConstants.LEAD)
        args.putSerializable(MOSLConstants.MAP, addtionalParameterMap as Serializable)
        navigator.openPage(
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
            true, false
        )
    }

    private fun getWidgetRequest(): AusfbLeadInput {
        return AusfbLeadInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = AdditionalParametersInput(
                fltBankName = view.getBank()?.bankId.toString(),
                fltBankBranchCode = view.getBranch()?.branchId.toString(),
                fltBankSubBrokerCode = view.getSubBranch()?.bankBranchId.toString(),
                fltPeriod = view.getPeriod()?.value.toString(),
                fltProduct = view.getProduct()?.value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

}