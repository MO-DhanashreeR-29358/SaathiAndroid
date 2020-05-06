package com.mosl.mobile.domain.dashboard

data class ActionableCount(
  @JvmField val table: List<Table>,
  @JvmField val table1: List<Table1>
)

data class Table(
  @JvmField val type: String
)

data class Table1(
  @JvmField val count: Int
)