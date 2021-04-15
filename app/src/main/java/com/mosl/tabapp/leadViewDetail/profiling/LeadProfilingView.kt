package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfileRequestData
import com.mosl.mobile.domain.leadviewdetail.profiling.ProfilingResponseModel
import com.mosl.tabapp.leadViewDetail.profiling.LeadProfileUiEvent.SubmitProfileEvent
import io.reactivex.rxjava3.core.Observable

interface LeadProfilingView : BaseView {

  fun render(leadId: String)
  fun renderProfileParameters(uiModel: UiModel<List<LeadProfile>>)

  fun onSubmitButtonClicked(): Observable<Unit>
  fun renderSubmitButtonClicked()

  fun onCancelButtonClicked(): Observable<Unit>
  fun renderCancelButtonClicked()
  fun getProfileRequestDataList(): List<ProfileRequestData>
  fun onSubmitProfileEventFired(): Observable<SubmitProfileEvent>
  fun renderSubmitProfile(uiModel: UiModel<ProfilingResponseModel>)
}