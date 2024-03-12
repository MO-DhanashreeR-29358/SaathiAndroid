package com.mosl.tabapp.login

import com.mosl.mobile.user.model.User
import io.reactivex.rxjava3.subjects.PublishSubject

sealed class LoginActivityUiEvent
{

    companion object{
        @Volatile private var instance: PublishSubject<LoginActivityUiEvent>?=null

        fun getLoginUIEvent(): PublishSubject<LoginActivityUiEvent> {
            if(instance==null)
            {
                instance= PublishSubject.create()
            }
            return instance as PublishSubject<LoginActivityUiEvent>
        }
    }
    object OnCallGenerateOtp:LoginActivityUiEvent()
    object OtpEntered:LoginActivityUiEvent()
    object  OnCallLogin:LoginActivityUiEvent()

    object OnCallOtpLogin:LoginActivityUiEvent()

    data class OnOtpSucess(val user: User?):LoginActivityUiEvent()

}

