package com.mosl.tabapp.leadViewDetail.main

import android.os.Bundle
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadviewMainBinding
import javax.inject.Inject

class LeadViewDetailPagerFragment : CommonFragment(), LeadViewDetailPagerView {

  private lateinit var binding: FragLeadviewMainBinding
  @Inject internal lateinit var presenter: LeadViewDetailPagerPresenter

  override fun getLayout(): Int {
    return R.layout.frag_leadview_main
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadviewMainBinding.bind(view)
    presenter.start()
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }




}