package com.mosl.tabapp.dashboard.base

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.dashboard.DashboardData

interface DashboardView : BaseView {
  fun render(uiModel: UiModel<List<DashboardData>>)
}