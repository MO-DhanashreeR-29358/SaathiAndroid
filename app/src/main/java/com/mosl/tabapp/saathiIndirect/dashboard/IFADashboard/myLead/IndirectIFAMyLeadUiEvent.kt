package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead


sealed class IndirectIFAMyLeadUiEvent{
    data class onTableRowClicked(var strCategory:String,var strFilter:String):IndirectIFAMyLeadUiEvent()
    object onDataRenderCall: IndirectIFAMyLeadUiEvent()
}
