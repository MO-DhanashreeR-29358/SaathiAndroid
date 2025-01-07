package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.Target

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.FilterPeriodModel

interface IndirectIFATargetView:BaseView {

    fun fnRenderFilterList(uiModel: UiModel<List<LookUp>>)
}