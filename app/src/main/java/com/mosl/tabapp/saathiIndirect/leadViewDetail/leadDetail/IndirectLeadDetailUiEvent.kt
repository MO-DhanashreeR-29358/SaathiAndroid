package com.mosl.tabapp.saathiIndirect.leadViewDetail.leadDetail

import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.tabapp.common.RowData

sealed class IndirectLeadDetailUiEvent
data class UpdateProfile(val selectedProfile: RowData) : IndirectLeadDetailUiEvent()
data class SelectInterestedProductEvent(val selectedProduct: RowData) : IndirectLeadDetailUiEvent()
data class SelectStageEvent(val selectedStage: RowData) : IndirectLeadDetailUiEvent()
data class ProductSpecialistEvent(
    val selectedProduct: String,
    val primaryRM: String
) : IndirectLeadDetailUiEvent()

data class AddProductEvent(val addProduct: InsertLeadProductRequest) : IndirectLeadDetailUiEvent()
data class AssignToEvent(val search: String) : IndirectLeadDetailUiEvent()

object UpdateLeadDetail : IndirectLeadDetailUiEvent()

object UpdateLeadSection : IndirectLeadDetailUiEvent()

object CallAccountType : IndirectLeadDetailUiEvent()