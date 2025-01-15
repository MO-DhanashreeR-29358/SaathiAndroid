package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAUniqueView :BaseView {

   // fun onFilterEventCalled(): Observable<IndirectIFADashboardUIEvent.onFilterChanged>

  //  fun fnRenderFilterEventCalled(strFilterVal:String)

    fun fnonMeetingEventCalled():Observable<IndirectIFAUniqueUiEvent.onMeetingEventCalled>

    fun fnonRevenuIpEventCalled():Observable<IndirectIFAUniqueUiEvent.onRevenuIpEventCalled>
    fun fnRenderTest()
    fun onWidgetItemClick(): Observable<IndirectIFAUniqueUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun renderData(uiModel: UiModel<FocoDashboardModel>)

    fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>)
    fun getColorsArray(): IntArray
    fun fngetFilterValue():String


    companion object{
     val strTeamUnique="Team Unique"
     val strTargetMetNotMeeting="Target Met Not Met"
     val strMyUnique="My Unique"
        val strProductWise="Product Wise sales"
        val strBranchWise="Branch Wise"
        val strTargetMetNotRevIp="Target Met Not Met"
    }
}