package com.mosl.tabapp.saathiIndirect.createMeeting

import android.content.Context
import com.mosl.mobile.user.login.UserManager
import com.mosl.tabapp.model.CreateMeetingPostReq
import com.mosl.tabapp.model.SetAppointmentPostReq
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

class IndirectCreateMeetingValidator @Inject constructor(val context: Context, val user: UserManager):Validator<CreateMeetingPostReq,CreateMeetingPostReq>() {
    override fun execute(input: CreateMeetingPostReq): ValidationResult<CreateMeetingPostReq> {
        val errorMessage = when {
            input.activityTypeID.isNullOrEmpty() || input.activityTypeID == "0" || input.activityTypeID == "null" -> {
                "Select activity type"
            }
            input.meetingReasonID == null || input.meetingReasonID == 0 -> {
                "Select meeting reason"
            }
            input.productTypeID.isNullOrEmpty() || input.productTypeID == "0" || input.productTypeID == "null" -> {
                "Select interested in"
            }
            input.meetingDurationID == null || input.meetingDurationID == 0 -> {
                "Select meeting duration"
            }
            input.meetingDescription.isNullOrEmpty() -> {
                "Enter meeting description"
            }
            (input.meetingDescription?.length ?: 0) < 50 -> {
                "Enter atleast 50 characters"
            }
            input.meetingObjectiveID.isNullOrEmpty() || input.meetingObjectiveID == "0" || input.meetingObjectiveID == "null" -> {
                "Select meeting objective"
            }
            (input.meetingAddressTypeId.isNullOrEmpty() || input.meetingAddressTypeId == "0") && input.activityTypeID == "3743" -> {
                "Select meeting address"
            }
            (input.travellingList.isNullOrEmpty()) && input.activityTypeID == "3743" -> {
                "Select travelling mode"
            }
            (input.pincode?.length ?: 0) < 6 && input.activityTypeID == "3743" -> {
                "Invalid pincode number"
            }
            user.getUser()?.isRemiser() == true && (input.meetingTypeR.isNullOrEmpty() || input.meetingTypeR == "Select Meeting Type") -> {
                "Select Meeting Type"
            }
            else -> null
        }

        return if (errorMessage != null) {
            withMessage(errorMessage)
        } else {
            withData( CreateMeetingPostReq(meetingLatitude=input.meetingLatitude,
                    meetingLongitude=input.meetingLongitude,
                     meetingAddress=input.meetingAddress,
                     mEETINGTYPEMEETING="M",
                     meetingType="M",
                     meetingDuration=input.meetingDuration,
                         travellingList=input.travellingList,
                         meetingDate =input.meetingDate,
                         meetingDescription=input.meetingDescription,
                         leadId=input.leadId,
                         meetingId =input.meetingId,
                         meetingReason=input.meetingReason,
                         ucid=input.ucid,
                         pincode=input.pincode,
                         amount=input.amount,
                         product=input.product,
                                 meetingAttendee=input.meetingAttendee,
                                 meetingDurationID=input.meetingDurationID,
                                 geoLongitude=input.geoLongitude,
                                 geoArea=input.geoArea,
                                 mEETINGTYPEAPPT ="A",
                                 displayType="A",
                         geoLatitude=input.geoLatitude,
                         dISPLAYTYPEMEETING="U",
                         meetingAddressTypeId=input.meetingAddressTypeId,
                         meetingObjectiveID=input.meetingObjectiveID,
                         meetingReasonID=input.meetingReasonID,
                         activityTypeID=input.activityTypeID,
                        productTypeID=input.productTypeID,
            attendeeCode=input.attendeeCode,
                meetingTypeR = input.meetingTypeR))
        }
    }


}