package com.mosl.tabapp.saathiIndirect.dashboard.cncfoco

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass

interface CncFocoDashboardView :BaseView {
    fun fnRenderFilterList(uiModel: UiModel<IndirectFilterDataClass>)
}