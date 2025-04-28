package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class IndirectcncfocoDashboardUIEvent {
    data class onFilterChanged(val strFilterText:String): IndirectcncfocoDashboardUIEvent()

    companion object{
        @Volatile private var instance: PublishSubject<IndirectcncfocoDashboardUIEvent>?=null

        fun getFundUiEvent(): PublishSubject<IndirectcncfocoDashboardUIEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<IndirectcncfocoDashboardUIEvent>
        }
    }
}