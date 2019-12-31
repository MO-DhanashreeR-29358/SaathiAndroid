package com.mosl.tabapp.dashboard.ausfb.leads

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.ausfbLead.AdditionalParametersInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadInput
import com.mosl.mobile.domain.ausfbLead.AusfbLeadRepository
import com.mosl.mobile.domain.ausfbLead.BankRepository
import com.mosl.tabapp.model.dashboard.DashboardModel
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class AuLeadsPresenter @Inject constructor(
    view: AuLeadsView, val bankRepository: BankRepository,
    val ausfbLeadRepository: AusfbLeadRepository,
    val dashboardModel: DashboardModel
) :
    BasePresenter<AuLeadsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            bankRepository.getBankFilter()
                .compose(applyUiModel())
        }
            .subscribe(view::renderBankFilter).addTo(disposable)

        view.onBankChange()
            .filter(InvalidSelectionPredicate)
            .switchMap {
                bankRepository.getBranchFilter(view.getBank()!!.bankId)
                    .compose(applyUiModel())
            }.subscribe(view::renderBranchFilter).addTo(disposable)

        view.onBranchChange()
            .filter(InvalidSelectionPredicate).switchMap {
                bankRepository.getSubBranchFilter(view.getBranch()!!.branchId)
                    .compose(applyUiModel())
            }.subscribe(view::renderSubBranchFilter).addTo(disposable)

        Observable.merge(
            view.onPeriodChange(), view.onProductChange(),
            view.onSubBranchChange()
        ).filter(InvalidSelectionPredicate)
            .map { getWidgetRequest() }
            .switchMap {
                ausfbLeadRepository.getAusfbLeadWidget(it)
                    .compose(applyUiModel())
            }.subscribe(view::renderData).addTo(disposable)
    }


    private fun getWidgetRequest(): AusfbLeadInput {
        return AusfbLeadInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = AdditionalParametersInput(
//                fltBankName = view.getBank()?.bankId.toString(),
//                fltBankBranchCode = view.getBranch()?.branchId.toString(),
//                fltBankSubBrokerCode = view.getSubBranch()?.bankBranchId.toString(),
                fltPeriod = view.getPeriod()?.value.toString(),
                fltProduct = view.getProduct()?.value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

}