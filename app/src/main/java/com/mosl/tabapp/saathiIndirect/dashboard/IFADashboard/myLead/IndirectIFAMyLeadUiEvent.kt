package com.mosl.tabapp.saathiIndirect.dashboard.IFADashboard.myLead


sealed class IndirectIFAMyLeadUiEvent{
    data class onTableRowClicked(var strID:String):IndirectIFAMyLeadUiEvent()
    object onDataRenderCall: IndirectIFAMyLeadUiEvent()
}
