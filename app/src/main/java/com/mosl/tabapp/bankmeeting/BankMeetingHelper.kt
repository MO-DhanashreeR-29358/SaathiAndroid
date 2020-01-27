package com.mosl.tabapp.bankmeeting

import android.view.View
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.google.android.material.textfield.TextInputLayout
import com.mosl.saathi.api.model.aubankmeeting.MeetingSaveRequest
import com.mosl.tabapp.R
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.BRANCH_VISIT
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.BranchVisit
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.CLIENT_MEETING
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.ClientMeeting
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.TRAINING
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.TrainingValue
import com.mosl.tabapp.bankmeeting.model.BankMeetingInput
import com.mosl.tabapp.databinding.FragmentAuBankMeetingBinding

fun Spinner.validateSpinnerValue(): Boolean {
    return this.selectedItem.toString().equals("Select", ignoreCase = true)
}

fun Spinner.getMeetingType(): Int {
    var a = 0
    when (this.selectedItem.toString()) {
        BranchVisit -> a = BRANCH_VISIT
        TrainingValue -> a = TRAINING
        ClientMeeting -> a = CLIENT_MEETING
    }

    return a
}

fun Group.toggelVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextInputLayout.toggelInputLayoutVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun transformInput(data: BankMeetingInput): MeetingSaveRequest {
    return MeetingSaveRequest(
        meetingTypeId = data.meetingTypeId,
        bankId = data.bankId.toInt(),
        bankBranchId = data.bankBranchId.toInt(),
        clientName = data.clientName,
        meetingDate = data.meetingDate,
        meetingDescription = data.meetingDescription,
        meetingDurationId = data.meetingDurationId.toInt(),
        subBrokerId = data.subBrokerId,
        subject = data.subject,
        bankEmployeeCode = data.bankEmployeeCode.toString(),
        clientMobileNumber = data.clientMobileNumber


    )
}

fun FragmentAuBankMeetingBinding.showValidationMsg(msg: Int) {
    val context = this.root.context
    when (msg) {
        0 -> {
            tvDateError.text = context.getString(R.string.validate_meeting_date)
            tvDateError.toggleTextView(true)
        }
        1 -> {
            tvMeetingTypeError.text = context.getString(R.string.validate_meeting_type)
            tvMeetingTypeError.toggleTextView(true)
            spMeetingType.requestFocus()
        }
        2 -> {
            tvMeetingDurationError.text = context.getString(R.string.validate_meeting_duration)
            tvMeetingDurationError.toggleTextView(true)
            spMeetingDuration.requestFocus()
        }
        3 -> {
            ilMeetingDescription.error = context.getString(R.string.validate_meeting_description)
            etMeetingDescription.requestFocus()
        }
        4 -> {
            tvBankError.text = context.getString(R.string.validate_bank)
            tvBankError.toggleTextView(true)
            spBank.requestFocus()
        }
        5 -> {
            tvBranchError.text = context.getString(R.string.validate_bank_branch)
            tvBranchError.toggleTextView(true)
            spBranch.requestFocus()
        }
        6 -> {
            ilSubBroker.error = context.getString(R.string.validate_sub_broker)
            actvSubBroker.requestFocus()
        }
        7 -> {
            tvBankEmployeeError.text = context.getString(R.string.validate_bank_employee)
            tvBankEmployeeError.toggleTextView(true)
//      ilBankEmployee.error = context.getString(R.string.validate_bank_employee)
//      actvBankEmployee.requestFocus()
        }
        8 -> {
            ilSubject.error = context.getString(R.string.validate_subject)
            etSubject.requestFocus()
        }
        9 -> {
            ilClientName.error = context.getString(R.string.validate_client_name)
            etClientName.requestFocus()
        }
        10 -> {
            ilMobileNumber.error = context.getString(R.string.validate_client_mobile_no)
            etMobileNumber.requestFocus()
        }
        11 -> {
            ilMobileNumber.error = context.getString(R.string.validate_invalid_mobile_number)
            etMobileNumber.requestFocus()
        }
    }

}

fun TextView.toggleTextView(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextInputLayout.setErrorMessages() {
    isErrorEnabled = false
}

fun Spinner.isSpinnerVisible(): Boolean {
    return this.visibility == View.VISIBLE
}