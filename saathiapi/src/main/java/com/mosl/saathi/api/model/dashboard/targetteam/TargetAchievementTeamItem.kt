package com.mosl.saathi.api.model.dashboard.targetteam

import com.google.gson.annotations.SerializedName

data class TargetAchievementTeamItem(
  @JvmField @SerializedName("Table")
  val table: List<TableItem>?,

  @JvmField @SerializedName("Table2")
  val table2: List<TableItem>?,

  @JvmField @SerializedName("Table3")
  val table3: List<Table3Item>?,

  @JvmField @SerializedName("Table1")
  val table1: List<TableItem>?
)