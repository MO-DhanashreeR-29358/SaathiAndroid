package com.mosl.tabapp.dashboard.tradeyesterday

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.tradeyesterday.ClientTradeYesterdayResponseModel
import io.reactivex.rxjava3.core.Observable
import java.util.*

interface ClientsTradedYesterdayView : BaseView {
  fun getGroupName(): String?
  fun getWidgetSystemsName(): String?
  fun getClientList(): MutableList<HashMap<String, String>>
  fun getAdditionalParameterMap(): Map<String, String>

  fun onDataEvent(): Observable<Boolean>
  fun onNavigateEvent(): Observable<MutableMap<String, String>>

  fun renderData(uiModel: UiModel<ClientTradeYesterdayResponseModel>)
  fun renderUI()
}