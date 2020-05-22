package com.mosl.app.core.data.leadviewdetail.createmeeting

import com.mosl.app.core.data.util.addDefaultSelection
import com.mosl.mobile.domain.createmeeting.AddUpdateMeeting
import com.mosl.mobile.domain.createmeeting.Attendee
import com.mosl.mobile.domain.createmeeting.ClientDetail
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.saathi.api.model.addupdatemeeting.AddUpdateMeetingItem
import com.mosl.saathi.api.model.clientdetail.ClientDetailItem
import com.mosl.saathi.api.model.createmeeting.AutoCompleteAttendeeItem
import com.mosl.saathi.api.model.lead.LeadViewItem
import com.mosl.saathi.api.model.lead.LookupItem

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
        isPotentialPMSProduct = clientDetailItem.isPotentialPMSProduct ?: ""
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
        currentStageSince = leadViewItem.leadProductMapping?.get(0)?.pendingDays.toString() ?: "",
        leadId = leadViewItem.leadID,
        isPotentialPMSProduct = leadViewItem.isPotentialPMSProduct ?: "",
        isOnlyClient = false
    )
  }

  fun transformLookupData(
    list: List<LookupItem>,
    type: String
  ): List<LookUp> {
    return list.map { LookUp(it.lookUpid, it.lookUpName ?: "") }
        .addDefaultSelection(LookUp(0, type))
  }

  fun transformAddUpdateMeeting(addUpdateMeetingItem: AddUpdateMeetingItem): AddUpdateMeeting {
    return AddUpdateMeeting(
        leadId = addUpdateMeetingItem.leadId ?: 0,
        ucidId = addUpdateMeetingItem.uCID ?: 0
    )
  }
}