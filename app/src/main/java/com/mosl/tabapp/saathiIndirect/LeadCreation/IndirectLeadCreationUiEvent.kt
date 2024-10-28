package com.mosl.tabapp.saathiIndirect.LeadCreation

sealed class IndirectLeadCreationUiEvent {
  data class AssignToSearchEvent(val searchText: String) : IndirectLeadCreationUiEvent()
  data class SecondaryRMSearchEvent(val searchText: String) : IndirectLeadCreationUiEvent()
  data class PCGSecondaryRMSearchEvent(val searchText: String) : IndirectLeadCreationUiEvent()
  data class TransferLeadEvent(val leadId: String) : IndirectLeadCreationUiEvent()
  data class TransferLeadPayment(val leadId: String) : IndirectLeadCreationUiEvent()
  object CallAccountType : IndirectLeadCreationUiEvent()
  object CreateLeadEvent : IndirectLeadCreationUiEvent()
  object CallChipsApi : IndirectLeadCreationUiEvent()
}