package com.mosl.tabapp.advancesearch.quickSearch

sealed class QuickSearchUiEvent{
    data class onTableRowClicked(var strLeadId:String):QuickSearchUiEvent()
}
