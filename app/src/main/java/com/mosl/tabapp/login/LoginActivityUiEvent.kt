package com.mosl.tabapp.login

sealed class LoginActivityUiEvent
{

    object OnCallGenerateOtp:LoginActivityUiEvent()
    object OtpEntered:LoginActivityUiEvent()

}

