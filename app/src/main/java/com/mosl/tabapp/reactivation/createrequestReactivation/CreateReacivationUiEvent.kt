package com.mosl.tabapp.reactivation.createrequestReactivation

import io.reactivex.rxjava3.subjects.PublishSubject

sealed class CreateReacivationUiEvent
{
    object OnCallMinDate:CreateReacivationUiEvent()
    object OnFileUpload:CreateReacivationUiEvent()
    companion object {
        @Volatile private var instance: PublishSubject<CreateReacivationUiEvent>? = null

        /**
         * Use this method to get singleton instance for UI event.
         */
        fun getSubject(): PublishSubject<CreateReacivationUiEvent> {
            if (instance == null) {
                instance = PublishSubject.create()
            }

            return instance as PublishSubject<CreateReacivationUiEvent>
        }
    }
}
