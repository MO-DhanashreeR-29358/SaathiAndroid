package com.mosl.tabapp.leadcreation

import android.os.Bundle
import android.view.View
import com.mosl.tabapp.R
import com.mosl.tabapp.core.CommonFragment
import com.mosl.tabapp.databinding.FragLeadCreationBinding
import javax.inject.Inject

class LeadCreationFragment : CommonFragment(), LeadCreationView {

  @Inject internal lateinit var presenter: LeadCreationPresenter
  private lateinit var binding: FragLeadCreationBinding

  override fun getLayout(): Int = R.layout.frag_lead_creation

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
    binding = FragLeadCreationBinding.bind(view)
  }

  override fun onDestroyView() {
    presenter.stop()
    super.onDestroyView()
  }
}