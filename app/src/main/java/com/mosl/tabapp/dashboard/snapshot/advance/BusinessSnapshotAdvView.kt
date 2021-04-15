package com.mosl.tabapp.dashboard.snapshot.advance

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotResponseModel
import io.reactivex.rxjava3.core.Observable
import java.util.*

interface BusinessSnapshotAdvView : BaseView {

  fun getWidgetSystemsName(): String?
  fun getClientList(): MutableList<HashMap<String, String>>
  fun getAdditionalParameterMap(): Map<String, String>

  fun onDataEvent(): Observable<Boolean>
  fun onNavigateEvent(): Observable<MutableMap<String, String>>

  fun renderData(uiModel: UiModel<BusinessSnapshotResponseModel>)
  fun renderUI()
}