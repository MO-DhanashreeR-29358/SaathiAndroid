package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Report

import io.reactivex.rxjava3.subjects.PublishSubject


sealed class IndirectIFADashboardReportUIEvent{

    data class OnReportClick(
        @JvmField val widgetName: String, var strStageCode:String
    ) :IndirectIFADashboardReportUIEvent()

    data class onFilterChanged(val strFilterText:String): IndirectIFADashboardReportUIEvent()

    data class  onDateFilterChanged(val strDateFilterText:String): IndirectIFADashboardReportUIEvent()

    object  OnCallDataEvent: IndirectIFADashboardReportUIEvent()

    object OnCallDateDataEvent: IndirectIFADashboardReportUIEvent()

    companion object{
        @Volatile private var instance: PublishSubject<IndirectIFADashboardReportUIEvent>?=null

        fun getFundUiEvent(): PublishSubject<IndirectIFADashboardReportUIEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<IndirectIFADashboardReportUIEvent>
        }
    }
}
