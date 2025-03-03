package com.mosl.tabapp.advancesearch


sealed class QuickSearchUiEvent{
    data class onTableRowClicked(var strLeadId:String,var strUcId:String): QuickSearchUiEvent()

    data class CreateMeetingClick(val id: Int) : QuickSearchUiEvent()
    object SetAppointmentClick : QuickSearchUiEvent()
    object  ViewDetailsClicked: QuickSearchUiEvent()
}
