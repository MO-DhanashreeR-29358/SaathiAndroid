package com.mosl.tabapp.saathiIndirect.setAppointment

import android.content.Context
import androidx.annotation.StringRes
import com.mosl.tabapp.R
import com.mosl.tabapp.model.MeetingAttendeeItem
import com.mosl.tabapp.model.SetAppointmentPostReq
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

class IndirectSetAppointmentValidator @Inject constructor(val context: Context):Validator<SetAppointmentPostReq,SetAppointmentPostReq>() {
    override fun execute(input: SetAppointmentPostReq): ValidationResult<SetAppointmentPostReq> {

            val errorMessage= when{
                    input.activityTypeID.equals("0") ->{
                       "Please select activity type"
                    }
                input.meetingReasonID==0->{
                    "Please select meeting reason"
                }
                input.meetingDes?.length==0->{
                    "Enter meeting comments"
                }
                else -> {
                    null
                }
            }
        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
            withData( SetAppointmentPostReq(
            product = input.product,
            meetingAttendee = input.meetingAttendee,
            mEETINGTYPEMEETING = input.meetingType,
            meetingDurationID = input.meetingDurationID,
            meetingType = input.meetingType,
            meetingDes = input.meetingDes,
            mEETINGTYPEAPPT = "M",
            displayType = input.displayType,
            dISPLAYTYPEMEETING = "U",
            meetingObjectiveID = input.meetingObjectiveID,
            meetingDate = input.meetingDate,
            meetingReasonID = input.meetingReasonID,
            activityTypeID = input.activityTypeID,
            leadId = input.leadId,
            ucid = input.ucid)
            )
        }
    }



}