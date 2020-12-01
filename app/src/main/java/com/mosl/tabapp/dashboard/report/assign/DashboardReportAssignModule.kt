package com.mosl.tabapp.dashboard.report.assign

import com.mosl.tabapp.common.CRMAppUtil
import com.mosl.tabapp.common.FilterRowData
import com.mosl.tabapp.constants.MoslConstant
import com.mosl.tabapp.dashboard.report.DashboardReportData
import com.mosl.tabapp.dashboard.report.DashboardReportPresenter
import com.mosl.tabapp.model.TypeActionableFilterModel
import com.mosl.tabapp.model.dashboard.WidgetFilterModel
import dagger.Module
import dagger.Provides
import java.io.Serializable
import java.util.*

@Module
class DashboardReportAssignModule {
    @Provides
    internal fun provideView(fragment: DashboardReportAssignFragment): DashboardReportAssignlView {
        return fragment
    }

    @Provides
    internal fun provideDashboardReport(fragment: DashboardReportAssignFragment): DashboardReportData {
        val additionalMap: Serializable?
        var isDateFilter = false
        var typeFilter: List<TypeActionableFilterModel> = emptyList<TypeActionableFilterModel>()
        var periodFilter: List<FilterRowData> = emptyList<FilterRowData>()
        fragment.arguments?.apply {
            additionalMap =
                this.getSerializable(DashboardReportPresenter.MAP) as HashMap<String, String>
            val label: String?
            if (this.containsKey(DashboardReportPresenter.WIDGETNAME)) {
                label = String.format(
                    "%s - %s", this.getString(DashboardReportPresenter.LABEL), this.getString(
                        DashboardReportPresenter.WIDGETNAME
                    )
                )
            } else {
                label = this.getString(DashboardReportPresenter.LABEL)
            }
            if (this.containsKey(DashboardReportPresenter.FILTER_LIST)) {
                val filterList =
                    this.getParcelableArrayList<WidgetFilterModel>(DashboardReportPresenter.FILTER_LIST)
                        ?: emptyList<WidgetFilterModel>()
                if (filterList.isNotEmpty()) {
                    if (filterList[0].keyValue.equals(
                            DashboardReportPresenter.FLT_DATE,
                            ignoreCase = true
                        )
                    ) {
                        isDateFilter = true
                        additionalMap[DashboardReportPresenter.FLT_DATE] = CRMAppUtil.getDateStr(
                            Date(),
                            MoslConstant.dd_MMM_yyyy
                        )
                    }
                    if (filterList[0].filterData.isNotEmpty()) {
                        periodFilter = filterList[0].filterData
                    }
                }
            }

            if (this.containsKey(DashboardReportPresenter.SPINNER_LIST)) {
                typeFilter =
                    this.getParcelableArrayList<TypeActionableFilterModel>(DashboardReportPresenter.SPINNER_LIST)
                        ?: emptyList()
            }

            return DashboardReportData(
                additionalMap as Map<String, String>,
                label,
                this.getString(DashboardReportPresenter.SYSTEM_NAME),
                periodFilter,
                typeFilter,
                isDateFilter
            )
        }
        return DashboardReportData(
            emptyMap<String, String>(),
            "",
            "",
            emptyList<FilterRowData>(),
            emptyList<TypeActionableFilterModel>(),
            false
        )
    }

}