package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.UniqueReport

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard.IndirectIFADashboardDetailsUiEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectIFAUniqueView :BaseView {

    fun onFilterEventCalled(): Observable<IndirectIFADashboardDetailsUiEvent.onFilterChanged>
    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onFilterCategoryEventCalled():Observable<IndirectIFADashboardDetailsUiEvent.onFilterCategoryChanged>

    fun fnRenderFilterCategoryEventCalled(strFilterCatVal:String)

    fun fnonMeetingEventCalled():Observable<IndirectIFAUniqueUiEvent.onMeetingEventCalled>

    fun fnonIfaActivationEventCalled() :Observable<IndirectIFAUniqueUiEvent.onIfaActivationEventCalled>

    fun fnonRevenuIpEventCalled():Observable<IndirectIFAUniqueUiEvent.onRevenuIpEventCalled>

    fun fnonRevenuBroEventCalled():Observable<IndirectIFAUniqueUiEvent.onRevenuBroEventCalled>
    fun fnRenderTest()
    fun onWidgetItemClick(): Observable<IndirectIFAUniqueUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun renderData(uiModel: UiModel<FocoDashboardModel>)

    fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>)
    fun getColorsArray(): IntArray
    fun fngetFilterValue():String
    fun fngetFilterCatValue():String


    companion object{
     val strTeamUnique="Team Unique"
     val strTargetMetNotMeeting="Target Met Not Met"
     val strMyUnique="My Unique"

        val strProductWise="Product Wise sales"
        val strBranchWise="Branch Wise"
        val strTargetMetNotRevIp="Target Met Not Met"

        val strIfaProductWise="Product Wise sales"
        val strIfaBranchWise="Branch Wise"

        val strRevBroProductWise="Product Wise sales"
        val strRevBroBranchWise="Branch Wise"
        val strRevBroTargetMetNotRevIp="Target Met Not Met"
    }
}