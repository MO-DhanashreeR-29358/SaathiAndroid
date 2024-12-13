package com.mosl.tabapp.saathiIndirect.prospestMeeting

import android.content.Context
import com.mosl.tabapp.model.CreateMeetingPostReq
import com.mosl.validator.core.Validator
import com.mosl.validator.data.ValidationResult
import javax.inject.Inject

class IndirectProspectMeetingValidation @Inject constructor(val context: Context):
    Validator<CreateMeetingPostReq, CreateMeetingPostReq>() {
    override fun execute(input: CreateMeetingPostReq): ValidationResult<CreateMeetingPostReq> {
        val errorMessage=when{
            input.activityTypeID.equals("0")->{
                "Select activity type"
            }
            input.meetingReasonID==0->{
                "Select meeting reason"
            }
            input.productTypeID.equals("0")->{
                "Select interested in"
            }
            input.meetingDurationID==0->{
                "Select meeting duration"
            }
            input.meetingDescription?.length==0->{
                "Enter meeting description"
            }
            input.meetingObjectiveID.equals("0")->{
                "Select meeting objective"
            }
            input.meetingAddressTypeId.equals("0") && input.activityTypeID.equals("3743")->{
                "Select meeting address"
            }
            input.travellingList?.length==0 && input.activityTypeID.equals("3743") ->{
                "Select travelling mode"
            }
            else -> {
                null
            }
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
                attendeeCode=input.attendeeCode)
            )
        }
    }



}