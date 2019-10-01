package com.mosl.shared.util

import android.content.ContentResolver
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Build
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

fun hasGPSDevice(context: Context): Boolean {
  val mgr = context
      .getSystemService(Context.LOCATION_SERVICE) as LocationManager
  val providers = mgr.allProviders ?: return false
  return providers.contains(LocationManager.GPS_PROVIDER)
}

fun isLocationFromMockProvider(
  context: Context,
  location: Location
): Boolean {
  val isMock: Boolean
  if (Build.VERSION.SDK_INT >= 18) {
    isMock = location.isFromMockProvider
  } else {
    isMock = isMockSettingsON(context)
  }
  return isMock
}

@Suppress("DEPRECATION") fun isMockSettingsON(context: Context): Boolean {
  return Settings.Secure.getString(
      context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION
  ) != "0"
}