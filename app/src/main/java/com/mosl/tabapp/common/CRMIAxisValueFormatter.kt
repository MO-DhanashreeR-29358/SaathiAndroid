package com.mosl.tabapp.common

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.DecimalFormat

class CRMIAxisValueFormatter internal constructor() : IAxisValueFormatter {
  private val mFormat: DecimalFormat
  override fun getFormattedValue(value: Float, axis: AxisBase): String {
    return mFormat.format(value.toDouble())
  }

  init {
    mFormat = DecimalFormat("########0.00")
  }
}