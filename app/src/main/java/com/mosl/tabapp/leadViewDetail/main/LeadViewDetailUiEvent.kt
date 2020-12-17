package com.mosl.tabapp.leadViewDetail.main

sealed class LeadViewDetailUiEvent
data class OaoFormClick(val oaoId: Int) : LeadViewDetailUiEvent()
data class CreateMeetingClick(val id: Int) : LeadViewDetailUiEvent()
object SetAppointmentClick : LeadViewDetailUiEvent()
object OpenAccountClick : LeadViewDetailUiEvent()
object ShareLinkClick : LeadViewDetailUiEvent()
object SuggestMeClick : LeadViewDetailUiEvent()
object CopyLinkClick : LeadViewDetailUiEvent()
object CallClick : LeadViewDetailUiEvent()
object SMSClick : LeadViewDetailUiEvent()
object RetryClick : LeadViewDetailUiEvent()