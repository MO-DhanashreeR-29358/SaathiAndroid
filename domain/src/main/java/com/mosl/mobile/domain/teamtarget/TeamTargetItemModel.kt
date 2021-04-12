package com.mosl.mobile.domain.teamtarget

data class TeamTargetItemModel(

    @JvmField val table: List<TeamTableItemModel>,
    @JvmField val table1: List<TeamTableItemModel>,
    @JvmField val table2: List<TeamTableItemModel>,
    @JvmField val table4: List<TeamTableItemModel>,
    @JvmField val table3: List<TeamTableItem3Model>
) {
  fun tableListData(): MutableList<List<TeamTableItemModel>> {

    val teamTableItemModelList = mutableListOf<List<TeamTableItemModel>>()

    teamTableItemModelList.apply {

      add(table)
      add(table1)
        add(table2)
        add(table4)
    }
    return teamTableItemModelList

  }
}