package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco.DetailDashboard

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class IndirectCncFocoDashboardDetailsUiEvent {
    data class onFilterChanged(val strFilterText:String): IndirectCncFocoDashboardDetailsUiEvent()

    data class  onFilterCategoryChanged(val strFilterCatText:String): IndirectCncFocoDashboardDetailsUiEvent()


    object  OnDetailsDataCall: IndirectCncFocoDashboardDetailsUiEvent()

    companion object{
        @Volatile private var instance: PublishSubject<IndirectCncFocoDashboardDetailsUiEvent>?=null

        fun getFundUiEvent(): PublishSubject<IndirectCncFocoDashboardDetailsUiEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<IndirectCncFocoDashboardDetailsUiEvent>
        }
    }
}