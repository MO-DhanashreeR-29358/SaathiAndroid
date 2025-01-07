package com.mosl.tabapp.saathiIndirect.dashboard.foco.report.hierarchy

import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MoslConstant.dd_MMM_yyyy
import com.mosl.tabapp.dashboard.report.DashboardReportData
import com.mosl.tabapp.dashboard.report.DashboardReportFragment
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FILTER_LIST
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.FLT_DATE
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.LABEL
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.MAP
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.SPINNER_LIST
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.SYSTEM_NAME
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter.Companion.WIDGETNAME
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import com.mosl.tabapp.saathiIndirect.dashboard.foco.FocoDashboardFragment
import com.mosl.tabapp.saathiIndirect.dashboard.foco.report.FocoDashboardReportFragment
import dagger.Module
import dagger.Provides
import java.io.Serializable
import java.util.Date

@Module
class FocoHierarchyReportModule {
  @Provides
  internal fun provideView(fragment: FocoHierarchyReportFragment): FocoHierarchyReportView {
    return fragment
  }




}