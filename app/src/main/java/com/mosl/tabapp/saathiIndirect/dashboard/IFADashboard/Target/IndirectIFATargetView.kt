package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.FilterPeriodModel
import com.mosl.tabapp.model.dashboard.WidgetModel
import com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.IndirectIFADashboardUIEvent
import io.reactivex.rxjava3.core.Observable

interface IndirectIFATargetView:BaseView {

    fun onFilterEventCalled():Observable<IndirectIFADashboardUIEvent.onFilterChanged>

    fun fnRenderFilterEventCalled(strFilterVal:String)

    fun onWidgetItemClick(): Observable<IndirectIFATargetUiEvent.ItemClick>
    fun renderWidgetData(widgetModel: WidgetModel, currentPosition: Int)
    fun renderData(uiModel: UiModel<FocoDashboardModel>)

    fun renderBarChartData(uiModel: UiModel<FocoDashboardModel>)
    fun getColorsArray(): IntArray
    fun fngetFilterValue():String

    companion object{
        val strMeeting ="Meeting"
        val strIFAActivation="IFA Activation"
        val strReveIP="Revenu-IP"
        val strReveBro="Revenu-Broking"
    }
}