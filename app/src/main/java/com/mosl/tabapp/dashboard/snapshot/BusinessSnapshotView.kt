package com.mosl.tabapp.dashboard.snapshot

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.snapshot.BusinessSnapshotCombo
import com.mosl.tabapp.common.FilterRowData
import io.reactivex.Observable

interface BusinessSnapshotView : BaseView {
  fun onFilter1Change(): Observable<Int>
  fun onFilter2Change(): Observable<Int>
  fun onFilter3Change(): Observable<Int>
  fun onNavigateEvent(): Observable<MutableMap<String, String>>

  fun getWidgetSystemsName(): String?
  fun getAdditionalParameterMap(rowData: String, columnData: String): Map<String, String>
  fun getFilter1(): FilterRowData?
  fun getFilter2(): FilterRowData?
  fun getFilter3(): FilterRowData?

  fun renderData(uiModel: UiModel<BusinessSnapshotCombo>)
}