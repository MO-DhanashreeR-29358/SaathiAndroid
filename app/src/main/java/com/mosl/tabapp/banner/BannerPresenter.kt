package com.mosl.tabapp.banner

import com.mosl.app.core.base.BasePresenter
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class BannerPresenter @Inject constructor(
    view: BannerView,
    val navigator: BannerNavigator,
    val data: BannerScreenData
) : BasePresenter<BannerView>(view) {

    override fun start() {

        onFetchCalled()
            .subscribe {
                view.render(data)
            }
            .addTo(disposable)

        view.onCloseButtonClicked()
            .subscribe { view.dismissDialog() }
            .addTo(disposable)

        val bannerImageClickObs = view.onBannerImageClicked()
            .map { data.target to data.actionUrl }
            .share()


        bannerImageClickObs
            .filter { it.first == 0 }
            .map { it.second }
            .subscribe(navigator::openUrlInAppCrome)
            .addTo(disposable)

        bannerImageClickObs
            .filter { it.first == 1 }
            .map { it.second }
            .subscribe(navigator::openUrlInBrowser)
            .addTo(disposable)

        bannerImageClickObs
            .filter { it.first == 2 }
            .map { it.second }
            .subscribe(navigator::openUrlInWebView)
            .addTo(disposable)

        bannerImageClickObs
            .subscribe { view.dismissDialog() }
            .addTo(disposable)

    }
}