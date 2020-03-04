package com.mosl.tabapp.ekyc

import com.mosl.mobile.user.login.UserManager
import com.mosl.mobile.user.login.user
import timber.log.Timber

fun UserManager.getDataForEkyc(
  commandType: String,
  userId: String
): String {
  val map = mapOf(
      NEW_USERID to userId, BRANCHEMPCODE to user.employeeCode, COMMANDTYPE to commandType,
      APPLICATIONTYPE to CRM,
      OSTYPE to ANDROID, APPLOGINID to user.employeeCode, TOKENKEY to user.token,
    ISAUFSB to user.isAufsb(), AUSALESSUPPORT to user.isAuBankSalesSupport()
  )
  Timber.d("input data %s", formatInputData(map))
  return formatInputData(map)
}

fun formatInputData(map: Map<String, Any>): String {
  var result = ""
  if (map.size > 0) {
    val keys = map.keys
    for (key in keys) {
      val value = if (map.get(key) == null) null else map.get(key)!!.toString()
      if (value == null) {
        result += key + "=" + "" + "&"
      } else {
        result += key + "=" + value + "&"
      }
    }
    result = result.substring(0, result.length - 1)
  }
  return result
}