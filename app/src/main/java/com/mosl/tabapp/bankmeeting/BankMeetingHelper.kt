package com.mosl.tabapp.bankmeeting

import android.support.constraint.Group
import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.Spinner
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.JOINT_MEETING
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.JointMeeting
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.PERSONAL_MEETING
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.PersonalMeeting
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.TRAINING
import com.mosl.tabapp.bankmeeting.BankMeetingFragment.Companion.TrainingValue

fun Spinner.validateSpinnerValue(): Boolean {
  return this.selectedItem.toString().equals("Select", ignoreCase = true)
}

fun Spinner.getMeetingType(): Int {
  var a = 0
  when (this.selectedItem.toString()) {
    PersonalMeeting -> a = PERSONAL_MEETING
    TrainingValue -> a = TRAINING
    JointMeeting -> a = JOINT_MEETING
  }
  return a
}

fun Group.toggelVisibility(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
}

fun TextInputLayout.toggelInputLayoutVisibility(visible: Boolean) {
  visibility = if (visible) View.VISIBLE else View.GONE
}