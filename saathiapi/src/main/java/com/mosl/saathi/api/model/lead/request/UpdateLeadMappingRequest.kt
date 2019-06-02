package com.mosl.saathi.api.model.lead.request

class UpdateLeadMappingRequest(
    @JvmField val leadId: String,
    @JvmField val interestedIn: String,
    @JvmField val leadProductMappingID: String,
    @JvmField val productID: String,
    @JvmField val primaryRM: String,
    @JvmField val productSpecialist: String,
    @JvmField val assignedBy: String,
    @JvmField val assignedByDate: String,
    @JvmField val stageID: String,
    @JvmField val stageName: String,
    @JvmField val isPotentialPMSProduct: String,
    @JvmField val statusID: String,
    @JvmField val statusValue: String,
    @JvmField val actAssignTo: String,
    @JvmField val map: Map<String, Any?>
) {
  fun toMap(): Map<String, Any?> {
    val mutableMapOf = mutableMapOf<String, Any?>(
        "LeadID" to leadId,
        "InterestedIn" to interestedIn,
        "LeadProductMappingID" to leadProductMappingID,
        "ProductID" to productID,
        "PrimaryRM" to primaryRM,
        "ProductSpecialist" to productSpecialist,
        "AssignedBy" to assignedBy,
        "AssignedByDate" to assignedByDate,
        "StageID" to stageID,
        "IsPotentialPMSProduct" to isPotentialPMSProduct,
        "StatusID" to statusID
    )
    mutableMapOf.putAll(map)
    return mutableMapOf
  }
}