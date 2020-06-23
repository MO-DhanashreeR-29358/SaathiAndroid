package com.mosl.mobile.domain.dashboardreport

data class LeadAssignRequest(
    @JvmField val pageSize: Int,

    @JvmField val pageIndex: Int,

    @JvmField val calculatedPage: Int

)

data class STPLeadTransferModel(
    @JvmField val prospectId: String,
    @JvmField val assignToTransferLead: String,
    @JvmField val checkAllFlag: Int = 0
)