package com.mosl.tabapp.leadViewDetail.main

import com.mosl.app.core.base.BasePresenter
import com.mosl.tabapp.ekyc.EkycNavigator
import javax.inject.Inject

class LeadViewDetailPagerPresenter @Inject constructor(
  view: LeadViewDetailPagerView,
  val data: LeadViewDetailArgument,
  val ekycNavigator: EkycNavigator
) : BasePresenter<LeadViewDetailPagerView>(view) {

  override fun start() {
    TODO("Not yet implemented")
  }
}