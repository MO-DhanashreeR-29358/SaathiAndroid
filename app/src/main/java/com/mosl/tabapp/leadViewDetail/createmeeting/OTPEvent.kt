package com.mosl.tabapp.leadViewDetail.createmeeting


sealed class OTPEvent {
    data class ButtonClick(@JvmField val sucess: Boolean) :
        OTPEvent()


}