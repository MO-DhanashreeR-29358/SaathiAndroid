package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class IndirectIFADashboardUIEvent {
    data class onFilterChanged(val strFilterText:String):IndirectIFADashboardUIEvent()

    companion object{
        @Volatile private var instance: PublishSubject<IndirectIFADashboardUIEvent>?=null

        fun getFundUiEvent(): PublishSubject<IndirectIFADashboardUIEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<IndirectIFADashboardUIEvent>
        }
    }
}