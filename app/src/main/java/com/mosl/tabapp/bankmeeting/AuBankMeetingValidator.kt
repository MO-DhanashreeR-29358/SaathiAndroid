package com.mosl.tabapp.bankmeeting

import androidx.core.view.isVisible
import com.mosl.tabapp.bankmeeting.model.BankMeetingInput
import com.mosl.tabapp.databinding.FragmentAuBankMeetingBinding

fun BankMeetingInput.validateForm(binding: FragmentAuBankMeetingBinding): Boolean {
  when {
    this.meetingDate.isEmpty() -> {
      binding.showValidationMsg(0)
      return false
    }
    this.meetingTypeId.isEmpty() || this.meetingTypeId == "-1" -> {
      binding.showValidationMsg(1)
      return false
    }
    this.meetingDurationId.isEmpty() || this.meetingDurationId == "-1" -> {
      binding.showValidationMsg(2)
      return false
    }
    this.meetingDescription.isEmpty() -> {
      binding.showValidationMsg(3)
      return false
    }
    this.bankId.isEmpty() || this.bankId == "-1" -> {
      binding.showValidationMsg(4)
      return false
    }
    this.branchId.isEmpty() || this.branchId == "-1" -> {
      binding.showValidationMsg(5)
      return false
    }
    !this.disabledFields && this.bankLocationName == "-1"  -> {
      binding.showValidationMsg(12)
      return false
    }
    !this.disabledFields && this.subBrokerId == -1 -> {
      binding.showValidationMsg(6)
      return false
    }
    /*!this.disabledFields && this.bankEmployeeCode.isEmpty() -> {
      binding.showValidationMsg(7)
      return false
    }*/
    this.isSubjectVisible && this.subject.isEmpty() -> {
      binding.showValidationMsg(8)
      return false
    }
      this.isClientNameVisible && this.clientMobileNumber.isEmpty() -> {
      binding.showValidationMsg(9)
      return false
    }
      this.isClientNameVisible && this.clientMobileNumber.length < 10 -> {
      binding.showValidationMsg(10)
      return false
    }
      this.isClientNameVisible && this.clientName.isEmpty() -> {
      binding.showValidationMsg(11)
      return false
    }

      else -> return true
  }

}