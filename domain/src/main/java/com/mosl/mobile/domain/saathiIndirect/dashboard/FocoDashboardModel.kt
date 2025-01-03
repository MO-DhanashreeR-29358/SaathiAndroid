package com.mosl.mobile.domain.saathiIndirect.dashboard

import com.mosl.saathi.api.model.common.ApiError
import com.mosl.saathi.api.model.common.PermissionModel

data class FocoDashboardModel(
      var graph             : ArrayList<Graph> = arrayListOf(),
      var strTarget            : String?              = null,
      var strAchieved          : String?              = null
)

data class Graph (
      var product           : String?  = null,
      var Value             : String?  = null
)