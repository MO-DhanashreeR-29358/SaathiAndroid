package com.mosl.saathi.domain

import java.util.Date

data class NewsDate(
  @JvmField val original: Date,
  @JvmField val day: String,
  @JvmField val month: String,
  @JvmField val time: String
)

fun Date.toNewsDate(): NewsDate {
  return NewsDate(
      original = this,
      day = DF_DAY.formatOrDefault(this),
      month = DF_MONTH.formatOrDefault(this),
      time = DF_TIME.formatOrDefault(this)
  )
}