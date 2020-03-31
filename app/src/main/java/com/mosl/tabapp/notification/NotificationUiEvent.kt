package com.mosl.tabapp.notification

sealed class NotificationUiEvent {
    data class ItemClick(@JvmField val additionalData: String) : NotificationUiEvent()
}