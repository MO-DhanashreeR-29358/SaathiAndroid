package com.mosl.mobile.domain.teamtarget

data class TeamTargetItemModel(

  @JvmField val table: List<TeamTableItemModel>?,
  @JvmField val table1: List<TeamTableItemModel>?,
  @JvmField val table2: List<TeamTableItemModel>?,
  @JvmField val table3: List<TeamTableItem3Model>?
)