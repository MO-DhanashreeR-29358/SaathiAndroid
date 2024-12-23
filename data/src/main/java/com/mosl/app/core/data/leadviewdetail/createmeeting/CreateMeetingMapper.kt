package com.mosl.app.core.data.leadviewdetail.createmeeting

import android.util.Log
import com.google.gson.Gson
import com.mosl.app.core.data.util.addDefaultSelection
import com.mosl.mobile.domain.createmeeting.*
import com.mosl.mobile.domain.dashboardreport.STPLeadTransferResponse
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingNewResposne
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.createmeeting.ChipsEventResponse
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LookupItem
import com.mosl.saathi.api.model.lead.createmeeting.MobileListResponse

object CreateMeetingMapper {
    fun transformClientDetail(clientDetailItem: ClientDetailItem): ClientDetail {
        return ClientDetail(
            name = clientDetailItem.clientName ?: "",
            branchcode = clientDetailItem.branchCode ?: "",
            clientcode = clientDetailItem.clientCode ?: "",
            currentStage = clientDetailItem.stageName ?: "",
            currentStageSince = clientDetailItem.pendingDays ?: "",
            leadId = clientDetailItem.leadID,
            isOnlyClient = clientDetailItem.isOnlyClient,
            isPotentialPMSProduct = clientDetailItem.isPotentialPMSProduct ?: "",
            sourceName = clientDetailItem.sourceName?: "",
            prospectSource = clientDetailItem.prospectSource?: "",
            activateOTPInApp = clientDetailItem.ActivateOTPInApp?: ""
        )
    }

    fun transformAttendeeData(list: List<AutoCompleteAttendeeItem>): List<Attendee> {
        if (list.isNotEmpty()) {
            return list.map {
                Attendee(
                    empName = it.employeeName ?: "",
                    branchCode = it.branchCode ?: "",
                    empCode = it.employeeCode ?: "",
                    hasData = true
                )
            }
        } else {
            return listOf(
                Attendee(
                    empName = "Not Found",
                    branchCode = "",
                    hasData = false,
                    empCode = ""
                )
            )
        }
    }

    fun transformLeadView(leadViewItem: LeadViewItem): ClientDetail {
        return ClientDetail(
            name = leadViewItem.contact?.name ?: "",
            branchcode = leadViewItem.branchcode ?: "",
            clientcode = leadViewItem.leadProductMapping?.get(0)?.clientCode ?: "",
            currentStage = leadViewItem.leadProductMapping?.get(0)?.stageName ?: "",
            currentStageSince = leadViewItem.leadProductMapping?.get(0)?.pendingDays.toString()
                ?: "",
            leadId = leadViewItem.leadID,
            isPotentialPMSProduct = leadViewItem.isPotentialPMSProduct ?: "",
            isOnlyClient = false,
            sourceName = leadViewItem.sourceName?: "",
            prospectSource = leadViewItem.prospectSource?: "",
            activateOTPInApp = leadViewItem.ActivateOTPInApp?: ""
        )
    }

    fun transformLookupData(
        list: List<LookupItem>,
        type: String
    ): List<LookUp> {
        return list.map { LookUp(it.lookUpid, it.lookUpName ?: "") }
            .addDefaultSelection(LookUp(0, type))
    }

    fun transformMobileList(
        list: List<MobileListResponse>
    ): List<MobileListData> {
        return list.map { MobileListData(it.contactField, it.contactNumber) }
            .addDefaultSelection(MobileListData("Select Mobile Number For Verification Code", "0"))
    }

    fun transformAddUpdateMeeting(addUpdateMeetingItem: AddUpdateMeetingItem): AddUpdateMeeting {
        return AddUpdateMeeting(
            leadId = addUpdateMeetingItem.leadId ?: 0,
            ucidId = addUpdateMeetingItem.uCID ?: 0
        )
    }


    fun transformAddUpdateMeetingNew(addUpdateMeetingItem: AddUpdateMeetingNewResposne):AddUpdateMeeting  {
        return AddUpdateMeeting(
            leadId = addUpdateMeetingItem.response?.leadId ?: 0,
            ucidId = addUpdateMeetingItem.response?.uCID ?: 0
        )
    }
    fun mapToSendOtpResponse(
        status: Boolean,
        error: String
    ): SendOtpResponse {
        return SendOtpResponse(
            status = status,
            errorMessage = error
        )
    }

    fun mapToCommanResponse(
        response : String?,
        status: Boolean,
        error: String
    ): CommanResponse {
        return CommanResponse(
            response = response?: "",
            status = status,
            errorMessage = error
        )
    }

    fun transformEventChipsList(
        list: List<ChipsEventResponse>
    ): List<String> {
        return list.map {
            it.eventName
        }.addDefaultSelection("Select Event")

    }
}