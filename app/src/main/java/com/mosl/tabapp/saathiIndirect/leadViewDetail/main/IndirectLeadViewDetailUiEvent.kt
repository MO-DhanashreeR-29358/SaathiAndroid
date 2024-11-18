package com.mosl.tabapp.saathiIndirect.leadViewDetail.main

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class IndirectLeadViewDetailUiEvent {
    data class CreateMeetingClick(val id: Int) : IndirectLeadViewDetailUiEvent()
    object SetAppointmentClick : IndirectLeadViewDetailUiEvent()
    object CallClick : IndirectLeadViewDetailUiEvent()
    object SMSClick : IndirectLeadViewDetailUiEvent()
    object RetryClick : IndirectLeadViewDetailUiEvent()
    data class UpdateCreateMeetingId(val id: Int) : IndirectLeadViewDetailUiEvent()

    companion object {
        @Volatile private var instance: PublishSubject<IndirectLeadViewDetailUiEvent>? = null

        /**
         * Use this method to get singleton instance for UI event.
         */
        fun getSubject(): PublishSubject<IndirectLeadViewDetailUiEvent> {
            if (instance == null) {
                instance = PublishSubject.create()
            }

            return instance as PublishSubject<IndirectLeadViewDetailUiEvent>
        }
    }
}