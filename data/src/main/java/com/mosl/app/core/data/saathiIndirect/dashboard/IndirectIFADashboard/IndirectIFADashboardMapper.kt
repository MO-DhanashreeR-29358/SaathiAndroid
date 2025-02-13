package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard

import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectIFADashboardFilterResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.LstPeriodItem

object IndirectIFADashboardMapper {
    fun fnTranformFilterList( objIndirectIFADashboardFilterResponse: IndirectIFADashboardFilterResponse?): IndirectFilterDataClass {

        return  IndirectFilterDataClass(
            lstPeriods=objIndirectIFADashboardFilterResponse?.lstPeriod?.map { LookUp(0, it?.displayText?:"") }?: emptyList(),
            lstProduct = objIndirectIFADashboardFilterResponse?.lstProduct?.map { LookUp(0, it?.displayText?:"") }?: emptyList(),
            lstStageCode = objIndirectIFADashboardFilterResponse?.lstStageCode?.map { LookUp(0, it?.displayText?:"") }?: emptyList()
        )

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
            date = date,
            fitHierarchy = fitHierarchy
        )
    }
}