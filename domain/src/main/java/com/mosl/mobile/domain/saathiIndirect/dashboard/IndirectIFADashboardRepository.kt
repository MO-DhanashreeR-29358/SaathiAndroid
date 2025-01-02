package com.mosl.mobile.domain.saathiIndirect.dashboard

import io.reactivex.rxjava3.core.Observable

interface IndirectIFADashboardRepository {

    fun fnGetIFADashboardFilter():Observable<String>

    fun fnGetTodaysBirthdayAnniversary():Observable<String>

    fun fnGetFirstMeetingNotDone():Observable<String>

    fun fnGetLeadsNotUpdated():Observable<String>

    fun fnGetSalesDashInactive():Observable<String>

    fun fnGetIFASalesDashboBuisnessSnapshot():Observable<String>

    fun fnGetIFASalesDashMyBuisnessSnapIP():Observable<String>

    fun fnGetIFADashREvenueIP():Observable<String>

    fun fnGetIFADashRevenueBroking():Observable<String>
    fun fnGetIFADashMeeting() :Observable<String>

    fun fnGetIFADashIFAACtivation() :Observable<String>
}