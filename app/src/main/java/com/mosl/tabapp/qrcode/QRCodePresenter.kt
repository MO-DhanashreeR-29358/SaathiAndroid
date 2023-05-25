package com.mosl.tabapp.qrcode

import com.mosl.app.core.base.BasePresenter
import com.mosl.mobile.domain.qrcode.QRCodeRepository
import com.mosl.mobile.user.login.UserManager
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

class QRCodePresenter @Inject constructor(
    view: QRCodeView,
    val qrCodeRepository: QRCodeRepository,
    val userManager: UserManager
) : BasePresenter<QRCodeView>(view) {

    override fun start() {

        onFetchCalled().switchMap {
            qrCodeRepository.getQRCode(userManager.getUser()!!.employeeCode)
                .compose(applyUiModel())
        }.subscribe(view::renderGetQRCode)
        .addTo(disposable)

    }
}