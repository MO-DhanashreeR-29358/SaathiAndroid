package com.mosl.tabapp.dashboard.clients

import android.os.Bundle
import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.InvalidSelectionPredicate
import com.mosl.mobile.domain.clients.APClientsInput
import com.mosl.mobile.domain.clients.ClientInput
import com.mosl.mobile.domain.clients.ClientsRepository
import com.mosl.tabapp.constants.MOSLConstants
import com.mosl.tabapp.model.dashboard.DashboardModel
import com.mosl.tabapp.navigation.NavigationConstants
import com.mosl.tabapp.navigation.ScreenNavigatorImpl
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable
import javax.inject.Inject

class ClientsPresenter @Inject constructor(
    view: ClientsView,
    val dashboardModel: DashboardModel,
    val repository: ClientsRepository,
    val navigator: ScreenNavigatorImpl

) : BasePresenter<ClientsView>(view) {
    override fun start() {
        onFetchCalled().switchMap {
            repository.getClientsWidget(getWidgetRequest())
                .compose(applyUiModel())
        }
            .subscribe(view::renderData)
            .addTo(disposable)

        val bankChangeObsc = view.onBankChange().skip(1).share()

        bankChangeObsc
            .filter(InvalidSelectionPredicate)
            .switchMap {
                repository.getClientsWidget(getWidgetRequest())
                    .compose(applyUiModel())
            }
            .subscribe(view::renderData)
            .addTo(disposable)

        val dataEvent = Observable.merge(view.onDataEvent(), view.onPeriodChange()).share()

        dataEvent.filter {
            view.getClientList().size > 0
        }.subscribe {
            view.renderUI(view.getPeriod().text, view.getPeriod().value)
        }.addTo(disposable)

        view.onNaviagteEvent().subscribe {
            toNavigate(it)
        }.addTo(disposable)

    }

    private fun getWidgetRequest(): ClientInput {
        return ClientInput(
            businessProfileId = dashboardModel.businessProfileIdForDashboard,
            widgetSystemName = view.getWidgetSystemsName(),
            productId = dashboardModel.productId,
            employeeCode = dashboardModel.employeeCode,
            addtionalParameters = APClientsInput(
                fltBankName = view.getBank()?.text,
                fltPeriod = view.getPeriod().value.toString(),
                fltHierarchy = dashboardModel.dashboardFor
            )
        )
    }

    private fun toNavigate(addtionalParameterMap: MutableMap<String, String>) {
        val args = Bundle()
        args.putString(MOSLConstants.SYSTEM_NAME, view.getWidgetSystemsName())
        args.putString(MOSLConstants.LABEL, CLIENTS)
        args.putSerializable(MOSLConstants.MAP, addtionalParameterMap as Serializable)
        navigator.openPage(
            NavigationConstants.NAVIGATE_TO_SECOND_CLICK_DASHBOARD, args,
            addToStack = true,
            forceReload = false
        )
    }

    companion object {
        const val CLIENTS = "Clients"
        const val CHART_MYCLIENTS_ALLIANCE = "Chart_MyClients_Alliance"
    }
}