package com.mosl.tabapp.dashboard.leads

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.bank.BankRepository
import com.mosl.mobile.domain.lead.AdditionalParametersInput
import com.mosl.mobile.domain.lead.LeadInput
import com.mosl.mobile.domain.lead.LeadRepository
import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.addTo
import java.io.Serializable
import java.util.*
import javax.inject.Inject

class LeadsPresenter @Inject constructor(
    view: LeadsView, val bankRepository: BankRepository,
    val leadRepository: LeadRepository,
    val dashboardModel: DashboardModel,
    val navigator: ScreenNavigatorImpl,
    val userManager: UserManager
) :
    BasePresenter<LeadsView>(view) {
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
                bankRepository.getBranchFilter(view.getBank()!!.bankName)
                    .compose(applyUiModel())
            }.subscribe(view::renderBranchFilter).addTo(disposable)

        branchChangeObsc
            .filter(InvalidSelectionPredicate)
            .switchMap {
                bankRepository.getSubBranchFilter(view.getBranch()!!.branchName)
                    .compose(applyUiModel())
            }.subscribe(view::renderSubBranchFilter).addTo(disposable)

        val bankChange = Observable.merge(
            bankChangeObsc,
            branchChangeObsc, view.onSubBranchChange()
        ).share()

        Observable.merge(
            view.onPeriodChange(), view.onProductChange(), view.onSourceChange(),
            bankChange
        ).filter(InvalidSelectionPredicate)
            .map { getWidgetRequest() }
            .switchMap {
                leadRepository.getLeadWidget(it)
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
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD.toString(), args,
            true, false
        )
    }

    private fun getWidgetRequest(): LeadInput {
        return LeadInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = AdditionalParametersInput(
                fltBankName = view.getBank()?.bankName,
                fltBankBranchCode = view.getBranch()?.branchName,
                fltBankSubBrokerCode = view.getSubBranch()?.subBrokerName,
                fltPeriod = view.getPeriod()?.value.toString(),
                fltProduct = view.getProduct()?.value.toString(),
                fltProspect = view.getSource()?.value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

}