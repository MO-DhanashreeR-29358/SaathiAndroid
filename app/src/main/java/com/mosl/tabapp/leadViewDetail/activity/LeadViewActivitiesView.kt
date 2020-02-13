package com.mosl.tabapp.leadViewDetail.activity

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityData
import com.mosl.mobile.domain.leadviewdetail.activities.ActivityRequest

interface LeadViewActivitiesView : BaseView {

  fun getActivityRequest(): ActivityRequest

  fun render(uiModel: UiModel<ActivityData>)
}