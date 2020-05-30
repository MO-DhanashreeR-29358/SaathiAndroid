package com.mosl.tabapp.leadViewDetail.main

sealed class LeadViewDetailUiEvent
object ShareLinkClick : LeadViewDetailUiEvent()
object SuggestMeClick : LeadViewDetailUiEvent()
object CopyLinkClick : LeadViewDetailUiEvent()