package com.mosl.tabapp.dashboard.clients

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.clients.APClientsInput
import com.mosl.mobile.domain.clients.ClientInput
import com.mosl.mobile.domain.clients.ClientsRepository
import com.mosl.tabapp.model.dashboard.DashboardModel
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ClientsPresenter @Inject constructor(
    view: CliendsView,
    val dashboardModel: DashboardModel,
    val repository: ClientsRepository

) : BasePresenter<CliendsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            repository.getClientsWidget(getWidgetRequest())
                .compose(applyUiModel())
        }
            .subscribe(view::renderData)
            .addTo(disposable)

    }

    private fun getWidgetRequest(): ClientInput {
        return ClientInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = APClientsInput(
                fltPeriod = view.getPeriod()?.value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

}