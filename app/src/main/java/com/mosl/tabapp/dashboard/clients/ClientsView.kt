package com.mosl.tabapp.dashboard.clients

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.clients.ClientItemResponseModel
import com.mosl.tabapp.common.FilterRowData
import io.reactivex.Observable
import java.util.*

interface ClientsView : BaseView {
    fun onPeriodChange(): Observable<Int>
    fun onDataEvent(): Observable<Boolean>
    fun getPeriod(): FilterRowData
    fun getWidgetSystemsName(): String?
    fun getAddtionalParameterMap(): Map<String, String>
    fun renderData(uiModel: UiModel<ClientItemResponseModel>)
    fun getClientList(): MutableList<HashMap<String, String>>
    fun getFamilyCount(): Int
    fun renderUI(text: String, value: String)
    fun onNaviagteEvent(): Observable<MutableMap<String, String>>
    fun onBankChange(): Observable<Int>
    fun getBank(): FilterRowData?
}