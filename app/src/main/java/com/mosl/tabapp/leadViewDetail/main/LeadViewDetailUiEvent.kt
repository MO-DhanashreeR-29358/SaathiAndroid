package com.mosl.tabapp.leadViewDetail.main

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class LeadViewDetailUiEvent {
    data class OaoFormClick(val oaoId: Int) : LeadViewDetailUiEvent()
    data class CreateMeetingClick(val id: Int) : LeadViewDetailUiEvent()
    object SetAppointmentClick : LeadViewDetailUiEvent()
    object OpenAccountClick : LeadViewDetailUiEvent()
    object ShareLinkClick : LeadViewDetailUiEvent()
    object SuggestMeClick : LeadViewDetailUiEvent()
    object CopyLinkClick : LeadViewDetailUiEvent()
    object CallClick : LeadViewDetailUiEvent()
    object SMSClick : LeadViewDetailUiEvent()
    object RetryClick : LeadViewDetailUiEvent()
    data class UpdateCreateMeetingId(val id: Int) : LeadViewDetailUiEvent()

    companion object {
        @Volatile private var instance: PublishSubject<LeadViewDetailUiEvent>? = null

        /**
         * Use this method to get singleton instance for UI event.
         */
        fun getSubject(): PublishSubject<LeadViewDetailUiEvent> {
            if (instance == null) {
                instance = PublishSubject.create()
            }

            return instance as PublishSubject<LeadViewDetailUiEvent>
        }
    }
}