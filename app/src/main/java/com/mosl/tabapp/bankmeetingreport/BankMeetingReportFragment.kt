package com.mosl.tabapp.bankmeetingreport

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mosl.tabapp.R.layout
import com.mosl.tabapp.databinding.FragmentBankMeetingReportBinding
import com.mosl.tabapp.ui.MOSLCommonFragment

class BankMeetingReportFragment : MOSLCommonFragment() {

  private lateinit var binding: FragmentBankMeetingReportBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    binding = DataBindingUtil.inflate(
        inflater, layout.fragment_bank_meeting_report, container, false
    )
    return binding.root
  }
}
