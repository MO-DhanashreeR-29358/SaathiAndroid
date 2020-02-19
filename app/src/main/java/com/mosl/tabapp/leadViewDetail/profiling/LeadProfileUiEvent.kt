package com.mosl.tabapp.leadViewDetail.profiling

sealed class LeadProfileUiEvent {
  object SubmitProfileEvent : LeadProfileUiEvent()
}