package com.mosl.saathi.api.model.dashboard.actionablecount

import com.google.gson.annotations.SerializedName

data class ActionableCountItem(

  @JvmField @SerializedName("Table")
  val table: List<TableItem>?,

  @JvmField @SerializedName("Table1")
  val table1: List<Table1Item>?
)