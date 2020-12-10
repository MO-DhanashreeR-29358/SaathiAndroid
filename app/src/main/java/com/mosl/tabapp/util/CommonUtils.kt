package com.mosl.tabapp.util

import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.mosl.app.coreui.spinner.setSpinnerData
import com.mosl.tabapp.common.RowData

fun emptyString() = ""

fun EditText.string(): String {
    return text.toString()
}

fun String?.nullSafe(): String {
    if (this == null)
        return emptyString()
    else
        return this
}

fun Spinner.setSpinnerData(
        arrayAdapter: ArrayAdapter<RowData>,
        rowData: List<RowData>
) {
    adapter = arrayAdapter
    arrayAdapter.setSpinnerData(this, rowData)
}