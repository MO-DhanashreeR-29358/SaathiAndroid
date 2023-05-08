package com.mosl.tabapp.geoLocation

import com.mosl.app.core.base.BasePresenter
import com.mosl.commons.rx.addTo
import javax.inject.Inject

class GeoTaggingPresenter @Inject constructor(
    view: GeoTaggingView
) : BasePresenter<GeoTaggingView>(view) {

    override fun start() {
        view.onMapTypeClicked()
            .compose(applyUiModel())
            .subscribe { view.renderMapType(it) }
            .addTo(disposable)

        view.onSaveClicked()
            .subscribe {
                view.renderSave()
            }
            .addTo(disposable)
    }
}