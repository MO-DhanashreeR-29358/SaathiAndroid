package com.mosl.tabapp.leadcreation.ui

import com.mosl.tabapp.leadViewDetail.createmeeting.CreateMeetingUiEvent

sealed class LeadCreationUiEvent {
  data class AssignToSearchEvent(val searchText: String) : LeadCreationUiEvent()
  data class SecondaryRMSearchEvent(val searchText: String) : LeadCreationUiEvent()
  data class PCGSecondaryRMSearchEvent(val searchText: String) : LeadCreationUiEvent()
  data class TransferLeadEvent(val leadId: String) : LeadCreationUiEvent()
  data class TransferLeadPayment(val leadId: String) : LeadCreationUiEvent()
  object CallAccountType : LeadCreationUiEvent()
  object CreateLeadEvent : LeadCreationUiEvent()
  object CallChipsApi : LeadCreationUiEvent()
}