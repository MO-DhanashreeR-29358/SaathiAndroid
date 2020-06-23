package com.mosl.tabapp.dashboard.report.assign

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.dashboardreport.LeadAssignRepository
import com.mosl.mobile.domain.dashboardreport.LeadAssignRequest
import com.mosl.mobile.domain.dashboardreport.STPLeadTransferModel
import com.mosl.mobile.user.login.UserManagerImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class DashboardReportAssignPresenter @Inject constructor(
    view: DashboardReportAssignlView,
    val repository: LeadAssignRepository,
    val userManager: UserManagerImpl
) : BasePresenter<DashboardReportAssignlView>(view) {

    override fun start() {

        Observable.merge(onFetchCalled(), onRetryCalled(), view.onRefreshEvent()).map {
            getLeadAssignStpRequest()
        }
            .switchMap {
                repository.getStpLeadAssignList(it)
                    .compose(applyUiModel())
            }
            .subscribe(view::renderLeadAssignStpList)
            .addTo(disposable)

        view.onBtnEvent().map {
            getSTPLeadTransferRequest("14169")
        }.switchMap {
            repository.getStpLeadTransfer(it)
                .compose(applyUiModel())
        }.subscribe(view::multiUpdateApproval).addTo(disposable)

        view.onSelectedApprovalList().subscribe {
            view.selectedApprovalList(it.selectedArrayList)
        }.addTo(disposable)

    }

    private fun getSTPLeadTransferRequest(empCode: String): STPLeadTransferModel {
        var approvalRequestId = StringBuilder()
        for (approvalId in view.getSelectedAppovalList()) {
            if (approvalRequestId.toString() == "") {
                approvalRequestId = StringBuilder(approvalId)
            } else {
                approvalRequestId.append(",").append(approvalId)
            }
        }
        return STPLeadTransferModel(approvalRequestId.toString(), empCode)
    }

    private fun getLeadAssignStpRequest(): LeadAssignRequest {
        return LeadAssignRequest(
            pageSize = 100,
            pageIndex = 0,
            calculatedPage = 0

        )
    }

    companion object {
        const val NAME = "Name"
        const val PROSPECT_ID = "ProspectId"
        const val REGISTRATION_DATE = "RegistrationDate"
        const val LAST_CONNECTED = "LastConnected"
        const val STAGE = "Stage"
        const val LEAD_STATUS = "Lead Status"
        const val TEAM_LEADER = "TL"
    }

}