package com.mosl.app.core.data.saathiIndirect.dashboard

import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoDashboardModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodFilterInput
import com.mosl.mobile.domain.saathiIndirect.dashboard.FocoPeriodModel
import com.mosl.mobile.domain.saathiIndirect.dashboard.Graph
import com.mosl.saathi.api.model.dashboard.target.FocoPeriodResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.FOCODashboardFilterpostReq
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectDashboardDetailsGraphCommonDataResponse
import com.mosl.saathi.api.model.saathiIndirect.dashboard.IndirectExtraDataCommonResponse

fun FocoPeriodFilterInput.requestTransform(): FOCODashboardFilterpostReq {
    return FOCODashboardFilterpostReq(
        employeeCode = employeeCode
    )
}

fun FocoDashboardInput.requestTransform(): FOCODashboardFilterpostReq {
    return FOCODashboardFilterpostReq(
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
        fitHierarchy = fitHierarchy
    )
}
fun List<FocoPeriodResponse>.itemTransform(): List<FocoPeriodModel> {
    return map {
        FocoPeriodModel(
            ord = it.ord,
            displayText = it.displayText ?: "",
            value = it.value ?: "0"


        )
    }
}

fun mapToIFADashboardMeetingGraph(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
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

fun mapToFocoDashboardDetailsModel(response: IndirectDashboardDetailsGraphCommonDataResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.response?.objectData?.graphTeamUnique?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph?.parameter,
                Value = originalGraph?.Val,
                currentMonth = "",
                lastMonth = ""
            )
            graphList.add(graph)
        }
    }
    val strTarget = ""
    val strAchieved = ""

    // Return the mapped FocoDashboardModel
    return FocoDashboardModel(
        graph = graphList,
        strTarget = strTarget,
        strAchieved = strAchieved
    )
}

fun mapToFocoDashboardMyTargetMetNotModel(response: IndirectDashboardDetailsGraphCommonDataResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.response?.objectData?.graphTargetNotMet?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph?.parameter,
                Value = originalGraph?.Val,
                currentMonth = "",
                lastMonth = ""
            )
            graphList.add(graph)
        }
    }
    val strTarget = ""
    val strAchieved = ""

    // Return the mapped FocoDashboardModel
    return FocoDashboardModel(
        graph = graphList,
        strTarget = strTarget,
        strAchieved = strAchieved
    )
}


fun mapToFocoDashboardMyUniqueDetailsModel(response: IndirectDashboardDetailsGraphCommonDataResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.response?.objectData?.graphMyUnique?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph?.parameter,
                Value = originalGraph?.Val,
                currentMonth = "",
                lastMonth = ""
            )
            graphList.add(graph)
        }
    }
    val strTarget = ""
    val strAchieved = ""

    // Return the mapped FocoDashboardModel
    return FocoDashboardModel(
        graph = graphList,
        strTarget = strTarget,
        strAchieved = strAchieved
    )
}

fun mapToFocoDashboardRevenueIpProductwiseModel(response: IndirectDashboardDetailsGraphCommonDataResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.response?.objectData?.graphProductWiseSales?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph?.product,
                Value = originalGraph?.value,
                currentMonth = "",
                lastMonth = ""
            )
            graphList.add(graph)
        }
    }
    val strTarget = ""
    val strAchieved = ""

    // Return the mapped FocoDashboardModel
    return FocoDashboardModel(
        graph = graphList,
        strTarget = strTarget,
        strAchieved = strAchieved
    )
}

fun mapToFocoDashboardModel(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.Response?.ObjectData?.graph?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph.product1 ,
                Value = originalGraph.Value,
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

fun mapToTotalMobileLoggedIn(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.Response?.ObjectData?.graph?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph.Category,
                Value = originalGraph.Valuedata
            )
            graphList.add(graph)
        }
    }

    // Extract target and achieved values from ObjectData
    val strTarget =getTarget(response.Response?.ObjectData?.strTarget, response.Response?.ObjectData?.target2)
    val strAchieved = response.Response?.ObjectData?.strAchieved

    // Return the mapped FocoDashboardModel
    return FocoDashboardModel(
        graph = graphList,
        strTarget = strTarget,
        strAchieved = strAchieved
    )
}

fun mapToNoOfClientsMeeting(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.Response?.ObjectData?.graph?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph.Parameter,
                Value = originalGraph.Val
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

fun mapToClientReach(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.Response?.ObjectData?.graph?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph.Series,
                Value = originalGraph.Value,
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
fun mapToIPSales(response: IndirectExtraDataCommonResponse): FocoDashboardModel {
    val graphList = ArrayList<Graph>()

    // Check if the response has valid data
    response.Response?.ObjectData?.graph?.let {
        // Map the Graph data from the original model to the FocoDashboardModel
        it.forEach { originalGraph ->
            val graph = Graph(
                product = originalGraph.product1,
                Value = originalGraph.Revenue,
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
