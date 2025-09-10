package com.mosl.tabapp.bankmeeting.otp


sealed class OTPEvent {
    data class ButtonClick(@JvmField val sucess: Boolean) :
        OTPEvent()


}