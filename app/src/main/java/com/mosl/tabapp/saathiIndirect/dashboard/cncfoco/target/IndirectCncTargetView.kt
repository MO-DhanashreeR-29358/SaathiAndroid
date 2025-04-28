package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.IndirectcncfocoDashboardUIEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectCncTargetView:BaseView {

    fun onFilterEventCalled(): Observable<IndirectcncfocoDashboardUIEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onWidgetItemClick(): Observable<IndirectCncfocoTargetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun renderData(uiModel: UiModel<FocoDashboardModel>)

    fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>)
    fun getColorsArray(): IntArray
    fun fngetFilterValue():String

    companion object{
        val strReve="Revenue"
        val strNoOfClient="No. of Client Meetings"

    }
}