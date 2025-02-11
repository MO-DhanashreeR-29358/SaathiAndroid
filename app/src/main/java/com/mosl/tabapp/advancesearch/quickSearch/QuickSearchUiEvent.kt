package com.mosl.tabapp.advancesearch.quickSearch


sealed class QuickSearchUiEvent{
    data class onTableRowClicked(var strLeadId:String):QuickSearchUiEvent()

    data class CreateMeetingClick(val id: Int) : QuickSearchUiEvent()
    object SetAppointmentClick : QuickSearchUiEvent()
    object  ViewDetailsClicked:QuickSearchUiEvent()
}
