package com.mosl.tabapp.drawermenu.digitalSalesKit

import com.mosl.app.core.base.BaseView
import com.mosl.commons.data.UiModel
import com.mosl.mobile.domain.report.AssignedLeadReportModel
import com.mosl.mobile.domain.report.HierarchyReportCombo
import com.mosl.tabapp.report.HierarchyReportUIEvent.CellEvent
import io.reactivex.rxjava3.core.Observable

interface DigitalSalesKitView : BaseView {

    fun onViewChange(): Observable<Unit>
    fun renderViewChange()

    fun onItemClick(): Observable<String>
    fun renderItemClick(pdfPath: String)
}