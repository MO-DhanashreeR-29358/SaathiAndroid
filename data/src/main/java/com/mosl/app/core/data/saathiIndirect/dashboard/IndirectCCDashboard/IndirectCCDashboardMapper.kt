package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectCCDashboard

import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.CCDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard.IndirectCCFilterDataClass
import com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectIFADashboard.IndirectIFADashboardInput
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectCCDashboardFilterResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardFilterPostRequest
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectIFADashboardFilterResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.LstPeriodItem

object IndirectCCDashboardMapper {
    fun fnTranformFilterList( objIndirectCCDashboardFilterResponse: IndirectCCDashboardFilterResponse?): IndirectCCFilterDataClass {

        return  IndirectCCFilterDataClass(
            lstPeriods=objIndirectCCDashboardFilterResponse?.lstPeriod?.map { LookUp(0, it?.displayText?:"") }?: emptyList(),
            lstAdvisor = objIndirectCCDashboardFilterResponse?.lstAdvisor?.map { LookUp(0, it?.displayText?:"") }?: emptyList()
        )

    } 


    fun CCDashboardInput.requestTransform(): FOCODashboardFilterpostReq {
        return FOCODashboardFilterpostReq(
            employeeCode = employeeCode,
            fltPeriod = fltPeriod,
            fltAdvisorEmpCode = fltAdvisorEmpCode,
            calculatedPage = calculatedPage,
            calculatedPageP = calculatedPageP,
            page = page,
            pageP = pageP,
            pageSize = pageSize,
            pageSizeP = pageSizeP,
            totalCount = totalCount,
            filter = filter
        )
    }
}