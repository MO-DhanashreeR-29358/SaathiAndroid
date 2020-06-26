package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.tabapp.common.RowData

sealed class LeadDetailUiEvent
data class UpdateProfile(val selectedProfile: RowData) : LeadDetailUiEvent()
data class SelectInterestedProductEvent(val selectedProduct: RowData) : LeadDetailUiEvent()
data class SelectStageEvent(val selectedStage: RowData) : LeadDetailUiEvent()