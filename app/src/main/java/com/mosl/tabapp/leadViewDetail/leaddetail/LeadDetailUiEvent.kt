package com.mosl.tabapp.leadViewDetail.leaddetail

import com.mosl.saathi.api.model.lead.InsertLeadProductRequest
import com.mosl.saathi.api.model.lead.UpdateLeadRequest
import com.mosl.tabapp.common.RowData

sealed class LeadDetailUiEvent{
  data class UpdateBtnEvent(val updateLeadRequest: UpdateLeadRequest) : LeadDetailUiEvent()
}
data class UpdateProfile(val selectedProfile: RowData) : LeadDetailUiEvent()
data class SelectInterestedProductEvent(val selectedProduct: RowData) : LeadDetailUiEvent()
data class SelectStageEvent(val selectedStage: RowData) : LeadDetailUiEvent()
data class ProductSpecialistEvent(
  val selectedProduct: String,
  val primaryRM: String
) : LeadDetailUiEvent()

data class AddProductEvent(val addProduct: InsertLeadProductRequest) : LeadDetailUiEvent()
data class AssignToEvent(val search: String) : LeadDetailUiEvent()