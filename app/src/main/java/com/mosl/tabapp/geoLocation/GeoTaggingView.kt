package com.mosl.tabapp.geoLocation

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import io.reactivex.rxjava3.core.Observable

interface GeoTaggingView : BaseView {
  fun onMapTypeClicked(): Observable<Unit>
  fun onSaveClicked(): Observable<Unit>
  fun renderMapType(uiModel: UiModel<Unit>)
  fun renderSave()

}

