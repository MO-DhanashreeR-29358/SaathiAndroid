package com.mosl.shared.util

import android.content.ContentResolver
import android.provider.Settings
import java.net.Inet4Address
import java.net.NetworkInterface

fun getLocalIpAddress(): String {
  try {
    val en = NetworkInterface.getNetworkInterfaces()
    while (en.hasMoreElements()) {
      val intf = en.nextElement()
      val enumIpAddr = intf.inetAddresses
      while (enumIpAddr.hasMoreElements()) {
        val inetAddress = enumIpAddr.nextElement()
        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
          return inetAddress.getHostAddress()
        }
      }
    }
  } catch (ex: Exception) {
    ex.printStackTrace()
  }
  return "127.0.0.1"
}

fun getDeviceID(contentResolver: ContentResolver): String {
  return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}