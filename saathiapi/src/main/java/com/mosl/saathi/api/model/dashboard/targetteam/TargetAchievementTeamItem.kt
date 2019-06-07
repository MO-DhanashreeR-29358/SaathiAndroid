package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName

data class TargetAchievementTeamItem(
  @JvmField @SerializedName("Table")
  val table: List<TableItem>?,

  @JvmField @SerializedName("Table2")
  val table2: List<Table2Item>?,

  @JvmField @SerializedName("Table3")
  val table3: List<Table3Item>?,

  @JvmField @SerializedName("Table1")
  val table1: List<Table1Item>?
)