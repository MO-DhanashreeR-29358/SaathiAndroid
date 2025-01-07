package com.mosl.app.core.data.saathiIndirect.dashboard.IndirectIFADashboard

import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.saathi.api.model.saathiIndirect.dashboard.LstPeriodItem

object IndirectIFADashboardMapper {
    fun fnTranformFilterList( lstPeriod: List<LstPeriodItem?>?):List<LookUp>{
     return lstPeriod?.map { LookUp(0, it?.displayText?:"") }?: emptyList()
    }
}