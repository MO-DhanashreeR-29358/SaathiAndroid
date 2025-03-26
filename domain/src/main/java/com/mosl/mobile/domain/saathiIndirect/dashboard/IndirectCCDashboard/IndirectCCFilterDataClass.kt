package com.mosl.mobile.domain.saathiIndirect.dashboard.IndirectCCDashboard

import com.mosl.mobile.domain.createmeeting.LookUp

data class IndirectCCFilterDataClass(
    @JvmField var lstPeriods:List<LookUp>,
    @JvmField var lstAdvisor:List<LookUp>
)
