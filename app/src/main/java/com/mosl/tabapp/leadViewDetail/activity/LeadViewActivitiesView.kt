package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel

interface LeadViewActivitiesView : BaseView {

  fun render(uiModel: UiModel<String>)
}