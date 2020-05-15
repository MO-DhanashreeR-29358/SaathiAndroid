package com.mosl.tabapp.leadcreation.ui

sealed class LeadCreationUiEvent {
  data class AssignToSearchEvent(val searchText: String) : LeadCreationUiEvent()
  object CreateLeadEvent : LeadCreationUiEvent()
}