package com.mosl.tabapp.leadViewDetail.leaddetail

import android.os.Bundle
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadviewDetailBinding
import javax.inject.Inject

class LeadDetailFragment : CommonFragment(), LeadDetailView {

  @Inject internal lateinit var presenter: LeadDetailPresenter
  private lateinit var binding: FragLeadviewDetailBinding

  override fun getLayout(): Int {
    return R.layout.frag_leadview_detail
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadviewDetailBinding.bind(view)
    setupView()
    presenter.start()
  }

  private fun setupView() {

  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}