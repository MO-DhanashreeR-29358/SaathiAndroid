package com.mosl.tabapp.leadViewDetail.leaddetail

import android.widget.EditText

data class LowMargin(
    //@JvmField val editText: EditText? = null,
  @JvmField val viewName: String,
  @JvmField val keyName: String,
  @JvmField val isMandatory: Boolean,
  @JvmField val isDefaultProduct: Boolean
) {
  var editText: EditText? = null
}