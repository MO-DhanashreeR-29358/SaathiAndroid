package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard

import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.LstPeriodItem

object IndirectIFADashboardMapper {
    fun fnTranformFilterList( lstPeriod: List<LstPeriodItem?>?):List<LookUp>{
     return lstPeriod?.map { LookUp(0, it?.displayText?:"") }?: emptyList()
    }


    fun IndirectIFADashboardInput.requestTransform(): IndirectDashboardFilterPostRequest {
        return IndirectDashboardFilterPostRequest(
            employeeCode = employeeCode,
            frequency = frequency,
            calculatedPage = calculatedPage,
            calculatedPageP = calculatedPageP,
            page = page,
            pageP = pageP,
            pageSize = pageSize,
            pageSizeP = pageSizeP,
            totalCount = totalCount,
            category = category,
            filter = filter,
            fltUserStatus = fltUserStatus,
            date = date
        )
    }
}