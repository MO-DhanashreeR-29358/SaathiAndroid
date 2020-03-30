package com.mosl.mobile.domain.lead.report

data class LeadsReportModel(

  @JvmField val header: Array<String?>,

  @JvmField val secondHeader: Array<String?>,

  @JvmField val listData: List<LinkedHashMap<String, String?>>,

  @JvmField val groupData: Map<Int, MutableList<Map<Int, String>>>

)