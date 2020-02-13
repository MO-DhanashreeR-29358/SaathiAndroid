package com.mosl.tabapp.banner

import com.mosl.app.core.base.BaseView
import io.reactivex.Observable

interface BannerView : BaseView {
    fun render(data: BannerScreenData)
    fun onCloseButtonClicked(): Observable<Unit>
    fun dismissDialog()
    fun onBannerImageClicked(): Observable<Unit>

    companion object {
        const val ARG_BANNER_SCREEN_DATA = "arg_banner_screen_data"
    }

}