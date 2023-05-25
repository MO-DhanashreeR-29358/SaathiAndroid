package com.mosl.tabapp.qrcode

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel

interface QRCodeView : BaseView {
    fun renderGetQRCode(uiModel: UiModel<String>)
}