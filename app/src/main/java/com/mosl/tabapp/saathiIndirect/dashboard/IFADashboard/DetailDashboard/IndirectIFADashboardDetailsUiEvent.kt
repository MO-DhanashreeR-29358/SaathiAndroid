package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.DetailDashboard

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class IndirectIFADashboardDetailsUiEvent {
    data class onFilterChanged(val strFilterText:String): IndirectIFADashboardDetailsUiEvent()

    object  OnDetailsDataCall:IndirectIFADashboardDetailsUiEvent()

    companion object{
        @Volatile private var instance: PublishSubject<IndirectIFADashboardDetailsUiEvent>?=null

        fun getFundUiEvent(): PublishSubject<IndirectIFADashboardDetailsUiEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<IndirectIFADashboardDetailsUiEvent>
        }
    }
}