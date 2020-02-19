package com.mosl.tabapp.leadViewDetail.profiling

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.profiling.LeadProfile

interface LeadProfilingView : BaseView {

  fun renderProfileParameters(uiModel: UiModel<List<LeadProfile>>)
}