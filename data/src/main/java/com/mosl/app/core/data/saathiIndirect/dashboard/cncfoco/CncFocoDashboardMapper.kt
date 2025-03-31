package com.mosl.app.core.data.saathiIndirect.dashboard.cncfoco


import com.mosl.mobile.domain.createmeeting.IndirectFilterDataClass
import com.mosl.mobile.domain.createmeeting.LookUp
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectExtraDataCommonResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectIFADashboardFilterResponse

object CncFocoDashboardMapper {
    fun fnTranformFilterList( objIndirectIFADashboardFilterResponse: IndirectIFADashboardFilterResponse?): IndirectFilterDataClass {

        return  IndirectFilterDataClass(
            lstPeriods=objIndirectIFADashboardFilterResponse?.lstPeriod?.map { LookUp(0, it?.displayText?:"") }?: emptyList(),
            lstProduct = objIndirectIFADashboardFilterResponse?.lstProduct?.map { LookUp(0, it?.displayText?:"") }?: emptyList(),
            lstStageCode = objIndirectIFADashboardFilterResponse?.lstStageCode?.map { LookUp(0, it?.displayText?:"") }?: emptyList()
        )

    }

    fun mapToCncDashboardMeetingGraph(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
        val graphList = ArrayList<Graph>()

        // Check if the response has valid data
        response.Response?.ObjectData?.graph?.let {
            // Map the Graph data from the original model to the FocoDashboardModel
            it.forEach { originalGraph ->
                val graph = Graph(
                    product = originalGraph.product1 ,
                    Value = originalGraph.Val,
                    currentMonth = originalGraph.CurrentMonth,
                    lastMonth = originalGraph.LastMonth
                )
                graphList.add(graph)
            }
        }

        // Extract target and achieved values from ObjectData
        val strTarget = getTarget(response.Response?.ObjectData?.strTarget, response.Response?.ObjectData?.target2)
        val strAchieved = response.Response?.ObjectData?.strAchieved

        // Return the mapped FocoDashboardModel
        return FocoDashboardModel(
            graph = graphList,
            strTarget = strTarget,
            strAchieved = strAchieved
        )
    }

    fun mapToCncDashboardClientMeetingGraph(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
        val graphList = ArrayList<Graph>()

        // Check if the response has valid data
        response.Response?.ObjectData?.graph?.let {
            // Map the Graph data from the original model to the FocoDashboardModel
            it.forEach { originalGraph ->
                val graph = Graph(
                    product = originalGraph.Parameter ,
                    Value = originalGraph.Val,
                    currentMonth = originalGraph.CurrentMonth,
                    lastMonth = originalGraph.LastMonth
                )
                graphList.add(graph)
            }
        }

        // Extract target and achieved values from ObjectData
        val strTarget = getTarget(response.Response?.ObjectData?.strTarget, response.Response?.ObjectData?.target2)
        val strAchieved = response.Response?.ObjectData?.strAchieved

        // Return the mapped FocoDashboardModel
        return FocoDashboardModel(
            graph = graphList,
            strTarget = strTarget,
            strAchieved = strAchieved
        )
    }
    private fun getTarget(target1 : String?, target2 : String?): String{
        val toTarget1 = target1?.toDouble()
        val toTarget2 = target2?.toDouble()
        if (toTarget1 != null) {
            if(toTarget1 > 0){
                return target1
            }
        }
        if (toTarget2 != null) {
            if(toTarget2 > 0){
                return target2
            }
        }
        return "0"
    }

}