package com.mosl.saathi.api.model.dashboard.incentive

import com.google.gson.annotations.SerializedName

data class IncentiveItem(

  @JvmField @SerializedName("Table")
  val table: List<TableItem>?,

  @JvmField @SerializedName("Table1")
  val table1: List<Table1Item>?
)