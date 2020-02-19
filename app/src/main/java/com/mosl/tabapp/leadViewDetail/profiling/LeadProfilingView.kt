package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import io.reactivex.Observable

interface LeadProfilingView : BaseView {

  fun renderProfileParameters(uiModel: UiModel<List<LeadProfile>>)

  fun onSubmitButtonClicked(): Observable<Unit>
  fun renderSubmitButtonClicked()

  fun onCancelButtonClicked(): Observable<Unit>
  fun renderCancelButtonClicked()
}