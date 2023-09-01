package com.mosl.tabapp.reactivation.detail

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class ReactivationDetailUiEvent {
    data class OnEditClick(val refNo: String) : ReactivationDetailUiEvent()
    object OnFeedbackSubmitCalled:ReactivationDetailUiEvent()
    data class OnHierarchyRowClick(val employeeCode: String, val firstReportingTo : String):ReactivationDetailUiEvent()
    object RefreshDetailTable:ReactivationDetailUiEvent()

    companion object {
        @Volatile private var instance: PublishSubject<ReactivationDetailUiEvent>? = null

        /**
         * Use this method to get singleton instance for UI event.
         */
        fun getSubject(): PublishSubject<ReactivationDetailUiEvent> {
            if (instance == null) {
                instance = PublishSubject.create()
            }

            return instance as PublishSubject<ReactivationDetailUiEvent>
        }
    }
}